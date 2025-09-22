package com.project.service

import com.project.entity.Image
import com.project.repository.ImageRepository
import org.springframework.stereotype.Service

@Service
class ImageService(
    private val repository: ImageRepository
) {
    fun getAllImages(): List<Image> = repository.findAll()

    fun getImageById(id: Long): Image? = repository.findById(id)

    fun getImagesByProductId(productId: Long): List<Image> = repository.findByProductId(productId)

    fun addImage(image: Image) = repository.save(image)

    fun updateImage(image: Image) = repository.update(image)

    fun deleteImage(id: Long) = repository.deleteById(id)
}
