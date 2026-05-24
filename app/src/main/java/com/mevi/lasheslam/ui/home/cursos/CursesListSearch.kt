package com.mevi.lasheslam.ui.home.cursos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.ui.home.cursos.components.AnimatedMarketplaceItem
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox

@Composable
fun CursesListSearch(
    onNavigateToServiceDetails: (String) -> Unit,
    courses: List<CoursesItem>,
    isLoading: Boolean,
    trackEvent: (AnalyticsEvent) -> Unit,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        if (isLoading) {

            items(6) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

        } else {

            itemsIndexed(
                items = courses,
                key = { _, course -> course.id }
            ) { index, course ->

                AnimatedMarketplaceItem(
                    trackEvent = trackEvent,
                    courses = course,
                    index = index,
                    favorites = favorites,
                    onToggleFavorite = onToggleFavorite
                ) {
                    onNavigateToServiceDetails(course.id)
                }
            }
        }
    }
}