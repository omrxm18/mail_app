package mail.model

enum class AccountProvider { GMAIL, PROTON }

data class Account(
    val id: String,
    val emailAddress: String,
    val provider: AccountProvider,
) {
    /** Short label used in the sidebar account tag, e.g. "omar@gmail" */
    val shortLabel: String
        get() {
            val namePart = emailAddress.substringBefore('@')
            val providerLabel = if (provider == AccountProvider.GMAIL) "gmail" else "proton"
            return "$namePart@$providerLabel"
        }
}
