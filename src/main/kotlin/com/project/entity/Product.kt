package com.project.entity

data class Product(
    val id: Long? = null,
    val title: String,
    val vendor: String?,
    val handle: String?,
    val variantsJson: String? = null // stored as JSONB
)