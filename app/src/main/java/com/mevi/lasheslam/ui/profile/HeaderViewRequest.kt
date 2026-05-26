package com.mevi.lasheslam.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mevi.lasheslam.ui.components.views.TitleTopBar
import com.mevi.lasheslam.ui.home.HomeViewModel
import com.mevi.lasheslam.ui.request.HeaderCategoriesMenuRequest

enum class Section {
    CURSOS,
    PRODUCTOS,
    SERVICIOS
}

@Composable
fun HeaderViewRequest(
    navController: NavController,
    selectedSection: Section,
    onSelectSection: (Section) -> Unit,
    countCourses: Int = 0,
    countProducts: Int = 0,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val photoUrl by remember { derivedStateOf { viewModel.photoUrl } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF80AB), Color(0xFFFFC1E3))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {

        TitleTopBar(title = "Solicitudes", photoUrl = photoUrl, navController = navController)
    }
    Spacer(modifier = Modifier.height(12.dp))
    HeaderCategoriesMenuRequest(
        selected = selectedSection,
        onSelect = onSelectSection,
        countCourses = countCourses,
        countProducts = countProducts
    )
}
