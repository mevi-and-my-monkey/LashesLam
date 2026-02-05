package com.mevi.lasheslam.ui.profile.students

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mevi.lasheslam.network.EnrolledStudent
import com.mevi.lasheslam.ui.components.GenericLoading

@Composable
fun EnrolledStudentsScreen(
    navController: NavController,
    courseId: String,
    courseName: String,
    viewModel: EnrolledViewModel = hiltViewModel()
) {
    val list by viewModel.students.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    var date = list.firstOrNull()?.date ?: ""
    var schedule = list.firstOrNull()?.schedule ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadStudents(courseId)
    }

    LaunchedEffect(list) {
        date = list.firstOrNull()?.date ?: ""
        schedule = list.firstOrNull()?.schedule ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .background(Color(0xFFFDEEF0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = courseName.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Date",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(date, color = Color.Gray, fontSize = 12.sp)
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = "Time",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(schedule, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("Total inscritos: ${list.size}", color = Color.Gray, fontSize = 12.sp)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(list) { student -> StudentCard(student) }
        }
    }

    if (loading) GenericLoading(true)
}

@Composable
fun StudentCard(student: EnrolledStudent) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(student.nameUser, fontWeight = FontWeight.Bold)
                Text(student.emailUser, color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Paid",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Inscrito", color = Color(0xFF4CAF50), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {

                            val email = student.emailUser.trim()

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "message/rfc822"
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                                putExtra(Intent.EXTRA_SUBJECT, "Información del curso")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Hola, me gustaría más información sobre el curso."
                                )
                            }

                            context.startActivity(
                                Intent.createChooser(intent, "Enviar correo con")
                            )
                        }
                )
            }
        }
    }
}
