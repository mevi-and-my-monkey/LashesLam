package com.mevi.lasheslam.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mevi.lasheslam.ui.auth.LoginViewModel
import com.mevi.lasheslam.ui.components.FloatingBottomNavigation
import com.mevi.lasheslam.ui.components.GenericLoading
import com.mevi.lasheslam.ui.profile.ProfilePage
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val isLoading : Boolean by loginViewModel.isLoading.observeAsState(initial = false)

    BackHandler {
        if (selectedIndex != 0) {
            selectedIndex = 0
        } else {
            //showExitDialog = true
        }
    }
    Scaffold(
        bottomBar = {
            FloatingBottomNavigation(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        }
    ) { paddingValues ->
        // Contenido de la pantalla
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {


            when (selectedIndex) {
                0 -> HomePage(navController, modifier.padding(horizontal = 8.dp))
                1 -> Text("Favoritos")
                2 -> Text("Pedidos")
                //3 -> Text("Notificaciones")
                4 -> ProfilePage(navController, modifier.padding(horizontal = 8.dp))
            }

            GenericLoading(
                isLoading = isLoading,
                message = "Procesando, por favor espera..."
            )
        }
    }
}

@Composable
@Preview(
    //uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_DESK
)
fun PreviewHome() {
    LashesLamTheme {
        HomeScreen(navController = NavHostController(LocalContext.current), modifier = Modifier)
    }
}