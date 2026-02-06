package com.mevi.lasheslam.ui.profile.favorite

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.ui.profile.request.AdminRequestsViewModel

@Composable
fun FavoriteCoursesScreen(viewModel: AdminRequestsViewModel = hiltViewModel()) {
    val favorites = viewModel.favoriteIds

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    LazyColumn {
        items(favorites) { courseId ->
            Text(text = "Curso favorito: $courseId")
        }
    }
}