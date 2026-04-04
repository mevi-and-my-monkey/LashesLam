package com.mevi.lasheslam.ui.components.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.R

@Composable
fun EmptyViewScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFC1E3).copy(alpha = 0.22f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_empty_drawable),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.empty_view_title),
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.empty_view_content),
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
        )


        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyViewScreenPreview() {
    EmptyViewScreen()
}