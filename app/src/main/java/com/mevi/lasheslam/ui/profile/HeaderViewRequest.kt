package com.mevi.lasheslam.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.views.TitleTopBar
import com.mevi.lasheslam.ui.home.HomeViewModel
import com.mevi.lasheslam.ui.home.components.HeaderCategoryItem

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

        // ----------- PERFIL + BOTÓN CARRITO -------------
        TitleTopBar(title = "Favoritos", photoUrl = photoUrl, navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        HeaderCategoriesMenu(
            selected = selectedSection,
            onSelect = onSelectSection
        )
    }
}

@Composable
fun HeaderCategoriesMenu(
    selected: Section,
    onSelect: (Section) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        HeaderCategoryItem(
            title = "Cursos",
            icon = R.drawable.ic_courses,
            isSelected = selected == Section.CURSOS,
            onClick = { onSelect(Section.CURSOS) }
        )

        HeaderCategoryItem(
            title = "Productos",
            icon = R.drawable.ic_products,
            isSelected = selected == Section.PRODUCTOS,
            onClick = { onSelect(Section.PRODUCTOS) }
        )

        HeaderCategoryItem(
            title = "Servicios",
            icon = R.drawable.ic_services,
            isSelected = selected == Section.SERVICIOS,
            onClick = { onSelect(Section.SERVICIOS) }
        )
    }
}
