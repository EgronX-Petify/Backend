# Authentication

## Login
- Endpoint: `POST /api/v1/auth/login`
- Description: Authenticate a user and return jwt token

### Request Body
```json
{
  "email": "string",
  "password": "string"
}
```
### Success Response
```json
{
  "token": "jwt_token"
}
```
**It will also set a `refresh token` COOKIE**
___
## Logout
- EndPoint: `POST /api/v1/auth/logout`
- Description: Logout user
- Note: **This just removes the `refresh token` cookie the front should remove the access token**

### Success Response
OK
```txt
Logged out
```

---
## Register
- Endpoint: `POST /api/v1/auth/signup`
- Description: Create a new user account

### Request Body
```json
{
  "email": "string",
  "password": "string",
  "role": "string"
}
```
### Success Response
`201 CREATED`
```json
{
  "message": "User Successfully Registered"
}
```

___
## Forgot Password
- Endpoint: `POST /api/v1/auth/forgot-password`
- Description: request a token for reset password.

### Request Body
```json
{
  "email": "string"
}
```

### Success Response
```json
{
  "message": "token was send to your email"
}
```
___
## Change Password
- Endpoint: `POST /api/v1/auth/change-password`
- Description: change password using the reset pass token

### Request Body
```json
{
  "token": "UUID-string",
  "newPassword": "string"
}
```
### Success Response
```json
{
  "message": "Password has been changed successfully"
}
```

## Refresh Auth
- Endpoint: `POST /api/v1/auth/refresh`
- Description: returns new access token using the refresh token and rotate the refresh token

### Success Response
OK
```json
{
  "token": "jwt_token"
}
```