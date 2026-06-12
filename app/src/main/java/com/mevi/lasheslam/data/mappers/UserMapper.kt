package com.mevi.lasheslam.data.mappers

import com.mevi.lasheslam.data.dto.UserDto
import com.mevi.lasheslam.network.UserModel

fun UserDto.toDomain() = UserModel(
    name = name.orEmpty(),
    email = email.orEmpty(),
    uid = uid.orEmpty(),
    phone = phone.orEmpty(),
    address = address.orEmpty(),
    userPhoto = userPhoto.orEmpty(),
    photoUpdatedByUser = photoUpdatedByUser
)

fun UserModel.toDto() = UserDto(
    name = name,
    email = email,
    uid = uid,
    phone = phone,
    address = address,
    userPhoto = userPhoto,
    photoUpdatedByUser = photoUpdatedByUser
)