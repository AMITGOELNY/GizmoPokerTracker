@file:Suppress("FunctionName")

import androidx.compose.ui.window.ComposeUIViewController
import com.ghn.poker.tracker.di.initKoin
import com.ghn.poker.tracker.ui.App
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin {
        module {}
    }
    return ComposeUIViewController { App() }
}
