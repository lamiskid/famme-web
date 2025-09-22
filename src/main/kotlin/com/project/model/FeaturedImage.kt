package com.project.model

data class FeaturedImage(
    val alt: String,
    val created_at: String,
    val height: Int,
    val id: Long,
    val position: Int,
    val product_id: Long,
    val src: String,
    val updated_at: String,
    val variant_ids: List<Long>,
    val width: Int
)