# Petify API Endpoints Documentation

## Authentication Endpoints

### POST /api/v1/auth/login
**Description:** Authenticate user and obtain access token
**Authorization:** None (public)
**Request Body:**
```json
{
  "email": "string (required, valid email)",
  "password": "string (required)"
}
```
**Response:** 200 OK
```json
{
  "token": "string"
}
```
**Side Effects:** Sets `refreshToken` HTTP-only cookie

### POST /api/v1/auth/signup
**Description:** Register new user account
**Authorization:** None (public)
**Request Body:**
```json
{
  "email": "string",
  "password": "string", 
  "role": "string (Valid values: ADMIN, SERVICE_PROVIDER, PET_OWNER)"
}
```
**Response:** 201 Created
```json
{
  "message": "string"
}
```

### POST /api/v1/auth/refresh
**Description:** Refresh access token using refresh token cookie
**Authorization:** None (requires refreshToken cookie)
**Request Body:** None
**Response:** 200 OK
```json
{
  "token": "string"
}
```
**Side Effects:** Updates `refreshToken` HTTP-only cookie

### POST /api/v1/auth/logout
**Description:** Logout user and clear refresh token
**Authorization:** None
**Request Body:** None
**Response:** 200 OK
```json
"Logged out"
```
**Side Effects:** Clears `refreshToken` cookie

### POST /api/v1/auth/forgot-password
**Description:** Request password reset token
**Authorization:** None (public)
**Request Body:**
```json
{
  "email": "string (required, valid email)"
}
```
**Response:** 201 Created
```json
{
  "token": "string",
  "expiryDate": "datetime"
}
```

### POST /api/v1/auth/change-password
**Description:** Confirm password reset with token
**Authorization:** None (public)
**Request Body:**
```json
{
  "token": "string (required)",
  "newPassword": "string (required)"
}
```
**Response:** 202 Accepted
```json
{
  "message": "Password has been changed successfully"
}
```

## User Profile Endpoints

### GET /api/v1/user/me
**Description:** Get current user profile
**Authorization:** Required (any role)
**Response:** 200 OK
```json
{
  "id": "number",
  "name": "string",
  "email": "string",
  "role": "string",
  "images": "array of ProfileImage objects",
  "phoneNumber": "string",
  "address": "string",
  "description": "string (SERVICE_PROVIDER only)",
  "contactInfo": "string (SERVICE_PROVIDER only)"
}
```

### PUT /api/v1/user/me
**Description:** Update current user profile
**Authorization:** Required (any role)
**Request Body:**
```json
{
  "name": "string (max 255 chars)",
  "phoneNumber": "string (max 255 chars)",
  "address": "string (max 500 chars)",
  "description": "string (max 1000 chars, SERVICE_PROVIDER only)",
  "contactInfo": "string (max 500 chars, SERVICE_PROVIDER only)"
}
```
**Response:** 200 OK - Updated profile object

### GET /api/v1/user/{userId}
**Description:** Get user profile by ID \
**Authorization:** not required \
**Path variables:**
- `userId` (number, required): User ID
**Note:** This endpoint uses @PathVariable to get the required user id
**Response:** 200 OK - User profile object

### POST /api/v1/user/me/image
**Description:** Upload profile image
**Authorization:** Required (any role)\
**Request:** Multipart form data `file` (file, required): Image file

**Response:** 200 OK \
**Response Body** 
```json
{
  "id": "number",
  "name": "string",
  "contentType": "string",
  "data": "byte array"
}
```

### GET /api/v1/user/me/image/{imageId}
**Description:** Get profile image by ID \
**Authorization:** Required (any role) \
**Path Params:** 
- `imageId` (number): Image ID
**Response:** 200 OK - ProfileImage object 

### GET /api/v1/user/me/image
**Description:** Get all profile images
**Authorization:** Required (any role)
**Response:** 200 OK - Array of ProfileImage objects

### DELETE /api/v1/user/me/image/{imageId}
**Description:** Delete profile image
**Authorization:** Required (any role)
**Path Params:**
- `imageId` (number): Image ID
**Response:** 204 No Content

## Pet Management Endpoints
**Authorization:** Required (PET_OWNER role)

### POST /api/v1/user/me/pet
**Description:** Create new pet
**Request Body:**
```json
{
  "name": "string (required)",
  "species": "string (required)",
  "breed": "string (required)", 
  "gender": "string (required)",
  "dateOfBirth": "date (required, YYYY-MM-DD)"
}
```
**Response:** 201 Created - PetResponse object

### GET /api/v1/user/me/pet
**Description:** Get all pets for current user
**Response:** 200 OK - Array of PetResponse objects

### GET /api/v1/user/me/pet/{petId}
**Description:** Get pet by ID
**Path Params:**
- `petId` (number): Pet ID
**Response:** 200 OK - PetResponse object

### PUT /api/v1/user/me/pet/{petId}
**Description:** Update pet information
**Path Params:**
- `petId` (number): Pet ID
**Request Body:**
```json
{
  "name": "string",
  "species": "string",
  "breed": "string",
  "gender": "string", 
  "dateOfBirth": "date (YYYY-MM-DD)"
}
```
**Response:** 200 OK - Updated PetResponse object

### DELETE /api/v1/user/me/pet/{petId}
**Description:** Delete pet
**Path Params:**
- `petId` (number): Pet ID
**Response:** 204 No Content

### GET /api/v1/user/me/pet/count
**Description:** Get total pet count for current user
**Response:** 200 OK
```json
number
```

### POST /api/v1/user/me/pet/{petId}/image
**Description:** Upload pet image \
**Path Params:** - `petId` (number): Pet ID \
**Request:** Multipart form data `file` (file, required): Image file \
**Response:** 200 OK - PetImage object

### GET /api/v1/user/me/pet/{petId}/image/{imageId}
**Description:** Get pet image by ID
**Path Params:**
- `petId` (number): Pet ID
- `imageId` (number): Image ID \
**Response:** 200 OK - PetImage object

### GET /api/v1/user/me/pet/{petId}/image
**Description:** Get all images for a pet
**Path Params:**
- `petId` (number): Pet ID \
**Response:** 200 OK - Array of PetImage objects

### DELETE /api/v1/user/me/pet/{petId}/image/{imageId}
**Description:** Delete pet image \
**Path Params:**
- `petId` (number): Pet ID
- `imageId` (number): Image ID \
**Response:** 204 No Content

## Appointment Management Endpoints (Pet Owner)
**Authorization:** Required (PET_OWNER role) \

### POST /api/v1/user/me/appointment
**Description:** Create new appointment \
**Request Body:**
```json
{
  "petId": "number (required)",
  "serviceId": "number (required)",
  "requestedTime": "datetime (ISO format)",
  "notes": "string"
}
```
**Response:** 201 Created - Appointment object

### GET /api/v1/user/me/appointment/{appointmentId}
**Description:** Get appointment by ID \
**Path Params:** 
- `appointmentId` (number): Appointment ID \
**Response:** 200 OK - Appointment object

### GET /api/v1/user/me/appointment
**Description:** Get appointments with filters \
**Query Params:** 
- `petId` (number, optional): Filter by pet ID
- `status` (string, optional): Filter by status (Valid values: PENDING, APPROVED, COMPLETED, CANCELLED)
- `timeFilter` (string, optional): "upcoming" | "past" \
**Response:** 200 OK - Array of appointment objects

### DELETE /api/v1/user/me/appointment/{appointmentId}
**Description:** Cancel appointment \
**Path Params:**
- `appointmentId` (number): Appointment ID \
**Response:** 204 No Content

## Service Endpoints (Public & Provider)

### GET /api/v1/service/search
**Description:** Search services by term \
**Authorization:** Public \
**Query Params:**
- `searchTerm` (string, required): Search term
**Response:** 200 OK - Array of service objects

### GET /api/v1/service/categories
**Description:** Get all service categories \
**Authorization:** Public \ 
**Response:** 200 OK - Array of service category enum values
```json
[
  "VET", "GROOMING", "TRAINING", "BOARDING", "WALKING", "SITTING", "VACCINATION", "OTHER"
]
```

### GET /api/v1/service
**Description:** Get services with filters \
**Authorization:** Public \
**Query Params:**
- `category` (string, optional): Service category (Valid values: VET, GROOMING, TRAINING, BOARDING, WALKING, SITTING, VACCINATION, OTHER)
- `providerId` (number, optional): Provider ID
**Response:** 200 OK - Array of service objects

### GET /api/v1/service/{id}
**Description:** Get service by ID \
**Authorization:** Public \ 
**Path Params:**
- `id` (number): Service ID
**Response:** 200 OK - Service object

### GET /api/v1/provider/me/service 
**Description:** Get services for current provider \
**Authorization:** Required (SERVICE_PROVIDER role) \
**Response:** 200 OK - Array of service objects

### POST /api/v1/provider/me/service 
**Description:** Create new service \
**Authorization:** Required (SERVICE_PROVIDER role) \
**Request Body:**
```json
{
  "name": "string (required)",
  "description": "string",
  "category": "string (required, Valid values: VET, GROOMING, TRAINING, BOARDING, WALKING, SITTING, VACCINATION, OTHER)",
  "price": "number (required, positive)",
  "notes": "string"
}
```
**Response:** 201 Created - Service object

### PUT /api/v1/provider/me/service/{serviceId}
**Description:** Update service \
**Authorization:** Required (SERVICE_PROVIDER role) \
**Path Params:**
- `serviceId` (number): Service ID \
**Request Body:** Same as create service \
**Response:** 200 OK - Updated service object

### DELETE /api/v1/provider/me/service/{serviceId}
**Description:** Delete service \
**Authorization:** Required (SERVICE_PROVIDER role) \
**Path Params:**
- `serviceId` (number): Service ID \
**Response:** 204 No Content

## Service Provider Appointment Management
**Authorization:** Required (SERVICE_PROVIDER role)

### GET /api/v1/provider/me/service/{serviceId}/appointment
**Description:** Get appointments for specific service \
**Path Params:** `serviceId` (number): Service ID \
**Query Params:**
- `status` (string, optional): Filter by status (Valid values: PENDING, APPROVED, COMPLETED, CANCELLED)
- `timeFilter` (string, optional): "upcoming" | "past" \
**Response:** 200 OK - Array of appointment objects

### GET /api/v1/provider/me/appointment
**Description:** Get all appointments for provider \
**Query Params:**
- `status` (string, optional): Filter by status (Valid values: PENDING, APPROVED, COMPLETED, CANCELLED)
- `timeFilter` (string, optional): "upcoming" | "past" \
**Response:** 200 OK - Array of appointment objects

### GET /api/v1/provider/me/appointment/{appointmentId}
**Description:** Get appointment by ID \
**Path Params:** `appointmentId` (number): Appointment ID \
**Response:** 200 OK - Appointment object

### PATCH /provider/me/appointment/{appointmentId}/approve
**Description:** Approve appointment \
**Path Params:** `appointmentId` (number): Appointment ID \
**Request Body:**
```json
{
  "scheduledTime": "datetime (ISO format, required)",
  "notes": "string"
}
```
**Response:** 200 OK - Updated AppointmentResponse object

### PATCH /provider/me/appointments/{appointmentId}/reject
**Description:** Reject appointment \
**Path Params:** `appointmentId` (number): Appointment ID \
**Request Body:**
```json
{
  "rejectionReason": "string (required)"
}
```
**Response:** 200 OK - Updated AppointmentResponse object

### PATCH /provider/me/appointments/{appointmentId}/complete
**Description:** Mark appointment as complete \
**Path Params:** `appointmentId` (number): Appointment ID \
**Request Body:** None \
**Response:** 200 OK - Updated AppointmentResponse object

## Notification Endpoints
**Authorization:** Required (any role)

### GET /api/v1/user/me/notification
**Description:** Get notifications with pagination \
**Query Params:**
- `page` (number, default: 0): Page number
- `size` (number, default: 10): Page size
- `unreadOnly` (boolean, default: false): Show only unread notifications \
**Response:** 200 OK
```json
{
  "notifications": "array of NotificationResponse objects",
  "page": "number",
  "size": "number",
  "totalElements": "number",
  "totalPages": "number",
  "hasNext": "boolean",
  "hasPrevious": "boolean"
}
```

### GET /api/v1/user/me/notification/count
**Description:** Get notification counts
**Response:** 200 OK
```json
{
  "unreadCount": "number",
  "totalCount": "number"
}
```

### PUT /api/v1/user/me/notification/{notificationId}
**Description:** Mark notification as read \
**Path Params:** `notificationId` (number): Notification ID \
**Response:** 200 OK

### PUT /api/v1/user/me/notification/mark-all-read
**Description:** Mark all notifications as read
**Response:** 200 OK

## Common Response Objects

### PetResponse Object
```json
{
  "id": "number",
  "name": "string",
  "species": "string", 
  "breed": "string",
  "gender": "string",
  "dateOfBirth": "date",
  "images": "Set of PetImage objects"
}
```

### ServiceResponse Object
```json
{
  "id": "number",
  "name": "string",
  "description": "string",
  "category": "string (Valid values: VET, GROOMING, TRAINING, BOARDING, WALKING, SITTING, VACCINATION, OTHER)",
  "price": "number",
  "notes": "string",
  "providerName": "string",
  "providerId": "number"
}
```

### AppointmentResponse Object
```json
{
  "id": "number",
  "petId": "number",
  "petName": "string",
  "serviceId": "number",
  "serviceName": "string",
  "serviceCategory": "string",
  "requestedTime": "datetime",
  "scheduledTime": "datetime",
  "status": "string (Valid values: PENDING, APPROVED, COMPLETED, CANCELLED)",
  "notes": "string",
  "rejectionReason": "string",
  "providerName": "string",
  "providerId": "number",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### PetImage Object
```json
{
  "id": "number",
  "name": "string",
  "contentType": "string",
  "data": "byte array"
}
```

### ProfileImage Object
```json
{
  "id": "number",
  "name": "string",
  "contentType": "string",
  "data": "byte array"
}
```

### NotificationResponse Object
```json
{
  "id": "number",
  "title": "string",
  "message": "string",
  "type": "string (Valid values: GENERAL, WELCOME, APPOINTMENT_CREATED, APPOINTMENT_APPROVED, APPOINTMENT_COMPLETED, APPOINTMENT_CANCELLED, APPOINTMENT_REJECTED, NEW_APPOINTMENT_REQUEST, SERVICE_REMINDER, SYSTEM_MAINTENANCE, PROFILE_UPDATE, APPOINTMENT_REMINDER, PET_CHECKUP_DUE)",
  "createdAt": "datetime",
  "isRead": "boolean"
}
```

## Error Responses
All endpoints may return these error responses:

**400 Bad Request** - Validation errors
```json
{
  "message": "string",
  "errors": "array of field errors"
}
```

**401 Unauthorized** - Authentication required
```json
{
  "message": "Unauthorized"
}
```

**403 Forbidden** - Insufficient permissions
```json
{
  "message": "Forbidden"
}
```

**404 Not Found** - Resource not found
```json
{
  "message": "Resource not found"
}
```

**500 Internal Server Error** - Server error
```json
{
  "message": "Internal server error"
}
```

## Notes
- All authenticated endpoints require valid JWT token in Authorization header: `Bearer <token>`
- Multipart file uploads should use `Content-Type: multipart/form-data`
- DateTime fields use ISO 8601 format: `YYYY-MM-DDTHH:mm:ss`
- Date fields use format: `YYYY-MM-DD`
- **Service categories**: VET, GROOMING, TRAINING, BOARDING, WALKING, SITTING, VACCINATION, OTHER
- **User roles**: ADMIN, SERVICE_PROVIDER, PET_OWNER
- **Appointment statuses**: PENDING, APPROVED, COMPLETED, CANCELLED
- **Notification types**: GENERAL, WELCOME, APPOINTMENT_CREATED, APPOINTMENT_APPROVED, APPOINTMENT_COMPLETED, APPOINTMENT_CANCELLED, APPOINTMENT_REJECTED, NEW_APPOINTMENT_REQUEST, SERVICE_REMINDER, SYSTEM_MAINTENANCE, PROFILE_UPDATE, APPOINTMENT_REMINDER, PET_CHECKUP_DUE