package tfg.sal.tripl.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import tfg.sal.tripl.appcontent.profile.profileoptions.settings.ui.SettingsViewModel
import java.util.*

private val DarkColorPalette = darkColors(
    primary = PrimaryColorNight,
    primaryVariant = PrimaryColorLightNight,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryDarkColor,
    onPrimary = PrimaryTextColor,
    onSecondary = SecondaryTextColor
)

private val LightColorPalette = lightColors(
    primary = PrimaryColorDay,
    primaryVariant = PrimaryColoDarkDay,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryDarkColor,
    onPrimary = PrimaryTextColor,
    onSecondary = SecondaryTextColor
)

@Composable
fun TriplTheme(
    settingsViewModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val darkMode: String by settingsViewModel.darkMode.observeAsState(initial = "")
    val darkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    settingsViewModel.setSwitch(darkTheme)

    val colors: Colors
    when (darkMode) {
        "enabled" -> {
            systemUiController.setSystemBarsColor(color = PrimaryColorNight)
            colors = DarkColorPalette
        }
        "disabled" -> {
            systemUiController.setSystemBarsColor(color = PrimaryColorDay)
            colors = LightColorPalette
        }
        else -> {
            colors = if (darkTheme) {
                systemUiController.setSystemBarsColor(color = PrimaryColorNight)
                DarkColorPalette
            } else {
                systemUiController.setSystemBarsColor(color = PrimaryColorDay)
                LightColorPalette
            }
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}