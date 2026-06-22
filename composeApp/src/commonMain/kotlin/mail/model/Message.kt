package mail.model

import kotlinx.datetime.LocalDateTime

/**
 * [folder] is a raw string key (matching FolderKind.key, e.g. "inbox") rather than
 * the enum directly, since server-side folder names ("[Gmail]/Sent Mail") don't map
 * 1:1 to FolderKind once real sync exists.
 */
data class Message(
    val id: String,
    val accountId: String,
    val folder: String,
    val fromName: String,
    val fromAddress: String,
    val subject: String,
    val preview: String,
    val body: String,
    val receivedAt: LocalDateTime,
    val isUnread: Boolean = false,
    val isStarred: Boolean = false,
)
