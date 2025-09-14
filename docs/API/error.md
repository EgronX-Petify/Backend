# API Error Response
- Description: Unified error response for all errors

## Response
```json
{
  "status": "HTTP Status code int",
  "message": "string",
  "errors": [
    {
      "field": "string",
      "message": "string"
    },
    {
      "field": "string",
      "message": "string"
    }
  ]
}
```

# API Error Handling Documentation

## Overview

The **Global Exception Handler** (`GlobalExceptionHandler`) is responsible for capturing exceptions across the application and returning a consistent error response (`APIErrorResponse`).

All API errors follow a unified JSON structure:

```json
{
  "status": <HTTP status code>,
  "message": "<Human-readable message>",
  "errors": {
    "<field/error>": "<details>"
  }
}
```

* `status`: HTTP status code.
* `message`: General error message.
* `errors`: Key-value pairs describing field-specific or additional errors.
As Shown Above.
---

## Exception Mapping

### 1. Generic Exception

* **Exception:** `Exception`
* **HTTP Status:** `500 Internal Server Error`
* **Message:** `"Unexpected error occurred."`
* **Error Field:** `"error": "<exception message>"`

**Example Response**

```json
{
  "status": 500,
  "message": "Unexpected error occurred.",
  "errors": {
    "type": "Exception class name",
    "error": "Null pointer exception"
  }
}
```

---

### 2. Authentication & Authorization

#### Bad Credentials

* **Exception:** `BadCredentialsException`
* **HTTP Status:** `401 Unauthorized`
* **Message:** `"Invalid username or password."`

```json
{
  "status": 401,
  "message": "Invalid username or password."
}
```

#### Access Denied

* **Exception:** `AuthorizationDeniedException`
* **HTTP Status:** `401 Unauthorized`
* **Message:** `"Access denied."`

```json
{
  "status": 401,
  "message": "Access denied."
}
```

#### User Not Found

* **Exception:** `UsernameNotFoundException`
* **HTTP Status:** `401 Unauthorized`
* **Message:** `"User not found"`

```json
{
  "status": 401,
  "message": "User not found"
}
```

#### Invalid Token

* **Exception:** `InvalidTokenException`
* **HTTP Status:** `401 Unauthorized`
* **Message:** `"Invalid token."`
* **Error Field:** `"error": "<exception message>"`

```json
{
  "status": 401,
  "message": "Invalid token.",
  "errors": {
    "error": "Token signature is invalid"
  }
}
```

---

### 3. User Registration

#### Username Already Exists

* **Exception:** `UsernameAlreadyExistsException`
* **HTTP Status:** `409 Conflict`
* **Message:** `"User already exists"`
* **Error Field:** `"error": "<exception message>"`

```json
{
  "status": 409,
  "message": "User already exists",
  "errors": {
    "error": "Username testUser already exists"
  }
}
```

---

### 4. Bad Request Errors

#### Invalid Argument

* **Exception:** `IllegalArgumentException`
* **HTTP Status:** `400 Bad Request`
* **Message:** `"<exception message>"`

```json
{
  "status": 400,
  "message": "Invalid input provided"
}
```

#### Invalid JSON

* **Exception:** `HttpMessageNotReadableException`
* **HTTP Status:** `400 Bad Request`
* **Message:** `"Invalid JSON object."`

```json
{
  "status": 400,
  "message": "Invalid JSON object."
}
```

#### Missing Parameter

* **Exception:** `MissingServletRequestParameterException`
* **HTTP Status:** `400 Bad Request`
* **Message:** `"Missing Required request parameters"`
* **Error Field:** `"<parameterName>": "Parameter is missing"`

```json
{
  "status": 400,
  "message": "Missing Required request parameters",
  "errors": {
    "username": "Parameter is missing"
  }
}
```

#### Validation Failure

* **Exception:** `MethodArgumentNotValidException`
* **HTTP Status:** `400 Bad Request`
* **Message:** `"Validation failed."`
* **Error Field:** Each field with validation issues.

```json
{
  "status": 400,
  "message": "Validation failed.",
  "errors": {
    "email": "must be a valid email",
    "password": "must not be blank"
  }
}
```

---

### 5. Resource Not Found

#### Resource Not Found

* **Exception:** `ResourceNotFoundException`
* **HTTP Status:** `404 Not Found`
* **Message:** `"Resource not found."`
* **Error Field:** `"error": "<exception message>"`

```json
{
  "status": 404,
  "message": "Resource not found.",
  "errors": {
    "error": "User with id 123 not found"
  }
}
```

#### No Handler Found

* **Exception:** `NoHandlerFoundException`
* **HTTP Status:** `404 Not Found`
* **Message:** `"Resource not found."`
* **Error Field:** `"error": "<exception message>"`

```json
{
  "status": 404,
  "message": "Resource not found.",
  "errors": {
    "error": "No handler for GET /api/unknown"
  }
}
```

---