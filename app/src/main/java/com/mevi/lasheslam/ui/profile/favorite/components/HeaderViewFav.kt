package com.mevi.lasheslam.ui.profile.favorite.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.home.components.Section

@Composable
fun HeaderViewFav(
    selectedSection: Section,
    onSelectSection: (Section) -> Unit,
    photoUrl: String?,
    popBack: () -> Unit
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
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {

        val title = "${stringResource(R.string.favorites)} - " + selectedSection.name.lowercase()
        TitleTopBarFav(title = title, photoUrl = photoUrl, popBack = popBack)

        Spacer(modifier = Modifier.height(16.dp))

        HeaderCategoriesMenuFav(
            selected = selectedSection,
            onSelect = onSelectSection
        )
    }
}