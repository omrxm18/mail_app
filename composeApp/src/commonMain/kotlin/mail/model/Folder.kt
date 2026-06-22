package mail.model

enum class FolderKind {
    INBOX, STARRED, SENT, DRAFTS, ARCHIVE, TRASH;

    /** Lowercase key matching the folder string used on Message.folder, e.g. "inbox". */
    val key: String get() = name.lowercase()
}

data class MailFolder(
    val kind: FolderKind,
    val displayName: String,
    val unreadCount: Int,
)
