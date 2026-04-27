package com.mevi.lasheslam.ui.home.cursos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.CoursesItem
import com.mevi.lasheslam.ui.home.cursos.components.AnimatedMarketplaceItemSearch
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox

@Composable
fun CursesListSearch(
    services: List<CoursesItem>,
    isLoading: Boolean,
    onClick: (CoursesItem) -> Unit
) {
    Column {
        if (isLoading) {
            repeat(6) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        } else {
            services.forEachIndexed { index, service ->
                AnimatedMarketplaceItemSearch(service, index) {
                    onClick(service)
                }
            }
        }
    }
}