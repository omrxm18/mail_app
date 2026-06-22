package mail.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mail.resources.Res
import mail.resources.inter_bold
import mail.resources.inter_medium
import mail.resources.inter_regular
import mail.resources.inter_semi_bold
import mail.resources.jetbrains_mono_bold
import mail.resources.jetbrains_mono_medium
import mail.resources.jetbrains_mono_regular
import org.jetbrains.compose.resources.Font

/**
 * Color tokens, lifted directly from the approved HTML mockup so the app
 * matches what was signed off on. Widgets should reference AppColors, not
 * raw hex values.
 */
object AppColors {
    val bg = Color(0xFF16181C)
    val bgElevated = Color(0xFF1C1F25)
    val bgHover = Color(0xFF21242B)
    val border = Color(0xFF2A2D34)
    val text = Color(0xFFE4E6EB)
    val textDim = Color(0xFF8B92A0)
    val textFaint = Color(0xFF5A5F6B)
    val accent = Color(0xFFD98B4A)
    val accentDim = Color(0xFF6B4A30)
    val accentTagBg = Color(0x1FD98B4A) // accent at ~12% opacity
    val statusGreen = Color(0xFF5FB37A)
    val primaryButtonLabel = Color(0xFFF4DCC4)
}

/**
 * Text style shortcuts used across the mail UI, matching Flutter's AppText.
 * Custom fonts must be loaded through a Composable (Font() needs a resource
 * loader), so these are exposed via rememberAppText() instead of top-level
 * constants.
 */
data class AppTextStyles(
    val mono: TextStyle,
    val accountTag: TextStyle,
    val folderLabel: TextStyle,
    val folderLabelActive: TextStyle,
    val folderCount: TextStyle,
    val folderCountZero: TextStyle,
    val listHeaderTitle: TextStyle,
    val msgFrom: TextStyle,
    val msgFromRead: TextStyle,
    val msgTime: TextStyle,
    val msgSubject: TextStyle,
    val msgSubjectUnread: TextStyle,
    val msgPreview: TextStyle,
    val readingSubject: TextStyle,
    val readingFrom: TextStyle,
    val readingAddr: TextStyle,
    val readingDate: TextStyle,
    val readingBody: TextStyle,
    val btnLabel: TextStyle,
    val btnLabelPrimary: TextStyle,
    val syncStatus: TextStyle,
)

@Composable
private fun rememberInterFamily(): FontFamily = remember {
    FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semi_bold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold),
    )
}

@Composable
private fun rememberMonoFamily(): FontFamily = remember {
    FontFamily(
        Font(Res.font.jetbrains_mono_regular, FontWeight.Normal),
        Font(Res.font.jetbrains_mono_medium, FontWeight.Medium),
        Font(Res.font.jetbrains_mono_bold, FontWeight.Bold),
    )
}

@Composable
fun rememberAppText(): AppTextStyles {
    val inter = rememberInterFamily()
    val mono = rememberMonoFamily()
    return remember(inter, mono) {
        AppTextStyles(
            mono = TextStyle(fontFamily = mono),
            accountTag = TextStyle(
                fontFamily = mono, fontSize = 11.sp,
                color = AppColors.textFaint, letterSpacing = 0.6.sp,
            ),
            folderLabel = TextStyle(fontFamily = inter, fontSize = 13.5.sp, color = AppColors.textDim),
            folderLabelActive = TextStyle(fontFamily = inter, fontSize = 13.5.sp, color = AppColors.text),
            folderCount = TextStyle(fontFamily = mono, fontSize = 11.sp, color = AppColors.accent),
            folderCountZero = TextStyle(fontFamily = mono, fontSize = 11.sp, color = AppColors.textFaint),
            listHeaderTitle = TextStyle(
                fontFamily = inter, fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold, color = AppColors.text,
            ),
            msgFrom = TextStyle(
                fontFamily = inter, fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold, color = AppColors.text,
            ),
            msgFromRead = TextStyle(
                fontFamily = inter, fontSize = 13.sp,
                fontWeight = FontWeight.Medium, color = AppColors.textDim,
            ),
            msgTime = TextStyle(fontFamily = mono, fontSize = 10.5.sp, color = AppColors.textFaint),
            msgSubject = TextStyle(fontFamily = inter, fontSize = 12.5.sp, color = AppColors.textDim),
            msgSubjectUnread = TextStyle(fontFamily = inter, fontSize = 12.5.sp, color = AppColors.text),
            msgPreview = TextStyle(fontFamily = inter, fontSize = 11.5.sp, color = AppColors.textFaint),
            readingSubject = TextStyle(
                fontFamily = inter, fontSize = 18.sp,
                fontWeight = FontWeight.Bold, color = AppColors.text,
            ),
            readingFrom = TextStyle(fontFamily = inter, fontSize = 13.sp, color = AppColors.text),
            readingAddr = TextStyle(fontFamily = mono, fontSize = 11.5.sp, color = AppColors.textFaint),
            readingDate = TextStyle(fontFamily = mono, fontSize = 11.sp, color = AppColors.textFaint),
            readingBody = TextStyle(
                fontFamily = inter, fontSize = 14.sp,
                lineHeight = 23.1.sp, color = AppColors.textDim,
            ),
            btnLabel = TextStyle(
                fontFamily = mono, fontSize = 11.sp,
                color = AppColors.textDim, letterSpacing = 0.4.sp,
            ),
            btnLabelPrimary = TextStyle(
                fontFamily = mono, fontSize = 11.sp,
                color = AppColors.primaryButtonLabel, letterSpacing = 0.4.sp,
            ),
            syncStatus = TextStyle(fontFamily = mono, fontSize = 10.5.sp, color = AppColors.textFaint),
        )
    }
}
