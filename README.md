# Famme Product Repository (Spring Boot + Kotlin)

A Spring Boot project built with Kotlin that demonstrates database operations using **JdbcClient**.  
Includes CRUD functionality, pagination, search, and unit testing setup.

---

## 🚀 Features
- Create, read, update, and delete (CRUD) products
- Search and pagination support
- Example of using **Spring’s JdbcClient** for clean SQL queries
- Unit tests with **JUnit 5** and **Mockito**
- Gradle build configuration

---

## ⚙️ Tech Stack
- **Kotlin**
- **Spring Boot**
- **JdbcClient** (Spring 6.1+)
- **JUnit 5**, **Mockito** (testing)
- **Gradle**

---

## 🛠️ Getting Started



### Prerequisites
- Java 17+
- Gradle
- A running database (PostgreSQL)

### Environment variables:
- DB_URL=
- DB_USER=
- DB_PASSWORD=


### Clone the Repository
https://github.com/lamiskid/famme-web.git

## ✅ Tests Example

```kotlin
 @Test
    fun `getProductWithImage should return null if product not found`() {
        val productId = 99L
        whenever(productRepository.findById(productId)).thenReturn(null)

        val result = productService.getProductWithImage(productId)

        assertNull(result)
    }



