package com.project.service

import com.project.entity.Image
import com.project.entity.Product
import com.project.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val imageService: ImageService
) {

    fun getAllProducts(): List<Product> =
        productRepository.findAll()

    fun countProducts(): Long =
        productRepository.countTotalProduct()

    @Transactional
    fun addProduct(product: Product): Long =
        productRepository.saveProduct(product)


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