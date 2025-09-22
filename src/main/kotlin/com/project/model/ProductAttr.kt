package com.project.model

data class ProductAttr(
    val body_html: String,
    val created_at: String,
    val handle: String,
    val id: Long,
    val images: List<Image>,
    val options: List<Option>,
    val product_type: String,
    val published_at: String,
    val tags: List<String>,
    val title: String,
    val updated_at: String,
    val variants: Variant?,
    val vendor: String
)