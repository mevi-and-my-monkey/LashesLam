package com.mevi.lasheslam.ui.courses.edit.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.mevi.lasheslam.R

@Composable
fun EditInstructView(
    instructora: String,
    onInstructorChange: (String) -> Unit,
    instructoraDesc: String,
    onInstructorDescChange: (String) -> Unit,
    imageUriInstr: Uri?,
    instructoraImage: String,
    onInstructorImageChange: (Uri?) -> Unit
) {
    val pickImageLauncherInst = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> onInstructorImageChange(uri) }

    OutlinedTextField(
        value = instructora,
        onValueChange = { onInstructorChange(it) },
        label = { Text(stringResource(R.string.instructora)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(12.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = instructoraDesc,
        onValueChange = { onInstructorDescChange(it) },
        label = { Text(stringResource(R.string.instructoraDesc)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        shape = RoundedCornerShape(12.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { pickImageLauncherInst.launch("image/*") },
        contentAlignment = Alignment.Center
    ) {
        when {
            imageUriInstr != null -> SubcomposeAsyncImage(
                model = imageUriInstr,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            instructoraImage.isNotEmpty() -> SubcomposeAsyncImage(
                model = instructoraImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = stringResource(R.string.camera),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.add_image_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.image_details),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}