package com.mevi.lasheslam.data.dto

import com.mevi.lasheslam.domain.model.CourseDetail
import com.mevi.lasheslam.domain.model.CreateCourseModel

data class CreateCourseDto(
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

fun CreateCourseDto.toDetail(): CourseDetail = CourseDetail(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    horaIncio = horaIncio,
    horaFin = horaFin,
    fecha = fecha,
    costo = costo,
    apartar = apartar,
    instructora = instructora,
    instructoraDesc = instructoraDesc,
    temarios = temarios,
    imagen = imagen,
    instructoraImage = instructoraImage,
    ubicacionNombre = ubicacionNombre,
    lat = lat,
    lng = lng,
    banner = banner,
)