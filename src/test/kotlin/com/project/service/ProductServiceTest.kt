package com.project.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.entity.Product
import com.project.repository.ProductRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var imageService: ImageService
    private lateinit var objectMapper: ObjectMapper
    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        productRepository = mock()
        imageService = mock()
        objectMapper = ObjectMapper()
        productService = ProductService(productRepository, imageService, objectMapper)
    }

    @Test
    fun `addProductWithImage should save product and images`() {
        // Given
        val product = Product(id = null, title = "Test Product", vendor = "Test Vendor", handle = "test-handle", variantsJson = null)
        val images = listOf(" img1.png ", "", "img2.png")

        whenever(productRepository.saveProduct(any())).thenReturn(1L)

        val productId = productService.addProductWithImage(product, images)

        assertEquals(1L, productId)

        verify(productRepository).saveProduct(product)
        verify(imageService, times(2)).addImage(check {
            assertEquals(1L, it.productId)
            assertTrue(it.src.trim().isNotEmpty())
        })
    }


    @Test
    fun `getProductWithImage should return null if product not found`() {
        val productId = 99L
        whenever(productRepository.findById(productId)).thenReturn(null)

        val result = productService.getProductWithImage(productId)

        assertNull(result)
    }

    @Test
    fun `deleteById should delete product and images`() {

        val productId = 1L

        whenever(productRepository.deleteById(productId)).thenReturn(1)

        val result = productService.deleteById(productId)

        assertEquals(1, result)

        verify(imageService).deleteImagesByProductId(productId)

        verify(productRepository).deleteById(productId)
    }

    @Test
    fun `countProducts should return correct total product count`() {

        val expectedCount = 50L

        whenever(productRepository.countTotalProduct()).thenReturn(expectedCount)

        val result = productService.countProducts()

        assertEquals(expectedCount, result)
        verify(productRepository).countTotalProduct()
    }

}