package mail

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import mail.state.MailViewModel
import mail.theme.AppColors
import mail.ui.InboxScreen

@Composable
fun App() {
    val viewModel: MailViewModel = viewModel { MailViewModel() }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = AppColors.bg,
            surface = AppColors.bg,
            primary = AppColors.accent,
            onSurface = AppColors.text,
        ),
    ) {
        Surface(color = AppColors.bg) {
            InboxScreen(viewModel)
        }
    }
}
