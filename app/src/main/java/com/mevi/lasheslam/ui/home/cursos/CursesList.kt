package com.mevi.lasheslam.ui.home.cursos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.home.cursos.components.AnimatedMarketplaceItem
import com.mevi.lasheslam.ui.home.cursos.components.ShimmerBox

@Composable
fun CursesList(
    services: List<ServiceItem>,
    isLoading: Boolean,
    onClick: (ServiceItem) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (isLoading) {
            items(6) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        } else {
            itemsIndexed(services) { index, service ->
                AnimatedMarketplaceItem(service, index) {
                    onClick(service)
                }
            }
        }
    }
}