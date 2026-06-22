package mail.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import mail.data.MockMailSource
import mail.model.Account
import mail.model.FolderKind
import mail.model.MailFolder
import mail.model.Message

/**
 * Which screen is showing on narrow (mobile) layouts. Desktop ignores this
 * entirely — it's only consulted when width < InboxScreen's mobile breakpoint.
 * Kept as explicit state (not derived from selectedMessageId) so "back" can
 * return to the list without clearing which message was open.
 */
enum class MobileScreen { FOLDERS, MESSAGE_LIST, READING }

/**
 * Single state holder for the mail UI. Real implementation later: folders/messages
 * get backed by a local cache refreshed by a sync engine — everything downstream
 * (the composables in mail.ui) only reads from this class's StateFlows.
 */
class MailViewModel : ViewModel() {

    /** Static for now — becomes a real suspend load once account setup exists. */
    val accounts: List<Account> = MockMailSource.accounts

    private val _folders = MutableStateFlow(MockMailSource.folders)
    val folders: StateFlow<List<MailFolder>> = _folders.asStateFlow()

    private val _selectedFolder = MutableStateFlow(FolderKind.INBOX)
    val selectedFolder: StateFlow<FolderKind> = _selectedFolder.asStateFlow()

    private val _messages = MutableStateFlow(MockMailSource.messages())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _selectedMessageId = MutableStateFlow<String?>(null)
    val selectedMessageId: StateFlow<String?> = _selectedMessageId.asStateFlow()

    private val _mobileScreen = MutableStateFlow(MobileScreen.MESSAGE_LIST)
    val mobileScreen: StateFlow<MobileScreen> = _mobileScreen.asStateFlow()

    /** Messages filtered to the currently selected folder, newest first. */
    val visibleMessages: StateFlow<List<Message>> = combine(_selectedFolder, _messages) { folder, all ->
        all.filter { it.folder == folder.key }.sortedByDescending { it.receivedAt }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /** The full Message for whatever is selected, if any. */
    val selectedMessage: StateFlow<Message?> = combine(_selectedMessageId, _messages) { id, all ->
        all.firstOrNull { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun selectFolder(kind: FolderKind, mobileExpanded: Boolean) {
        _selectedFolder.value = kind
        if (mobileExpanded) _mobileScreen.value = MobileScreen.MESSAGE_LIST
    }

    fun selectMessage(id: String, mobileExpanded: Boolean) {
        _selectedMessageId.value = id
        markRead(id)
        if (mobileExpanded) _mobileScreen.value = MobileScreen.READING
    }

    fun markRead(id: String) {
        _messages.update { list -> list.map { if (it.id == id) it.copy(isUnread = false) else it } }
    }

    fun markUnread(id: String) {
        _messages.update { list -> list.map { if (it.id == id) it.copy(isUnread = true) else it } }
    }

    /** Steps mobile nav up one level; used by the system back button/gesture. */
    fun goBackMobile() {
        _mobileScreen.value = when (_mobileScreen.value) {
            MobileScreen.READING -> MobileScreen.MESSAGE_LIST
            MobileScreen.MESSAGE_LIST -> MobileScreen.FOLDERS
            MobileScreen.FOLDERS -> MobileScreen.FOLDERS // nothing above this — caller lets the app exit
        }
    }
}
