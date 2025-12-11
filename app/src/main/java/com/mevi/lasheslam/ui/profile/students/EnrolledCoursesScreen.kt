package com.mevi.lasheslam.ui.profile.students

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavController
import com.mevi.lasheslam.navigation.Screen
import com.mevi.lasheslam.network.EnrolledCourse
import com.mevi.lasheslam.ui.components.GenericLoading

@Composable
fun EnrolledCoursesScreen(
    navController: NavController,
    viewModel: EnrolledViewModel = hiltViewModel()
) {
    val list by viewModel.courses.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)

    LaunchedEffect(Unit) { viewModel.loadCourses() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(list) { course ->
                CourseCard(course = course) {
                    navController.navigate(
                        Screen.CourseInscritos.createRoute(
                            courseId = course.courseId,
                            courseName = course.courseName
                        )
                    )
                }
            }
        }

        if (loading) GenericLoading(true)
    }
}

@Composable
fun CourseCard(course: EnrolledCourse, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color(0xFFD97D8C), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Column {
            Text(
                course.courseName,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(6.dp))
            Text("Fecha: ${course.fecha}", color = Color.White)
            Spacer(Modifier.height(6.dp))
            Text("Inscritos: ${course.inscritosCount}", color = Color.White)
        }
    }
}