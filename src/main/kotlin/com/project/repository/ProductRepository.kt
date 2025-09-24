package com.project.repository


import com.project.entity.Product

import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jdbcClient: JdbcClient,
) {

    fun findAll(): List<Product> {
        return jdbcClient.sql(
            """
            SELECT id, title, vendor, handle, variants_json 
            FROM products ORDER BY id
            """
        ).query { rs, _ ->
            Product(
                id = rs.getLong("id"),
                title = rs.getString("title"),
                vendor = rs.getString("vendor"),
                handle = rs.getString("handle"),
                variantsJson = rs.getString("variants_json") ?: "[]"
            )
        }.list()
    }

    fun countTotalProduct(): Long {
        return jdbcClient.sql("SELECT COUNT(*) FROM products")
            .query(Long::class.java)
            .single()
    }



    fun saveProduct(product: Product): Long {
        return jdbcClient.sql(
            """
            INSERT INTO products (title, vendor, handle,variants_json) 
            VALUES (:title, :vendor, :handle, CAST(:variantsJson AS JSONB))
            RETURNING id
            """
        )
            .param("title", product.title)
            .param("vendor", product.vendor)
            .param("handle", product.handle)
            .param("variantsJson", product.variantsJson)
            .query(Long::class.java)
            .single()
    }

    fun getTotalProducts(): Int =
        jdbcClient.sql("SELECT COUNT(*) FROM products")
            .query { rs, _ -> rs.getInt(1) }
            .single()

    fun findById(id: Long): Product? {
        return jdbcClient.sql(
            """
        SELECT id, title, vendor, handle, feature_image, variants_json 
        FROM products 
        WHERE id = :id
        """
        )
            .param("id", id)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    handle = rs.getString("handle"),
                    variantsJson = rs.getString("variants_json") ?: "[]"
                )
            }
            .optional()
            .orElse(null)
    }


    fun updateById(id: Long, updated: Product): Int {
        return jdbcClient.sql(
            """
        UPDATE products 
        SET title = :title,
            vendor = :vendor,
            handle = :handle
        WHERE id = :id
        """
        )
            .param("id", id)
            .param("title", updated.title)
            .param("vendor", updated.vendor)
            .param("handle", updated.handle)
            .update()
    }

    fun deleteById(id: Long): Int {
        return jdbcClient.sql(
            "DELETE FROM products WHERE id = :id"
        )
            .param("id", id)
            .update()
    }


    fun findPagedProduct(limit: Int, offset: Int): List<Product> {
        return jdbcClient.sql(
            """
        SELECT * FROM products
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
            """
        ).param("limit", limit)
            .param("offset", offset)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    handle = rs.getString("handle"),
                    variantsJson = rs.getString("variants_json") ?: "[]"
                )
            }.list()
    }

    fun countSearchedProduct(search: String): Int {
        return if (search.isBlank()) {
            jdbcClient.sql("SELECT COUNT(*) FROM products")
                .query(Int::class.java)
                .single() ?: 0
        } else {
            jdbcClient.sql("SELECT COUNT(*) FROM products WHERE LOWER(title) LIKE :search")
                .param("search", "%${search.lowercase()}%")
                .query(Int::class.java)
                .single() ?: 0
        }
    }



    fun findPaged(limit: Int, offset: Int, search: String): List<Product> {
        val sql = if (search.isBlank()) {
            """
        SELECT id, title, vendor, handle
        FROM products
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
        """
        } else {
            """
        SELECT id, title, vendor, handle
        FROM products
        WHERE LOWER(title) LIKE :search
        ORDER BY id DESC
        LIMIT :limit OFFSET :offset
        """
        }

        return jdbcClient.sql(sql)
            .param("search", "%${search.lowercase()}%")
            .param("limit", limit)
            .param("offset", offset)
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    vendor = rs.getString("vendor"),
                    handle = rs.getString("handle")
                )
            }
            .list()
    }

}
