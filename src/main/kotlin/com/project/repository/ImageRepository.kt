package com.project.repository

import com.project.entity.Image
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ImageRepository(
    private val jdbcClient: JdbcClient
) {
    fun findAll(): List<Image> =
        jdbcClient.sql("SELECT id, product_id, src FROM images")
            .query(Image::class.java)
            .list()

    fun findById(id: Long): Image? =
        jdbcClient.sql("SELECT id, product_id, src FROM images WHERE id = :id")
            .param("id", id)
            .query(Image::class.java)
            .optional()
            .orElse(null)

    fun findByProductId(productId: Long): List<Image> =
        jdbcClient.sql("SELECT id, product_id, src FROM images WHERE product_id = :productId")
            .param("productId", productId)
            .query(Image::class.java)
            .list()

    fun save(image: Image): Int =
        jdbcClient.sql("INSERT INTO images (product_id, src) VALUES (:productId, :src)")
            .param("productId", image.productId)
            .param("src", image.src)
            .update()

    fun update(image: Image): Int =
        jdbcClient.sql("UPDATE images SET product_id = :productId, src = :src WHERE id = :id")
            .param("productId", image.productId)
            .param("src", image.src)
            .param("id", image.id)
            .update()

    fun deleteById(id: Long): Int =
        jdbcClient.sql("DELETE FROM images WHERE id = :id")
            .param("id", id)
            .update()
}