package com.project.dto


data class ProductResponse(
    val id: Long,
    val title: String,
    val vendor: String?,
    val handle: String?,
    val images: List<ImageResponse> = emptyList(),
    val variants: List<VariantResponse> = emptyList()
)