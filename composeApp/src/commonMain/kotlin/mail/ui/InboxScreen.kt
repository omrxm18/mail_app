package mail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mail.state.MailViewModel
import mail.theme.AppColors

/**
 * Below this width, the three-pane desktop layout can't fit (sidebar + list
 * alone need ~520dp before the reading pane gets any room) — switch to
 * single-pane mobile navigation instead.
 */
private val MOBILE_BREAKPOINT = 700.dp

@Composable
fun InboxScreen(viewModel: MailViewModel) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(AppColors.bg)) {
        if (maxWidth < MOBILE_BREAKPOINT) {
            MobileInboxNav(viewModel)
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                FolderSidebar(viewModel)
                MessageListPane(viewModel)
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    ReadingPane(viewModel)
                }
            }
        }
    }
}
