package com.mevi.lasheslam.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mevi.lasheslam.R
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.network.UserModel
import com.mevi.lasheslam.session.SessionManager
import com.mevi.lasheslam.ui.theme.LashesLamTheme

@Composable
fun ProfilePage(navController: NavController, modifier: Modifier = Modifier) {
    val isAdmin by SessionManager.isUserAdmin.collectAsState()
    val isInvited by SessionManager.isUserInvited.collectAsState()

    if (!isInvited) {

        val context = LocalContext.current
        val userModel = remember { mutableStateOf(UserModel()) }
        val addressInput = remember { mutableStateOf(userModel.value.address) }
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val photoUrl = firebaseUser?.photoUrl?.toString()

        LaunchedEffect(key1 = Unit) {
            Firebase.firestore.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val result = it.result.toObject(UserModel::class.java)
                        if (result != null) {
                            userModel.value = result
                            addressInput.value = userModel.value.address
                        }
                    }
                }
        }

        fun updateAddress() {
            if (addressInput.value?.isNotEmpty() == true) {
                FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                    .update("address", addressInput.value)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Dirección actualizada correctamente",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(context, "La dirección no puede estar vacía", Toast.LENGTH_LONG)
                    .show()
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Tu perfil",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(20.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photoUrl)
                    .crossfade(true)
                    .error(R.drawable.ic_guest)
                    .placeholder(R.drawable.ic_guest)
                    .build(),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(148.dp)
                    .clip(CircleShape)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = userModel.value.name ?: "Usuario",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary,
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = addressInput.value ?: "",
                onValueChange = { addressInput.value = it },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { updateAddress() }),
                trailingIcon = {
                    IconButton(onClick = { updateAddress() }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Guardar dirección"
                        )
                    }
                },
                label = { Text("Dirección") }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Correo electronico",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = userModel.value.email ?: "",
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Spacer(modifier = Modifier.height(48.dp))
            TextButton(
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Cerrar sesion", fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Tu perfil",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
            )

            Button(onClick = {
                Firebase.auth.signOut()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Text(text = "Inicia sesion")
            }
        }
    }

}

@Composable
@Preview(
    //uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_DESK
)
fun PreviewProfile() {
    LashesLamTheme {
        ProfilePage(navController = NavHostController(LocalContext.current), modifier = Modifier)
    }
}