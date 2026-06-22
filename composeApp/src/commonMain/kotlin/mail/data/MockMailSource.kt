package mail.data

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import mail.model.Account
import mail.model.AccountProvider
import mail.model.FolderKind
import mail.model.MailFolder
import mail.model.Message

/**
 * Stand-in for real IMAP data while the UI is being built. Once the sync engine
 * exists, this gets replaced by a repository backed by a local cache + IMAP
 * client — MailViewModel is the only place that needs to change.
 */
object MockMailSource {

    val accounts = listOf(
        Account(id = "acc_gmail", emailAddress = "omar@gmail.com", provider = AccountProvider.GMAIL),
        Account(id = "acc_proton", emailAddress = "omar@proton.me", provider = AccountProvider.PROTON),
    )

    val folders = listOf(
        MailFolder(FolderKind.INBOX, "Inbox", unreadCount = 3),
        MailFolder(FolderKind.STARRED, "Starred", unreadCount = 0),
        MailFolder(FolderKind.SENT, "Sent", unreadCount = 0),
        MailFolder(FolderKind.DRAFTS, "Drafts", unreadCount = 1),
        MailFolder(FolderKind.ARCHIVE, "Archive", unreadCount = 0),
        MailFolder(FolderKind.TRASH, "Trash", unreadCount = 0),
    )

    /** Fixed reference "now" that the mock data below is authored against. */
    val fixedNow = LocalDateTime(2026, 6, 21, 14, 2)

    private fun before(amount: Duration): LocalDateTime =
        fixedNow.toInstant(TimeZone.UTC).minus(amount).toLocalDateTime(TimeZone.UTC)

    fun messages(): List<Message> = listOf(
        Message(
            id = "msg_1",
            accountId = "acc_gmail",
            folder = "inbox",
            fromName = "GitHub",
            fromAddress = "notifications@github.com",
            subject = "[omrxdev/qtstock-importer] CI run failed on windows-build",
            preview = "cargo build --release --lib exited with code 101 — error[E0433]: failed to resolve...",
            body = """
                Workflow run #142 failed for windows-build in omrxdev/qtstock-importer.

                error[E0433]: failed to resolve: use of undeclared crate or module `windows_sys`
                  --> src\lib.rs:14:5
                cargo build --release --lib exited with code 101

                This is the first failure on this branch since the FFI bridge was added last week. The Android job (cargo-ndk, arm64-v8a) completed successfully.

                — Sent from GitHub Actions, on behalf of omrxdev/qtstock-importer
            """.trimIndent(),
            receivedAt = fixedNow,
            isUnread = true,
        ),
        Message(
            id = "msg_2",
            accountId = "acc_proton",
            folder = "inbox",
            fromName = "Proton Mail Team",
            fromAddress = "no-reply@proton.me",
            subject = "Your Bridge session will expire soon",
            preview = "To keep IMAP/SMTP access working, refresh your Bridge session before...",
            body = """
                Your Proton Mail Bridge session is set to expire in 48 hours.

                To keep IMAP and SMTP access working for connected clients, open Bridge and re-authenticate your account. No mail will be lost, but new messages will stop syncing until you refresh the session.
            """.trimIndent(),
            receivedAt = before(2.hours + 15.minutes),
            isUnread = true,
        ),
        Message(
            id = "msg_3",
            accountId = "acc_gmail",
            folder = "inbox",
            fromName = "Tailscale",
            fromAddress = "notify@tailscale.com",
            subject = "New device connected to your tailnet",
            preview = "homelab-relay joined your network 2 minutes ago from 100.x.x.x...",
            body = """
                A new device joined your tailnet.

                Device: homelab-relay
                IP: 100.x.x.x
                Joined: just now

                If this wasn't you, revoke this device from the admin console.
            """.trimIndent(),
            receivedAt = before(4.hours + 47.minutes),
            isUnread = true,
        ),
        Message(
            id = "msg_4",
            accountId = "acc_gmail",
            folder = "inbox",
            fromName = "Arch Linux News",
            fromAddress = "news@archlinux.org",
            subject = "linux 6.15.3 now in [core]",
            preview = "A new kernel build is available, requiring a manual intervention for...",
            body = """
                linux 6.15.3.arch1-1 has moved to the [core] repository.

                This update requires a manual intervention: users with custom kernel signing hooks should verify their post-transaction hooks still match the new module paths.
            """.trimIndent(),
            receivedAt = before(1.days),
        ),
        Message(
            id = "msg_5",
            accountId = "acc_gmail",
            folder = "inbox",
            fromName = "npm",
            fromAddress = "support@npmjs.com",
            subject = "Security advisory affecting 1 dependency",
            preview = "Dependabot detected a moderate severity vulnerability in...",
            body = """
                Dependabot detected a moderate severity vulnerability in one of your dependencies.

                Repository: whatsapp-bot
                Package: affected-package@1.2.3

                Review the suggested fix in your Dependabot alerts.
            """.trimIndent(),
            receivedAt = before(1.days + 1.hours),
        ),
        Message(
            id = "msg_6",
            accountId = "acc_proton",
            folder = "inbox",
            fromName = "Cloudflare",
            fromAddress = "noreply@cloudflare.com",
            subject = "Your weekly traffic summary",
            preview = "cgit.omar.dev received 412 requests this week, up 8% from...",
            body = """
                Your weekly traffic summary is ready.

                cgit.omar.dev received 412 requests this week, up 8% from last week. No security events were flagged.
            """.trimIndent(),
            receivedAt = before(2.days),
        ),
    )
}
