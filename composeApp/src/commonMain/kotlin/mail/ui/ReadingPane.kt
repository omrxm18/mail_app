package mail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import mail.format.formatReadingDate
import mail.state.MailViewModel
import mail.theme.AppColors
import mail.theme.rememberAppText

/**
 * Renders the open message. Always fills the space its caller gives it — the
 * caller decides sizing (InboxScreen wraps it in a weighted Box for the
 * desktop Row, MobileInboxNav gives it the full screen).
 */
@Composable
fun ReadingPane(viewModel: MailViewModel) {
    val message by viewModel.selectedMessage.collectAsState()
    val text = rememberAppText()

    if (message == null) {
        Box(modifier = Modifier.fillMaxSize().background(AppColors.bg), contentAlignment = Alignment.Center) {
            Text("Select a message to read it.", style = text.msgPreview)
        }
        return
    }
    val m = message!!

    Column(modifier = Modifier.fillMaxSize().background(AppColors.bg)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .borderBottom(AppColors.border)
                .padding(start = 24.dp, top = 18.dp, end = 24.dp, bottom = 16.dp),
        ) {
            Text(m.subject, style = text.readingSubject)
            Spacer(Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text(m.fromName, style = text.readingFrom)
                    Spacer(Modifier.height(2.dp))
                    Text(m.fromAddress, style = text.readingAddr)
                }
                Text(formatReadingDate(m.receivedAt), style = text.readingDate)
            }
            Spacer(Modifier.height(14.dp))
            Row {
                ActionButton(label = "Reply", primary = true, onClick = {})
                Spacer(Modifier.width(8.dp))
                ActionButton(label = "Archive", onClick = {})
                Spacer(Modifier.width(8.dp))
                ActionButton(label = "Mark unread", onClick = { viewModel.markUnread(m.id) })
                Spacer(Modifier.width(8.dp))
                ActionButton(label = "View source →", onClick = {})
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        ) {
            Text(m.body, style = text.readingBody)
        }
    }
}

@Composable
private fun ActionButton(label: String, onClick: () -> Unit, primary: Boolean = false) {
    val text = rememberAppText()
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .background(if (primary) AppColors.accentDim else AppColors.bgElevated)
            .border(1.dp, if (primary) AppColors.accent else AppColors.border, RoundedCornerShape(3.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(label, style = if (primary) text.btnLabelPrimary else text.btnLabel)
    }
}
