package com.project.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.project.dto.ImageResponse
import com.project.dto.ProductResponse
import com.project.dto.VariantResponse
import com.project.entity.Image
import com.project.entity.Product
import com.project.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val imageService: ImageService,
    private val objectMapper: ObjectMapper,
) {

    @Transactional
    fun addProductWithImage(product: Product, images: List<String>?): Long {
        val productId = productRepository.saveProduct(product)

        images
            ?.filter { it.isNotBlank() }
            ?.forEach { img ->
                val image = Image(
                    productId = productId,
                    src = img.trim()
                )
                imageService.addImage(image)
            }

        return productId
    }

    fun getProductWithImage(productId: Long): ProductResponse? {
        val product = productRepository.findById(productId)

        // Deserialize variantsJson to list of VariantResponse
        val variants: List<VariantResponse> = product?.variantsJson?.let {
            objectMapper.readValue(it, object : TypeReference<List<VariantResponse>>() {})
        } ?: emptyList()

        val images: List<ImageResponse> = imageService.getImagesByProductId(productId)
            .map { image ->
                ImageResponse(
                    src = image.src
                )
            }


        if (product != null) {
            return ProductResponse(
                id = product.id!!,
                title = product.title,
                vendor = product.vendor,
                handle = product.handle,
                images = images,
                variants = variants
            )
        }

       return null
    }


    fun getAllProducts(): List<Product> =
        productRepository.findAll()

    fun countProducts(): Long =
        productRepository.countTotalProduct()

    @Transactional
    fun addProduct(product: Product): Long =
        productRepository.saveProduct(product)

    fun findById(id: Long): Product? =
        productRepository.findById(id)


    fun updateProduct(id: Long, updated: Product): Int =
        productRepository.updateById(id,updated)


    fun deleteById(id: Long): Int {
        imageService.deleteImagesByProductId(id)
        return productRepository.deleteById(id)
    }



    fun getProductsPaged(limit: Int, offset: Int): List<Product> =
        productRepository.findPagedProduct(limit, offset)

    fun getTotalProducts(): Int =
        productRepository.getTotalProducts()

    fun getTotalProducts(search: String): Int {
        return productRepository.countSearchedProduct(search)
    }

    fun getProductsPaged(limit: Int, offset: Int, search: String): List<Product> {
        return productRepository.findPaged(limit, offset, search)
    }

}