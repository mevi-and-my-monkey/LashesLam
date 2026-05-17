package com.mevi.lasheslam.ui.home.cursos

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.domain.analytics.AnalyticsEvent
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.ui.home.components.BannerView
import com.mevi.lasheslam.ui.home.cursos.components.AnimatedMarketplaceItem
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox

@Composable
fun CursosPageContent(
    onNavigateToSearch: () -> Unit,
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

        item {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                BannerView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    onNavigateToServiceDetails = onNavigateToServiceDetails
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(R.string.courses_available),
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleMedium,
                )

                TextButton(
                    onClick = {
                        trackEvent(AnalyticsEvent.IconSearchClick)
                        onNavigateToSearch()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.see_all),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

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