package com.project.service

import com.project.entity.Image
import com.project.repository.ImageRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExtendWith(MockitoExtension::class)
class ImageServiceTest {

    private lateinit var imageRepository: ImageRepository
    private lateinit var imageService: ImageService

    @BeforeEach
    fun setUp() {
        imageRepository = mock()
        imageService = ImageService(imageRepository)
    }

    @Test
    fun `getImagesByProductId should return images for a given productId`() {

        val productId = 1L
        val imageList = listOf(
            Image(1L, productId, "image1.jpg"),
            Image(2L, productId, "image2.jpg")
        )
        whenever(imageRepository.findByProductId(productId)).thenReturn(imageList)

        val result = imageService.getImagesByProductId(productId)

        assertEquals(imageList, result)
        verify(imageRepository).findByProductId(productId)
    }

    @Test
    fun `deleteImagesByProductId should delete images for a given productId`() {

        val productId = 1L
        val deletedCount = 2
        whenever(imageRepository.deleteImagesByProductId(productId)).thenReturn(deletedCount)

        val result = imageService.deleteImagesByProductId(productId)

        assertEquals(deletedCount, result)
        verify(imageRepository).deleteImagesByProductId(productId)
    }
}
