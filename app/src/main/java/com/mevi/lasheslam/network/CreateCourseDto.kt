package com.mevi.lasheslam.network

import com.mevi.lasheslam.domain.model.CreateCourseModel

data class CreateCourseDto(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val horaIncio: String = "",
    val horaFin: String = "",
    val fecha: String = "",
    val costo: String = "",
    val apartado: String = "",
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

fun CreateCourseModel.toDto(
    imageUrl: String,
    instructorImageUrl: String,
    id: String
): CreateCourseDto {
    return CreateCourseDto(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        horaIncio = horaInicio,
        horaFin = horaFin,
        fecha = fecha,
        costo = costo,
        apartado = apartado,
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