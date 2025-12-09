package com.mevi.lasheslam.ui.profile.request

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mevi.lasheslam.ui.profile.HeaderViewRequest
import com.mevi.lasheslam.ui.profile.Section

@Composable
fun AdminRequestsScreen(navController: NavController) {

    var selectedSection by remember { mutableStateOf(Section.CURSOS) }

    Column(modifier = Modifier.fillMaxSize()) {

        HeaderViewRequest(
            navController = navController,
            selectedSection = selectedSection,
            onSelectSection = { selectedSection = it }
        )

        when (selectedSection) {
            Section.CURSOS -> AdmRequestCursesScreen()
            Section.PRODUCTOS -> {}
            Section.SERVICIOS -> {}
        }
    }
}
