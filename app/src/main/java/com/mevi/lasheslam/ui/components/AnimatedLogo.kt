package com.mevi.lasheslam.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.core.Strings
import com.mevi.lasheslam.ui.theme.LashesLamTheme

/**
 * Funcion que muestra el logo de la aplicacion con una animacion de fadeIn
 * **/
@Composable
fun AnimatedLogo() {
    var visible by remember { mutableStateOf(false) }
    // Se ejecuta cuando se crea el composable, solo una vez
    LaunchedEffect(Unit) {
        visible = true
    }
    //animacion para mostrar el logo
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1200))
    ) {
        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.logo_lashes_dark else R.drawable.logo_lashes_),
            contentDescription = Strings.logoContentDescription,
            modifier = Modifier
                .size(120.dp)
        )
    }
}

/**
 * Usado para previsualizar el logo en el login
 * **/
@Preview(showBackground = true, name = "LoginLogo")
@Composable
fun LoginLogoPreview() {
    LashesLamTheme {
        AnimatedLogo()
    }
}