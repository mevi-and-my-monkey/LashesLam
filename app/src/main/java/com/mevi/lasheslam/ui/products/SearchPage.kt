package com.mevi.lasheslam.ui.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.ui.home.components.HeaderCategoriesMenu
import com.mevi.lasheslam.ui.home.cursos.CursesList
import com.mevi.lasheslam.ui.home.cursos.CursesListSearch
import com.mevi.lasheslam.ui.products.search.SearchViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchPage(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val focusRequester = remember { FocusRequester() }

    val query = viewModel.searchQuery
    val selectedSection = viewModel.selectedSection
    val filteredItems = viewModel.filteredItems
    val isLoading = viewModel.isLoading

    Column(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = true,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFF80AB), Color(0xFFFFC1E3))
                        ),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.Black
                        )
                    }

                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.onSearchChanged(it) },
                        placeholder = {
                            Text("Buscar productos...", color = Color.Black.copy(alpha = 0.8f))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color.Black
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedLeadingIconColor = Color.Black,
                            unfocusedLeadingIconColor = Color.Black.copy(alpha = 0.8f),
                            focusedPlaceholderColor = Color.Black.copy(alpha = 0.8f),
                            unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.8f)
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                HeaderCategoriesMenu(
                    selected = selectedSection,
                    onSelect = { viewModel.onSectionChanged(it) }
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(200)
            focusRequester.requestFocus()
        }

        CursesListSearch(
            services = filteredItems,
            isLoading = isLoading
        ) { item ->
            navController.navigate(
                Screen.ServiceDetails.createRoute(item.id)
            )
        }
    }
}