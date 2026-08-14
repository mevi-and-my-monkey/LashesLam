package com.mevi.lasheslam.ui.profile.students

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.usecase.GetEnrolledCoursesUseCase
import com.mevi.lasheslam.domain.usecase.GetEnrolledStudentsUseCase
import com.mevi.lasheslam.domain.model.EnrolledCourse
import com.mevi.lasheslam.domain.model.EnrolledStudent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnrolledViewModel @Inject constructor(
    private val getEnrolledCoursesUseCase: GetEnrolledCoursesUseCase,
    private val getEnrolledStudentsUseCase: GetEnrolledStudentsUseCase
) : ViewModel() {

    private val _courses = MutableLiveData<List<EnrolledCourse>>()
    val courses: LiveData<List<EnrolledCourse>> = _courses

    private val _students = MutableLiveData<List<EnrolledStudent>>()
    val students: LiveData<List<EnrolledStudent>> = _students

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun loadCourses() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = getEnrolledCoursesUseCase()) {
                is Resource.Success -> _courses.value = result.data
                else -> {}
            }
            _loading.value = false
        }
    }

    fun loadStudents(courseId: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = getEnrolledStudentsUseCase(courseId)) {
                is Resource.Success -> _students.value = result.data
                else -> {}
            }
            _loading.value = false
        }
    }
}
