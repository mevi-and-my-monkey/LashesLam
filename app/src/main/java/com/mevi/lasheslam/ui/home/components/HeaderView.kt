package com.mevi.lasheslam.ui.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.NotificationBadge
import com.mevi.lasheslam.ui.home.HomePageViewModel
import com.mevi.lasheslam.ui.home.HomeViewModel

enum class Section {
    CURSOS,
    PRODUCTOS,
    SERVICIOS
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeaderView(
    onNavigateToSearch: () -> Unit,
    onNavigateToRequest: () -> Unit,
    selectedSection: Section,
    onSelectSection: (Section) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    viewModelHP: HomePageViewModel = hiltViewModel()
) {
    val uiState by viewModelHP.uiState.collectAsState()

    val name by remember { derivedStateOf { viewModel.name } }
    val photoUrl by remember { derivedStateOf { viewModel.photoUrl } }
    val isLoading by remember { derivedStateOf { name.isEmpty() } }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF80AB), Color(0xFFFFC1E3))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {

        // ----------- PERFIL + BOTÓN CARRITO -------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photoUrl)
                        .crossfade(true)
                        .error(R.drawable.ic_guest)
                        .placeholder(R.drawable.ic_guest)
                        .build(),
                    contentDescription = stringResource(R.string.profile_picture),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = stringResource(R.string.welcome),
                        color = Color.Black.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    )
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = Color.White.copy(alpha = 0.4f)
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
            ) {

                IconButton(
                    onClick = {
                        if (uiState.isAdmin) {
                            onNavigateToRequest()
                        } else {
                            //navController.navigate(Screen.UserCourses.route)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp)
                        )

                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = stringResource(R.string.requests),
                        tint = Color.Black,
                    )
                }

                val badgeCount =
                    if (uiState.isAdmin) uiState.adminPendingCount else viewModel.userAcceptedCount

                NotificationBadge(
                    count = badgeCount,
                    color = if (uiState.isAdmin) Color.Red else Color(0xFF2196F3)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ----------- BUSCADOR ------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    onNavigateToSearch()
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Buscar...",
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ----------- MENÚ DE SECCIONES --------------------
        HeaderCategoriesMenu(
            selected = selectedSection,
            onSelect = onSelectSection
        )
    }
}