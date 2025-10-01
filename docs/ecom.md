# E-Com API Documentation
## 📦 Products

### Get All Products
```http
GET /products
````

**Query Parameters (Optional):**

* `tags` – multiple allowed (`tags=electronics&tags=gaming` or `tags=electronics,gaming`)
* `category` – filter by category (`category=phones`)
* `limit` – pagination limit
* `offset` – pagination offset

**Example:**

```
GET /products?tags=electronics&category=phones&limit=10&offset=0
```

**Response:**

```json
[
{
  "id": 101,
  "sellerId": 5001,
  "name": "Premium Dog Kibble",
  "description": "Grain-free premium dog kibble made with real chicken and sweet potatoes. Suitable for all breeds.",
  "notes": "Store in a cool, dry place after opening",
  "price": 39.99,
  "discount": 5.0,
  "final_price": 37.99,
  "stock": 120,
  "category": "Pet Food",
  "tags": [
    "dog",
    "food",
    "grain-free",
    "chicken",
    "healthy"
  ],
  "images": [
    "https://example.com/images/dog-kibble-front.jpg",
    "https://example.com/images/dog-kibble-back.jpg"
  ]
}
]
```

**Might change and make it Page response instead of list**

---

### Get Single Product

```http
GET /products/{productId}
```

**Response:**

```json
{
  "id": 101,
  "sellerId": 5001,
  "name": "Premium Dog Kibble",
  "description": "Grain-free premium dog kibble made with real chicken and sweet potatoes. Suitable for all breeds.",
  "notes": "Store in a cool, dry place after opening",
  "price": 39.99,
  "discount": 5.0,
  "final_price": 37.99,
  "stock": 120,
  "category": "Pet Food",
  "tags": [
    "dog",
    "food",
    "grain-free",
    "chicken",
    "healthy"
  ],
  "images": [
    "https://example.com/images/dog-kibble-front.jpg",
    "https://example.com/images/dog-kibble-back.jpg"
  ]
}
```

---
## Create Product

```http
POST /products
```

**Request body**
```json
{
  "sellerId": 5001,
  "name": "Premium Dog Kibble",
  "description": "Grain-free premium dog kibble made with real chicken and sweet potatoes. Suitable for all breeds.",
  "notes": "Store in a cool, dry place after opening",
  "price": 39.99,
  "discount": 5.0,
  "final_price": 37.99,
  "stock": 120,
  "category": "Pet Food",
  "tags": [
    "dog",
    "food",
    "grain-free",
    "chicken",
    "healthy"
  ],
  "images": [
    "https://example.com/images/dog-kibble-front.jpg",
    "https://example.com/images/dog-kibble-back.jpg"
  ]
}
```

## Update Product
```http
PUT /products
```

**Request Body**
```json
{
  "sellerId": 5001,
  "name": "Premium Dog Kibble",
  "description": "Grain-free premium dog kibble made with real chicken and sweet potatoes. Suitable for all breeds.",
  "notes": "Store in a cool, dry place after opening",
  "price": 39.99,
  "discount": 5.0,
  "final_price": 37.99,
  "stock": 120,
  "category": "Pet Food",
  "tags": [
    "dog",
    "food",
    "grain-free",
    "chicken",
    "healthy"
  ],
  "images": [
    "https://example.com/images/dog-kibble-front.jpg",
    "https://example.com/images/dog-kibble-back.jpg"
  ]
}
```

## Delete Product
```http
DELETE /products/{id}
```

## 📷 Product Images
**Authorization:** Required (SERVICE_PROVIDER role)

### Upload Product Image
```http
POST /products/{productId}/image
```

**Description:** Upload an image for a product \
**Path Params:**
- `productId` (number): Product ID \
**Request:** Multipart form data
- `file` (file, required): Image file \
**Response:** 200 OK - ProductImage object

**Response Body:**
```json
{
  "id": "number",
  "name": "string",
  "contentType": "string",
  "data": "byte array"
}
```

### Get Product Image by ID
```http
GET /products/{productId}/image/{imageId}
```

**Description:** Get product image by ID \
**Path Params:**
- `productId` (number): Product ID
- `imageId` (number): Image ID \
**Response:** 200 OK - ProductImage object

### Get All Product Images
```http
GET /products/{productId}/image
```

**Description:** Get all images for a product \
**Path Params:**
- `productId` (number): Product ID \
**Response:** 200 OK - Array of ProductImage objects

### Delete Product Image
```http
DELETE /products/{productId}/image/{imageId}
```

**Description:** Delete product image \
**Path Params:**
- `productId` (number): Product ID
- `imageId` (number): Image ID \
**Response:** 204 No Content

## 🛒 Cart

### Get Current Cart

```http
GET /cart
```

**Response:**

```json
{
  "id": 1,
  "userId": 2001,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    },
    {
      "productId": 102,
      "quantity": 1
    },
    {
      "productId": 103,
      "quantity": 5
    }
  ]
}
```

---

### Add Item to Cart

```http
POST /cart/items
```

**Request Body:**

```json
{
      "productId": 101,
      "quantity": 2
}
```

**Response:**

```json
{
  "id": 1,
  "userId": 2001,
  "products": [
    {
      "productId": 101,
      "quantity": 2
    },
    {
      "productId": 102,
      "quantity": 1
    },
    {
      "productId": 103,
      "quantity": 5
    }
  ]
}
```

---

### Update Item Quantity

```http
PUT /cart/items/{productId}
```

**Request Body:**

```json
{ "quantity": 3 }
```

**Response:** `200 OK` (returns updated cart)

---

### Remove Item from Cart

```http
DELETE /cart/items/{productId}
```

**Response:** `204 No Content`

---

### Clear Cart

```http
DELETE /cart
```

**Response:** `204 No Content`

---
