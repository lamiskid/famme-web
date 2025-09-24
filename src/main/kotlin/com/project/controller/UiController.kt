package com.project.controller

import com.project.entity.Product
import com.project.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

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

    @GetMapping("/products/{id}/edit")
    fun editProductPage(@PathVariable id: Long, model: Model): String {
        val product = productService.findById(id)
        model.addAttribute("product", product)
        return "products/edit"
    }

    @PostMapping("/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestParam title: String,
        @RequestParam vendor: String,
        @RequestParam handle: String,
        redirectAttributes: RedirectAttributes
    ): String {
        val product = Product(
            id = null,
            title = title,
            vendor = vendor,
            handle = handle,
            variantsJson = null.toString()
        )
        productService.updateProduct(id, product)
        redirectAttributes.addFlashAttribute("message", "Product updated")
        return "redirect:/"
    }


    @DeleteMapping("/products/{id}")
    //@ResponseBody
    fun deleteProduct(@PathVariable id: Long, model: Model): String {
        productService.deleteById(id)
        val page = 0
        val size = 10
        val products = productService.getProductsPaged(size, 0)
        val total = productService.getTotalProducts()
        val totalPages = if (size == 0) 0 else (total + size - 1) / size
        val hasNextPage = (page + 1) < totalPages
        model.addAttribute("products", products)
        model.addAttribute("page", page)
        model.addAttribute("size", size)
        model.addAttribute("totalPages", totalPages)
        model.addAttribute("hasNextPage", hasNextPage)
        model.addAttribute("nextPage", page + 1)
        return "fragments/product-table :: productTableFragment"
    }

    @GetMapping("/products/view/{id}")
    fun viewProduct(@PathVariable id: Long, model: Model): String {
        val product = productService.getProductWithImage(id)
            ?: throw NoSuchElementException("Product not found: $id")

        println("Product: $product")

        model.addAttribute("product", product)
        model.addAttribute("variants", product.variants)
        model.addAttribute("images", product.images) // if your view needs them

        return "products/view"
    }

}

