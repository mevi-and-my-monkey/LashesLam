package com.mevi.lasheslam.ui.profile.favorite.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mevi.lasheslam.R
import com.mevi.lasheslam.ui.home.components.Section
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun HeaderCategoriesMenuFav(
    selected: Section,
    onSelect: (Section) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryPill(
            title = stringResource(R.string.courses),
            isSelected = selected == Section.CURSOS,
            onClick = { onSelect(Section.CURSOS) }
        )

        CategoryPill(
            title = stringResource(R.string.products),
            isSelected = selected == Section.PRODUCTOS,
            onClick = { onSelect(Section.PRODUCTOS) }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HeaderCategoriesMenuFavPreview() {
    LashesLamTheme {
        HeaderCategoriesMenuFav(
            selected = Section.CURSOS,
            onSelect = {}
        )
    }
}
