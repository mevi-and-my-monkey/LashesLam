package com.mevi.lasheslam.network

import com.mevi.lasheslam.domain.model.UpdateCourseModel

data class UpdateCourseDto(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val horaIncio: String = "",
    val horaFin: String = "",
    val fecha: String = "",
    val costo: String = "",
    val apartar: String = "",
    val instructora: String = "",
    val instructoraDesc: String = "",
    val temarios: List<String> = emptyList(),
    val imagen: String = "",
    val instructoraImage: String = "",
    val ubicacionNombre: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val banner: Int = 0
)

fun UpdateCourseModel.toDto(
    imageUrl: String,
    instructorImageUrl: String,
    id: String
): UpdateCourseDto {
    return UpdateCourseDto(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        horaIncio = horaInicio,
        horaFin = horaFin,
        fecha = fecha,
        costo = costo,
        apartar = apartar,
        instructora = instructora,
        instructoraDesc = instructoraDesc,
        temarios = temarios,
        imagen = imageUrl,
        instructoraImage = instructorImageUrl,
        ubicacionNombre = ubicacionNombre,
        lat = lat,
        lng = lng,
        banner = banner
    )
}