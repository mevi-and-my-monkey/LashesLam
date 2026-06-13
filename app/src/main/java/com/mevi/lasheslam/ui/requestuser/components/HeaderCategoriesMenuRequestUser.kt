package com.mevi.lasheslam.ui.requestuser.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.request.CategoryBadgePill


@Composable
fun HeaderCategoriesMenuRequestUser(
    selected: Section,
    onSelect: (Section) -> Unit,
    countCourses: Int = 0,
    countProducts: Int = 0,
    countServices: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        CategoryBadgePill(
            title = stringResource(R.string.courses),
            count = countCourses,
            isSelected = selected == Section.CURSOS,
            onClick = { onSelect(Section.CURSOS) }
        )

        CategoryBadgePill(
            title = stringResource(R.string.products),
            count = countProducts,
            isSelected = selected == Section.PRODUCTOS,
            onClick = { onSelect(Section.PRODUCTOS) }
        )

        CategoryBadgePill(
            title = stringResource(R.string.services),
            count = countServices,
            isSelected = selected == Section.SERVICIOS,
            onClick = { onSelect(Section.SERVICIOS) }
        )
    }
}