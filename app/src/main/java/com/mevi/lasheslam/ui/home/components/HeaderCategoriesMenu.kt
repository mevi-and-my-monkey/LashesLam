package com.mevi.lasheslam.ui.home.components

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
            title = stringResource(R.string.courses),
            icon = R.drawable.ic_courses,
            isSelected = selected == Section.CURSOS,
            onClick = { onSelect(Section.CURSOS) }
        )

        HeaderCategoryItem(
            title = stringResource(R.string.products),
            icon = R.drawable.ic_products,
            isSelected = selected == Section.PRODUCTOS,
            onClick = { onSelect(Section.PRODUCTOS) }
        )

        HeaderCategoryItem(
            title = stringResource(R.string.services),
            icon = R.drawable.ic_services,
            isSelected = selected == Section.SERVICIOS,
            onClick = { onSelect(Section.SERVICIOS) }
        )
    }
}