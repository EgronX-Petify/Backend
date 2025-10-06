# Petify Backend

A comprehensive Spring Boot application for pet management services, including e-commerce functionality, appointment scheduling, user management, and notification services.

## 🐾 About Petify

Petify is a full-featured backend service designed to support pet care businesses. It provides a robust platform for managing pets, scheduling appointments with service providers, handling e-commerce transactions, and maintaining user profiles. The application features secure authentication, payment processing integration, and a complete administrative system.

## 🚀 Features

### Core Modules
- **Authentication & Authorization**: JWT-based secure authentication with role-based access control
- **Pet Management**: Complete pet profile management with medical records and care history
- **Service Provider System**: Appointment scheduling and service provider management
- **E-commerce Platform**: Product catalog, shopping cart, and order management
- **Payment Processing**: Integrated payment gateway with webhook support
- **Notification System**: Real-time notifications for users and service providers
- **User Management**: Comprehensive user profiles with role-based permissions
- **File Storage**: Secure file upload and storage for profile pictures and documents

### Technical Features
- **RESTful API**: Well-structured REST endpoints with comprehensive validation
- **Database Integration**: MySQL database with JPA/Hibernate ORM
- **Security**: Spring Security with JWT token authentication
- **Email Services**: SMTP email integration for notifications
- **API Documentation**: Swagger/OpenAPI documentation
- **Error Handling**: Global exception handling with custom error responses
- **Validation**: Comprehensive input validation and sanitization
- **Containerization**: Docker support for easy deployment

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Language**: Java 24
- **Database**: MySQL 8.0
- **Authentication**: JWT (JSON Web Tokens)
- **Security**: Spring Security
- **ORM**: Spring Data JPA / Hibernate
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Email**: Spring Mail with SMTP
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## 📋 Prerequisites

- Docker & Docker Compose
- Git

## ⚙️ Installation & Setup

### ⚠️ Required Environment Variables
**Important**: Before running the application, you must create a `.env` file in the root directory with the following variables:

```env
# Server Configuration
SERVER_PORT=8080

# JWT Configuration
JWT_SECRET=your_jwt_secret_key

# Email Configuration
EMAIL=your_email@gmail.com
EMAIL_PASSWORD=your_app_password

# Payment Gateway (Paymob)
PAYMOB_HMAC_KEY=your_paymob_hmac_key
PAYMOB_API_KEY=your_paymob_api_key
PAYMOB_SECRET_KEY=your_paymob_secret_key
PAYMOB_PUBLIC_KEY=your_paymob_public_key
PAYMOB_INTEGRATION_ID=your_integration_id
WEBHOOK_URL=your_webhook_url
```

**Note**: Database credentials are configured in `docker-compose.yml` (user: `root`, password: `rootpass`, database: `petify`). If you need to change them, edit the `docker-compose.yml` file directly.

### 🚀 Running the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/EgronX-Petify/Backend
   cd Backend
   ```

2. **Create the .env file** with the variables shown above

3. **Start the application**
   ```bash
   docker compose up --build
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080`
   - MySQL Database: `localhost:3306`

## 📚 API Documentation

Once the application is running, you can access the interactive API documentation via Swagger UI:

**Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

The Swagger documentation provides:
- Complete API endpoint reference
- Request/response schemas
- Interactive testing interface
- Authentication examples
- Error response codes

## 🏗️ Project Structure

```
com.example.petify/
├── config/                    # Configuration classes
├── controller/                # REST controllers
├── dto/                       # Data Transfer Objects
├── model/                     # JPA Entities
├── repository/                # Data repositories
├── service/                   # Business logic services
├── mapper/                    # Entity-DTO mappers
├── exception/                 # Custom exceptions
├── utils/                     # Utility classes
├── validation/                # Custom validators
├── specification/             # Specifications
└── runner/                    # Application runners
```

## 🔧 Configuration

### Database Configuration
The application uses MySQL as the primary database. The database schema is automatically created and updated using Hibernate's DDL auto-update feature.

### Security Configuration
- JWT token-based authentication
- Role-based access control (Admin, Pet Owner, Service Provider)
- Password encryption using BCrypt
- CORS configuration for frontend integration

### File Storage
- Images are stored in the `uploads/` directory
- Secure file upload with validation
- Support for various image formats

### Health Checks & Monitoring
The application includes Spring Boot Actuator for comprehensive monitoring and management:
- **Access**: All actuator endpoints are restricted to users with **ADMIN role only**
- **Base Path**: `/actuator`
- **Available Endpoints**:
  - `/actuator/health` - Application health status
  - `/actuator/info` - Application information
  - `/actuator/metrics` - Application metrics
  - `/actuator/env` - Environment properties
  - And more...

**Note**: You must authenticate with admin credentials to access any actuator endpoint. Non-admin users will receive a 403 Forbidden response.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is proprietary software. All rights reserved.

## 🆘 Support

For support and questions, please contact the development team or create an issue in the repository.

