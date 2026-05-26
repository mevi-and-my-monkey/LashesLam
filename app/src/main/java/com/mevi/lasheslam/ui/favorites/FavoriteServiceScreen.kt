package com.mevi.lasheslam.ui.favorites

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
import com.mevi.lasheslam.network.ServiceItem
import com.mevi.lasheslam.ui.components.views.EmptyViewScreen
import com.mevi.lasheslam.ui.favorites.service.FavoriteServiceCard

@Composable
fun FavoriteServiceScreen(
    onNavigateToServiceEdit: (String) -> Unit,
    favoriteServices: List<ServiceItem>,
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
            if (favoriteServices.isEmpty()) {
                EmptyViewScreen()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(favoriteServices) { service ->
                        FavoriteServiceCard(service = service) {
                            onNavigateToServiceEdit(service.id)
                        }
                    }
                }
            }
        }
    }
}