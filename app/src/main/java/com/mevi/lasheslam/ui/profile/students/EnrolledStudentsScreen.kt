package com.mevi.lasheslam.ui.profile.students

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mevi.lasheslam.network.EnrolledStudent
import com.mevi.lasheslam.ui.components.GenericLoading

@Composable
fun EnrolledStudentsScreen(
    courseId: String,
    courseName: String,
    viewModel: EnrolledViewModel = hiltViewModel()
) {
    val list by viewModel.students.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)

    LaunchedEffect(Unit) { viewModel.loadStudents(courseId) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 32.dp, horizontal = 24.dp)) {
        Text(courseName, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(list) { student -> StudentCard(student) }
        }
    }

    if (loading) GenericLoading(true)
}

@Composable
fun StudentCard(student: EnrolledStudent) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(student.nameUser, style = MaterialTheme.typography.titleMedium)
        Text(student.emailUser, style = MaterialTheme.typography.bodyMedium)
        Text("Fecha: ${student.date}")
        Text("Horario: ${student.schedule}")
    }
}
