package com.mevi.lasheslam

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mevi.lasheslam.navigation.AppNavGraph
import com.mevi.lasheslam.ui.theme.LashesLamTheme
import com.mevi.lasheslam.ui.update.PlayCoreUpdateLauncher

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot(
    playCoreUpdateLauncher: PlayCoreUpdateLauncher,
    viewModel: AppViewModel = hiltViewModel()
) {
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    LashesLamTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.systemBars
            ) { innerPadding ->
                AppNavGraph(
                    playCoreUpdateLauncher = playCoreUpdateLauncher,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}