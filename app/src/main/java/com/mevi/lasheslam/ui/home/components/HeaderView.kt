package com.mevi.lasheslam.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.components.NotificationBadge
import com.mevi.lasheslam.ui.home.HomeViewModel

enum class Section {
    CURSOS,
    PRODUCTOS,
    SERVICIOS
}

@Composable
fun HeaderView(
    navController: NavController,
    selectedSection: Section,
    onSelectSection: (Section) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val name by remember { derivedStateOf { viewModel.name } }
    val photoUrl by remember { derivedStateOf { viewModel.photoUrl } }
    val isUserInvited by viewModel.isUserInvited.collectAsState()
    val isLoading by remember { derivedStateOf { name.isEmpty() } }
    val isAdmin by SessionManager.isUserAdmin.collectAsState()

    val userId = SessionManager.currentUserId

    LaunchedEffect(isAdmin) {
        viewModel.initUserStatus(isAdmin, userId.value ?: "")
    }

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
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
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
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = if (!isUserInvited) "Bienvenido de nuevo" else "Bienvenido",
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
                        if (isAdmin) {
                            navController.navigate(Screen.Request.route)
                        } else {
                            //navController.navigate(Screen.UserCourses.route)
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Solicitudes",
                        tint = Color.White
                    )
                }

                val badgeCount =
                    if (isAdmin) viewModel.adminPendingCount
                    else viewModel.userAcceptedCount

                NotificationBadge(
                    count = badgeCount,
                    color = if (isAdmin) Color.Red else Color(0xFF2196F3)
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
                .clickable {
                    navController.navigate(Screen.Search.route)
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

@Composable
fun HeaderCategoriesMenu(
    selected: Section,
    onSelect: (Section) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        HeaderCategoryItem(
            title = "Cursos",
            icon = R.drawable.ic_courses,
            isSelected = selected == Section.CURSOS,
            onClick = { onSelect(Section.CURSOS) }
        )

        HeaderCategoryItem(
            title = "Productos",
            icon = R.drawable.ic_products,
            isSelected = selected == Section.PRODUCTOS,
            onClick = { onSelect(Section.PRODUCTOS) }
        )

        HeaderCategoryItem(
            title = "Servicios",
            icon = R.drawable.ic_services,
            isSelected = selected == Section.SERVICIOS,
            onClick = { onSelect(Section.SERVICIOS) }
        )
    }
}

@Composable
fun HeaderCategoryItem(
    title: String,
    icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Text(
            text = title,
            color = if (isSelected) Color.White else Color.Black.copy(alpha = 0.6f),
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}