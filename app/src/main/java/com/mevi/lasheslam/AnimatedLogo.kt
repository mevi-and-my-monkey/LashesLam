package com.mevi.lasheslam

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun AnimatedLogo() {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1200))
    ) {
        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.logo_lashes_dark else R.drawable.logo_lashes),
            contentDescription = Strings.logoContentDescription,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
    }
}

@Preview(showBackground = true, name = "LoginLogo")
@Composable
fun LoginLogoPreview() {
    LashesLamTheme {
        AnimatedLogo()
    }
}