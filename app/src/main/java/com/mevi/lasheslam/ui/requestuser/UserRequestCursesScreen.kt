package com.mevi.lasheslam.ui.requestuser

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.mevi.lasheslam.network.CourseRequest
import com.mevi.lasheslam.ui.requestuser.components.RequestUserCurseItem

@Composable
fun UserRequestCursesScreen(
    onNavigateToCourseDetails: (String) -> Unit,
    requestUserCourses: List<CourseRequest>
) {
    LazyColumn {
        items(items = requestUserCourses) { request ->
            RequestUserCurseItem(request, onNavigateToCourseDetails = onNavigateToCourseDetails)
        }
    }
}