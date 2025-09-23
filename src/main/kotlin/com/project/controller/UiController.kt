package com.project.controller

import com.project.entity.Product
import com.project.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/ui")
class UiController(
    private val productService: ProductService
) {


    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam vendor: String,
        @RequestParam handle: String,
        @RequestParam(required = false, name = "images") images: List<String>?,
        model: Model
    ): String {
        val product = Product(
            id = null,
            title = title,
            vendor = vendor,
            handle = handle,
            variantsJson = null.toString()
        )

        productService.addProductWithImage(product, images = images)

        val size = 10 // default page size
        val total = productService.getTotalProducts()
        val totalPages = if (total == 0) 0 else (total + size - 1) / size

        // put new product on the last page
        val page = (totalPages - 1).coerceAtLeast(0)
        val offset = page * size

        val products = productService.getProductsPaged(size, offset)
        val hasNextPage = page + 1 < totalPages
        val nextPage = page + 1

        model.addAttribute("products", products)
        model.addAttribute("page", page)
        model.addAttribute("size", size)
        model.addAttribute("totalPages", totalPages)
        model.addAttribute("hasNextPage", hasNextPage)
        model.addAttribute("nextPage", nextPage)

        return "fragments/product-table"
    }


    @GetMapping("/products")
    fun loadProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model
    ): String {
        val safePage = page.coerceAtLeast(0)
        val safeSize = size.coerceIn(1, 100) // prevent invalid size

        val total = productService.getTotalProducts()
        val totalPages = if (total == 0) 0 else (total + safeSize - 1) / safeSize
        val safePageClamped = safePage.coerceAtMost((totalPages - 1).coerceAtLeast(0))

        val offset = safePageClamped * safeSize
        val products = productService.getProductsPaged(safeSize, offset)

        val hasNextPage = safePageClamped + 1 < totalPages
        val nextPage = safePageClamped + 1

        model.addAttribute("products", products)
        model.addAttribute("page", safePageClamped)
        model.addAttribute("size", safeSize)
        model.addAttribute("totalPages", totalPages)
        model.addAttribute("hasNextPage", hasNextPage)
        model.addAttribute("nextPage", nextPage)

        return "fragments/product-table"
    }


   /* @GetMapping("/products/search-results")
    fun searchResults(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model
    ): String {
        val products = productService.getProductsPaged(size, page)
        model.addAttribute("products", products)
        return "products/search"
    }*/

    /*@GetMapping("/products/search")
    fun searchPage(): String {
        return "products/search"
    }


     @GetMapping("/products/search-list")
     fun loadProducts(
         @RequestParam(defaultValue = "0") page: Int,
         @RequestParam(defaultValue = "10") size: Int,
         @RequestParam(defaultValue = "") q: String,
         model: Model
     ): String {
         val safePage = page.coerceAtLeast(0)
         val safeSize = size.coerceIn(1, 100)

         val total = productService.getTotalProducts(q) // filter count
         val totalPages = if (total == 0) 0 else (total + safeSize - 1) / safeSize
         val safePageClamped = safePage.coerceAtMost((totalPages - 1).coerceAtLeast(0))

         val offset = safePageClamped * safeSize
         val products = productService.getProductsPaged(safeSize, offset, q)

         val hasNextPage = safePageClamped + 1 < totalPages
         val nextPage = safePageClamped + 1

         model.addAttribute("products", products)
         model.addAttribute("page", safePageClamped)
         model.addAttribute("size", safeSize)
         model.addAttribute("totalPages", totalPages)
         model.addAttribute("hasNextPage", hasNextPage)
         model.addAttribute("nextPage", nextPage)
         model.addAttribute("search", q)

         return "products/search"
     }*/


    @GetMapping("/products/search")
    fun searchPage(): String {
        return "products/search"
    }

    // ... existing code ...

    @GetMapping("/products/search-list")
    fun loadProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "") q: String,
        model: Model
    ): String {
        val safePage = page.coerceAtLeast(0)
        val safeSize = size.coerceIn(1, 100)

        val total = productService.getTotalProducts(q)
        val totalPages = if (total == 0) 0 else (total + safeSize - 1) / safeSize
        val safePageClamped = safePage.coerceAtMost((totalPages - 1).coerceAtLeast(0))

        val offset = safePageClamped * safeSize
        val products = productService.getProductsPaged(safeSize, offset, q)

        val hasNextPage = safePageClamped + 1 < totalPages
        val nextPage = safePageClamped + 1

        model.addAttribute("products", products)
        model.addAttribute("page", safePageClamped)
        model.addAttribute("size", safeSize)
        model.addAttribute("totalPages", totalPages)
        model.addAttribute("hasNextPage", hasNextPage)
        model.addAttribute("nextPage", nextPage)
        model.addAttribute("search", q)

        return "fragments/product-table :: productTableFragment"
    }

}

