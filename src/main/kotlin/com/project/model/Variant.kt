package com.project.model

data class Variant(
    val available: Boolean,
    val compare_at_price: Any?,
    val created_at: String,
    val featured_image: FeaturedImage?,
    val grams: Int,
    val id: Long,
    val option1: String,
    val option2: String,
    val option3: Any?,
    val position: Int,
    val price: String,
    val product_id: Long,
    val requires_shipping: Boolean,
    val sku: String,
    val taxable: Boolean,
    val title: String,
    val updated_at: String
)