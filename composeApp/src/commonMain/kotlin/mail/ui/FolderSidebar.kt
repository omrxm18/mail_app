package mail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import mail.model.FolderKind
import mail.model.MailFolder
import mail.state.MailViewModel
import mail.theme.AppColors
import mail.theme.AppTextStyles
import mail.theme.rememberAppText

/**
 * @param mobileExpanded when true, fills available width (mobile full-screen) and
 * tapping a folder advances mobile nav to the message list. When false (default),
 * renders as the fixed-width desktop rail and only updates the selected folder.
 */
@Composable
fun FolderSidebar(viewModel: MailViewModel, mobileExpanded: Boolean = false) {
    val folders by viewModel.folders.collectAsState()
    val selected by viewModel.selectedFolder.collectAsState()
    val text = rememberAppText()
    val accountTag = viewModel.accounts.joinToString(" · ") { it.shortLabel }

    val content: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
                    .borderBottom(AppColors.border),
            ) {
                Text(
                    text = accountTag.uppercase(),
                    style = text.accountTag,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 14.dp),
                )
            }
            for (folder in folders) {
                FolderRow(
                    folder = folder,
                    isActive = folder.kind == selected,
                    text = text,
                    onClick = { viewModel.selectFolder(folder.kind, mobileExpanded) },
                )
            }
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .borderTop(AppColors.border)
                    .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(AppColors.statusGreen),
                )
                Spacer(Modifier.width(6.dp))
                Text("idle · synced 14:02", style = text.syncStatus)
            }
        }
    }

    if (mobileExpanded) {
        Box(modifier = Modifier.fillMaxSize().background(AppColors.bgElevated)) { content() }
        return
    }

    Row(modifier = Modifier.fillMaxHeight()) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .background(AppColors.bgElevated),
        ) { content() }
        Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(AppColors.border))
    }
}

@Composable
private fun FolderRow(
    folder: MailFolder,
    isActive: Boolean,
    text: AppTextStyles,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isActive) AppColors.bgHover else Color.Transparent)
            .then(if (isActive) Modifier.leftAccentBar(AppColors.accent) else Modifier)
            .clickable(onClick = onClick)
            .padding(PaddingValues(horizontal = 16.dp, vertical = 7.dp)),
    ) {
        Text(folder.displayName, style = if (isActive) text.folderLabelActive else text.folderLabel)
        Text(
            folder.unreadCount.toString(),
            style = if (folder.unreadCount > 0) text.folderCount else text.folderCountZero,
        )
    }
}
