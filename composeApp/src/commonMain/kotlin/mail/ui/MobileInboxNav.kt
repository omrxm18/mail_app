package mail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mail.state.MailViewModel
import mail.state.MobileScreen
import mail.theme.AppColors
import mail.theme.rememberAppText

/**
 * Single-pane navigation for narrow screens. Shows one of folders / message
 * list / reading at a time. The platform back gesture/button should call
 * viewModel.goBackMobile() before popping — wired per-platform (see
 * androidMain's MainActivity, which intercepts OnBackPressedCallback).
 */
@Composable
fun MobileInboxNav(viewModel: MailViewModel) {
    val screen by viewModel.mobileScreen.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(AppColors.bg)) {
        MobileTopBar(viewModel, screen)
        when (screen) {
            MobileScreen.FOLDERS -> FolderSidebar(viewModel, mobileExpanded = true)
            MobileScreen.MESSAGE_LIST -> MessageListPane(viewModel, mobileExpanded = true)
            MobileScreen.READING -> ReadingPane(viewModel)
        }
    }
}

@Composable
private fun MobileTopBar(viewModel: MailViewModel, screen: MobileScreen) {
    val folder by viewModel.selectedFolder.collectAsState()
    val message by viewModel.selectedMessage.collectAsState()
    val text = rememberAppText()

    val title = when (screen) {
        MobileScreen.FOLDERS -> "mail"
        MobileScreen.MESSAGE_LIST -> folder.name.lowercase().replaceFirstChar { it.uppercase() }
        MobileScreen.READING -> message?.fromName ?: ""
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.bgElevated)
            .borderBottom(AppColors.border)
            .height(56.dp)
            .padding(horizontal = 4.dp),
    ) {
        if (screen != MobileScreen.FOLDERS) {
            IconButton(onClick = { viewModel.goBackMobile() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppColors.text)
            }
        } else {
            Spacer(Modifier.padding(start = 16.dp))
        }
        Text(
            title,
            style = if (screen == MobileScreen.READING) text.readingFrom else text.listHeaderTitle,
            modifier = Modifier.padding(start = if (screen == MobileScreen.FOLDERS) 16.dp else 0.dp),
        )
    }
}
