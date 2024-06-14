package com.example.melali.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun MelaliTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xffA6784C),
            onPrimary = Color(0xffFFF9F3),
            primaryContainer = Color(0xffFDC48D),
            onPrimaryContainer = Color(0xff4E351C),
            secondary = Color(0xff2CABE9),
            onSecondary = Color(0xffEBF8FF),
            secondaryContainer = Color(0xffC0EAFF),
            onSecondaryContainer = Color(0xff004161)
        ),
        typography = Typography,
        content = content
    )
}