package com.mevi.lasheslam.ui.products.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.ui.theme.CormorantGaramond
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun DetailCostProductView(
    currentPrice: Double,
    originalPrice: Double,
    modifier: Modifier = Modifier
) {
    val discount = if (originalPrice > currentPrice && originalPrice > 0) {
        (((originalPrice - currentPrice) / originalPrice) * 100).toInt()
    } else 0

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "$${currentPrice.toInt()}",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = CormorantGaramond,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 32.sp
            ),
            color = Color(0xFFD97D8C)
        )

        Spacer(Modifier.height(8.dp))

        if (originalPrice > currentPrice) {
            Text(
                text = "$${originalPrice.toInt()}",
                style = MaterialTheme.typography.titleMedium.copy(
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = 18.sp
                ),
                color = Color.LightGray
            )

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFFDF2F0))
                    .border(0.5.dp, Color(0xFFD97D8C).copy(alpha = 0.3f), RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-$discount%",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = Color(0xFFD97D8C)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailCostProductViewPreview() {
    LashesLamTheme {
        DetailCostProductView(
            currentPrice = 280.0,
            originalPrice = 340.0,
            modifier = Modifier.padding(16.dp)
        )
    }
}
