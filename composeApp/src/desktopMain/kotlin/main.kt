import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ghn.poker.tracker.di.initKoin
import com.ghn.poker.tracker.ui.App
import org.koin.dsl.module

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        initKoin {
            module {}
        }
        App()
    }
}

@Preview
@Composable
fun AppDesktopPreview() {
    App()
}