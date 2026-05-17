package com.mevi.lasheslam.ui.courses.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R

@Composable
fun DetailsOptionsTop(
    isAdmin: Boolean,
    isFavorite: Boolean,
    id: String,
    onEditClick: (String) -> Unit,
    showConfirmDelete: () -> Unit,
    onDismiss: () -> Unit,
    toggleFavorite: (String) -> Unit,
    ) {
    if (isAdmin) {
        IconButton(
            onClick = {
                onEditClick(id)
            },
            modifier = Modifier
                .size(44.dp)
                .shadow(4.dp, CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
        ) {
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        IconButton(
            onClick = showConfirmDelete,
            modifier = Modifier
                .size(44.dp)
                .shadow(4.dp, CircleShape)
                .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }

    IconButton(
        onClick = {
            toggleFavorite(id)
        },
        modifier = Modifier
            .size(44.dp)
            .shadow(4.dp, CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = "Favorito",
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

    IconButton(
        onClick = onDismiss,
        modifier = Modifier
            .size(44.dp)
            .shadow(4.dp, CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
    ) {
        Icon(Icons.Default.Close, contentDescription = "Cerrar")
    }

}