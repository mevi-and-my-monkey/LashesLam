package com.mevi.lasheslam.domain.usecase

import android.net.Uri
import com.mevi.lasheslam.core.results.Resource
import com.mevi.lasheslam.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserPhotoUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(imageUri: Uri): Resource<String> {
        return repository.updateProfilePhoto(imageUri)
    }
}
