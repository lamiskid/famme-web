package com.project.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.dto.ProductResponse
import com.project.entity.Image
import com.project.entity.Product
import com.project.service.ImageService
import com.project.service.ProductService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class ProductFetcher(
    private val objectMapper: ObjectMapper,
    private val productService: ProductService,
    private val imageService: ImageService
) {

    private val client = WebClient.create("https://famme.no")
    private val limit = 50

    @Scheduled(initialDelay = 0, fixedDelayString = "PT24H")
    fun fetchAndSaveProducts() {
        val existing = productService.countProducts()
        if (existing >= limit) {
            println("Already have $existing products (>= $limit). Skipping fetch.")
            return
        }

        val result = client.get()
            .uri("/products.json?limit=$limit")
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val rawProducts = (result?.get("products") as? List<Map<String, Any>>) ?: emptyList()
        rawProducts.take(limit).forEach { productMap ->

            val apiProduct = objectMapper.convertValue(productMap, ProductResponse::class.java)

            // variants JSON stored as-is in JSONB column
            val variantsJson = objectMapper.writeValueAsString(apiProduct.variants)

            val product = Product(
                title = apiProduct.title,
                vendor = apiProduct.vendor,
                handle = apiProduct.handle,
                variantsJson = variantsJson
            )

            val productId = productService.addProduct(product)


            apiProduct.images.forEach { img ->
                val image = Image(
                    productId = productId,
                    src = img.src
                )
                imageService.addImage(image)
            }
        }
    }
}


