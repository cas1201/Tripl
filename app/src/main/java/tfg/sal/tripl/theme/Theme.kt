package tfg.sal.tripl.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    primaryVariant = PrimaryColoDarkrDay,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryDarkColor,
    onPrimary = PrimaryTextColor,
    onSecondary = SecondaryTextColor
)

@Composable
fun TriplTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setSystemBarsColor(color = PrimaryColorNight)
        DarkColorPalette
    } else {
        systemUiController.setSystemBarsColor(color = PrimaryColorDay)
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}