package com.mevi.lasheslam.ui.products.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.utils.Utilities

@Composable
fun DetailsSocialMediaCourView(
    onOpenFacebook: (String) -> Unit,
    onOpenInstagram: (String) -> Unit,
    onOpenWhatsApp: (String) -> Unit,
    facebook: String,
    instagram: String,
    whatsApp: String,
    titulo: String,
    precio: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            IconButton(
                onClick = {
                    onOpenFacebook(facebook)
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_facebook),
                    contentDescription = stringResource(R.string.facebook),
                    tint = Color.Unspecified
                )
            }

            IconButton(
                onClick = {
                    onOpenInstagram(instagram)
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_instagram),
                    contentDescription = stringResource(R.string.instragram),
                    tint = Color.Unspecified
                )
            }

            IconButton(
                onClick = {
                    onOpenWhatsApp(
                        Utilities.createProductMessageWhatsApp(
                            titulo = titulo,
                            precio = precio,
                            whatsapp = whatsApp
                        )
                    )
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_whatsapp),
                    contentDescription = stringResource(R.string.whatsApp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}