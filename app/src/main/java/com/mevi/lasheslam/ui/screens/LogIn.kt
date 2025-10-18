package com.mevi.lasheslam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mevi.lasheslam.PinkBackground
import com.mevi.lasheslam.R
import com.mevi.lasheslam.WavyBackground

@Composable
fun LogIn() {
    WavyBackground {
        // Contenido dentro de la ola
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo dentro de la ola
            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "Logo LashesLam",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(180.dp))

            Text(
                "¡Bienvenida!",
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp),
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                onClick = { /* Acción 1 */ },
                colors = ButtonDefaults.buttonColors(containerColor = PinkBackground)
            ) {
                Text("Iniciar sesión", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                onClick = { /* Acción 2 */ }) {
                Text("Registrarse", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(100.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    thickness = DividerDefaults.Thickness, color = Color.LightGray
                )

                Text(
                    text = "  Continuar con  ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    thickness = DividerDefaults.Thickness, color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { /* Acción iniciar con Google */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_one),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Google",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login")
@Composable
fun LoginPreview() {
    LogIn()
}