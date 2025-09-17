# Profile API Documentation

## Overview
The Profile API allows authenticated users to view and update their profile information. All endpoints require authentication via JWT token.

## Base URL
All profile endpoints are prefixed with `/api/v1/profile`

---

## Endpoints

### Get Current User Profile
- **Endpoint:** `GET /api/v1/profile`
- **Description:** Retrieve the current user's profile information
- **Authentication:** Required (JWT token)
- **Authorization:** Any authenticated user (ADMIN, SERVICE_PROVIDER, PET_OWNER)

#### Success Response
**Status:** `200 OK`
```json
{
  "id": 1,
  "profileType": "PET_OWNER",
  "imageUrls": ["https://example.com/image1.jpg"],
  "phoneNumber": "+1234567890",
  "name": "John Doe",
  "address": "123 Main St, City, State",
  "businessName": null,
  "description": null,
  "user": {
    "id": 1,
    "email": "john.doe@example.com",
    "role": "PET_OWNER"
  }
}
```

#### Profile Type Specific Fields
- **ADMIN:** `name`
- **PET_OWNER:** `name`, `address`
- **SERVICE_PROVIDER:** `businessName`, `description`

---

### Update Current User Profile
- **Endpoint:** `PUT /api/v1/profile`
- **Description:** Update the current user's profile information
- **Authentication:** Required (JWT token)
- **Authorization:** Any authenticated user (ADMIN, SERVICE_PROVIDER, PET_OWNER)

#### Request Body
```json
{
  "phoneNumber": "+1234567890",
  "name": "John Doe",
  "address": "123 Updated St, City, State",
  "businessName": "My Business",
  "description": "Business description",
  "imageUrls": ["https://example.com/newimage.jpg"]
}
```

**Note:** Only include fields you want to update. Null fields will be ignored.

#### Field Validation
- `phoneNumber`: Max 255 characters
- `name`: Max 255 characters  
- `address`: Max 500 characters
- `businessName`: Max 255 characters
- `description`: Max 1000 characters
- `imageUrls`: List of image URLs

#### Success Response
**Status:** `200 OK`
```json
{
  "id": 1,
  "profileType": "PET_OWNER",
  "imageUrls": ["https://example.com/newimage.jpg"],
  "phoneNumber": "+1234567890",
  "name": "John Doe", 
  "address": "123 Updated St, City, State",
  "businessName": null,
  "description": null,
  "user": {
    "id": 1,
    "email": "john.doe@example.com",
    "role": "PET_OWNER"
  }
}
```

---

## Error Responses

All endpoints may return the following error responses:

### Authentication Errors
**Status:** `401 Unauthorized`
```json
{
  "status": 401,
  "message": "Access denied."
}
```

### Profile Not Found
**Status:** `404 Not Found`
```json
{
  "status": 404,
  "message": "Resource not found.",
  "errors": [
    {
      "field": "error",
      "message": "Profile not found for user"
    }
  ]
}
```

### Validation Errors
**Status:** `400 Bad Request`
```json
{
  "status": 400,
  "message": "Validation failed.",
  "errors": [
    {
      "field": "name",
      "message": "Name must not exceed 255 characters"
    }
  ]
}
```

---

## Usage Examples

### Get Profile (curl)
```bash
curl -X GET "http://localhost:8080/api/v1/profile" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Update Profile (curl)
```bash
curl -X PUT "http://localhost:8080/api/v1/profile" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Name",
    "phoneNumber": "+1234567890"
  }'
```

---

## Notes

1. **Profile Types:** Each user has exactly one profile based on their role:
   - ADMIN → AdminProfile
   - PET_OWNER → POProfile  
   - SERVICE_PROVIDER → SPProfile

2. **Field Relevance:** When updating, only provide fields relevant to your profile type:
   - Admin users: `name`, `phoneNumber`, `imageUrls`
   - Pet Owner users: `name`, `address`, `phoneNumber`, `imageUrls`
   - Service Provider users: `businessName`, `description`, `phoneNumber`, `imageUrls`

3. **Image URLs:** The system expects image URLs to be provided. Image upload functionality would need to be implemented separately.

4. **Partial Updates:** Only the fields provided in the update request will be modified. Other fields remain unchanged.