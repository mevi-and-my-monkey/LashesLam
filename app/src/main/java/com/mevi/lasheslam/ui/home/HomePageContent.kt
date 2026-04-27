package com.mevi.lasheslam.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.components.BottomSheetOption
import com.mevi.lasheslam.ui.components.GenericOptionsBottomSheet
import com.mevi.lasheslam.ui.home.components.HeaderView
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.home.components.ServiceAddView
import com.mevi.lasheslam.ui.home.cursos.CursosPageContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePageContent(
    state: HomePageUiState,
    onNavigateToSearch: () -> Unit,
    onNavigateToRequest: () -> Unit,
    onNavigateToServiceDetails: (String) -> Unit,
    onSelectedSection: (Section) -> Unit,
) {
    var showOptionsBottomSheet by remember { mutableStateOf(false) }
    var showAddView by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            HeaderView(
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToRequest = onNavigateToRequest,
                selectedSection = state.selectedSection,
                onSelectSection = { section ->
                    onSelectedSection(section)
                }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    when (state.selectedSection) {
                        Section.CURSOS -> {
                            CursosPageContent(
                                onNavigateToSearch = onNavigateToSearch,
                                onNavigateToServiceDetails = onNavigateToServiceDetails,
                                services = state.courses,
                                isLoading = state.isLoading
                            )
                        }

                        else -> {}
                    }
                }
            }
        }

        if (state.isAdmin) {
            FloatingActionButton(
                onClick = {
                    showOptionsBottomSheet = true
                },
                containerColor = Color(0xFFFF80AB),
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                    tint = Color.White
                )
            }
        }

        if (showOptionsBottomSheet) {
            GenericOptionsBottomSheet(
                title = stringResource(R.string.manege_products_and_services),
                onDismiss = { showOptionsBottomSheet = false },
                options = listOf(
                    BottomSheetOption(
                        label = stringResource(R.string.uploaded_new_product),
                        icon = Icons.Default.AddBusiness
                    ) {

                    },
                    BottomSheetOption(
                        label = stringResource(R.string.uploaded_new_product),
                        icon = Icons.Default.PostAdd
                    ) {
                        showOptionsBottomSheet = false
                        showAddView = true
                    }
                )
            )
        }

        if (showAddView) {
            ServiceAddView(
                linkedBannerIndex = 99,
                onDismiss = { showAddView = false })
        }
    }
}

