package com.project.dto

data class VariantResponse(
    val id: Long?,
    val title: String?,
    val price: String?,
    val featured_image: FeaturedImageResponse? = null
)

