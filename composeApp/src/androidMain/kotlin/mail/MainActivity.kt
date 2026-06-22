package mail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import mail.state.MailViewModel
import mail.state.MobileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MailViewModel = viewModel { MailViewModel() }

            // Mirrors Flutter's PopScope: step through mail nav (reading -> list ->
            // folders) before letting the system back gesture exit the app.
            val backCallback = remember(viewModel) {
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (viewModel.mobileScreen.value == MobileScreen.FOLDERS) {
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                            isEnabled = true
                        } else {
                            viewModel.goBackMobile()
                        }
                    }
                }
            }
            DisposableEffect(backCallback) {
                onBackPressedDispatcher.addCallback(backCallback)
                onDispose { backCallback.remove() }
            }

            App()
        }
    }
}
