package com.mevi.lasheslam.ui.profile.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.domain.model.CoursesItem
import com.mevi.lasheslam.ui.components.views.EmptyViewScreen
import com.mevi.lasheslam.ui.favorites.courses.FavoriteCourseCard

@Composable
fun FavoriteCoursesScreen(
    onNavigateToCourseDetails: (String) -> Unit,
    favoriteCourses: List<CoursesItem>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            if (favoriteCourses.isEmpty()) {
                EmptyViewScreen()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favoriteCourses) { course ->
                        FavoriteCourseCard(course = course) {
                            onNavigateToCourseDetails(course.id)
                        }
                    }
                }
            }
        }
    }
}
