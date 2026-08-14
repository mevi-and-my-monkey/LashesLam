package com.mevi.lasheslam.domain.model

/** Estado de una solicitud de curso. */
enum class CourseRequestStatus(val value: String) {
    PENDING("pendiente"),
    ACCEPTED("aceptado");
}
