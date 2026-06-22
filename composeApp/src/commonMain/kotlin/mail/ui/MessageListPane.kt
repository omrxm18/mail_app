package mail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import mail.format.formatMessageTime
import mail.model.Message
import mail.state.MailViewModel
import mail.theme.AppColors
import mail.theme.AppTextStyles
import mail.theme.rememberAppText

/**
 * @param mobileExpanded when true, fills available width (mobile full-screen) and
 * tapping a message advances mobile nav to the reading screen.
 */
@Composable
fun MessageListPane(viewModel: MailViewModel, mobileExpanded: Boolean = false) {
    val folder by viewModel.selectedFolder.collectAsState()
    val messages by viewModel.visibleMessages.collectAsState()
    val selectedId by viewModel.selectedMessageId.collectAsState()
    val unreadCount = messages.count { it.isUnread }
    val text = rememberAppText()

    val content: @Composable () -> Unit = {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .borderBottom(AppColors.border)
                    .padding(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 10.dp),
            ) {
                Text(folder.name.lowercase().replaceFirstChar { it.uppercase() }, style = text.listHeaderTitle)
                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(AppColors.accentTagBg)
                            .padding(horizontal = 6.dp, vertical = 1.dp),
                    ) {
                        Text("[$unreadCount]", style = text.mono.copy(fontSize = text.folderCount.fontSize, color = AppColors.accent))
                    }
                }
            }
            if (messages.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nothing here yet.", style = text.msgPreview)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(messages, key = { it.id }) { message ->
                        MessageRow(
                            message = message,
                            isSelected = !mobileExpanded && message.id == selectedId,
                            text = text,
                            onClick = { viewModel.selectMessage(message.id, mobileExpanded) },
                        )
                    }
                }
            }
        }
    }

    if (mobileExpanded) {
        Box(modifier = Modifier.fillMaxSize().background(AppColors.bg)) { content() }
        return
    }

    Row(modifier = Modifier.fillMaxHeight()) {
        Box(modifier = Modifier.width(320.dp).fillMaxHeight().background(AppColors.bg)) { content() }
        Box(modifier = Modifier.fillMaxHeight().width(1.dp).background(AppColors.border))
    }
}

@Composable
private fun MessageRow(
    message: Message,
    isSelected: Boolean,
    text: AppTextStyles,
    onClick: () -> Unit,
) {
    val unread = message.isUnread

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) AppColors.bgElevated else Color.Transparent)
            .then(if (isSelected) Modifier.leftAccentBar(AppColors.accent) else Modifier)
            .borderBottom(AppColors.border)
            .clickable(onClick = onClick)
            .padding(start = if (isSelected) 14.dp else 16.dp, top = 10.dp, end = 16.dp, bottom = 10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            UnreadMarker(visible = unread)
            Text(
                message.fromName,
                style = if (unread) text.msgFrom else text.msgFromRead,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            Text(formatMessageTime(message.receivedAt), style = text.msgTime)
        }
        Spacer(Modifier.height(3.dp))
        Text(
            message.subject,
            style = if (unread) text.msgSubjectUnread else text.msgSubject,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(3.dp))
        Text(message.preview, style = text.msgPreview, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun UnreadMarker(visible: Boolean) {
    Box(modifier = Modifier.padding(end = 6.dp).width(5.dp).height(5.dp)) {
        if (visible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(AppColors.accent),
            )
        }
    }
}
