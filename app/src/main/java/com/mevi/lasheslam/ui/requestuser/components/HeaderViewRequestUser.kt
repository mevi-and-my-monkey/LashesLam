package com.mevi.lasheslam.ui.requestuser.components

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
import com.mevi.lasheslam.ui.components.views.TitleTopBar
import com.mevi.lasheslam.ui.home.components.Section

@Composable
fun HeaderViewRequestUser(
    popBack: () -> Unit,
    selectedSection: Section,
    onSelectSection: (Section) -> Unit,
    countCourses: Int = 0,
    countProducts: Int = 0,
    countServices: Int = 0,
    photoUrl: String = "",
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

        TitleTopBar(
            title = stringResource(R.string.requests),
            photoUrl = photoUrl,
            popBack = popBack
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    HeaderCategoriesMenuRequestUser(
        selected = selectedSection,
        onSelect = onSelectSection,
        countCourses = countCourses,
        countProducts = countProducts,
        countServices = countServices
    )
}