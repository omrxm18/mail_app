package mail.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** A single hairline border on one edge, matching Flutter's Border(bottom/top/left/right: BorderSide(...)). */
fun Modifier.borderBottom(color: Color, thickness: Dp = 1.dp): Modifier = drawBehind {
    val strokeWidth = thickness.toPx()
    drawLine(color, Offset(0f, size.height - strokeWidth / 2), Offset(size.width, size.height - strokeWidth / 2), strokeWidth)
}

fun Modifier.borderTop(color: Color, thickness: Dp = 1.dp): Modifier = drawBehind {
    val strokeWidth = thickness.toPx()
    drawLine(color, Offset(0f, strokeWidth / 2), Offset(size.width, strokeWidth / 2), strokeWidth)
}

fun Modifier.borderRight(color: Color, thickness: Dp = 1.dp): Modifier = drawBehind {
    val strokeWidth = thickness.toPx()
    drawLine(color, Offset(size.width - strokeWidth / 2, 0f), Offset(size.width - strokeWidth / 2, size.height), strokeWidth)
}

/** Left accent bar used to mark the active folder / selected message row. */
fun Modifier.leftAccentBar(color: Color, thickness: Dp = 2.dp): Modifier = drawBehind {
    val strokeWidth = thickness.toPx()
    drawLine(color, Offset(strokeWidth / 2, 0f), Offset(strokeWidth / 2, size.height), strokeWidth)
}
