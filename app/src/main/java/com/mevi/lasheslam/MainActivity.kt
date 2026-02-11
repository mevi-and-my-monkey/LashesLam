package com.mevi.lasheslam

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.navigation.AppNavGraph
import com.mevi.lasheslam.ui.auth.LoginViewModel
import com.mevi.lasheslam.ui.theme.LashesLamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun AppContent() {
        val loginViewModel: LoginViewModel = hiltViewModel()
        val isDarkMode by loginViewModel.isDarkMode.collectAsState()

        LashesLamTheme(darkTheme = isDarkMode) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold { innerPadding ->
                    AppNavGraph(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}