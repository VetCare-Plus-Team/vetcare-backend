# VetCare Backend

## Project Overview

VetCare Backend is a comprehensive microservices-based veterinary clinic management system designed to streamline pet healthcare operations. The system provides a complete digital solution for managing pet profiles, appointments, medical records, vaccinations, medications, and staff operations in a veterinary clinic environment.

### Key Features

- **Pet Profile Management**: Complete pet and owner information management with QR code generation
- **Appointment Scheduling**: Book, reschedule, and manage veterinary appointments
- **Medical Records**: Comprehensive health history aggregation from multiple services
- **Vaccination Tracking**: Automated vaccination scheduling and reminder notifications
- **Medication Management**: Prescription and medication administration tracking
- **User Authentication**: Secure JWT-based authentication with role-based access control
- **Employee Management**: Staff profile management with image upload capabilities
- **Audit Logging**: Centralized logging system for all system activities
- **Email Notifications**: Automated vaccination reminders and custom notifications
- **API Gateway**: Centralized entry point with authentication and routing

## Architecture

The system follows a microservices architecture pattern with the following components:

```
┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │  Eureka Server  │
│   (Port: 8080)  │    │  (Port: 8761)   │
└─────────────────┘    └─────────────────┘
          │                       │
          │                       │
┌─────────┼─────────┬─────────────┼─────────────┐
│         │         │             │             │
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ │
│ │User Auth     │ │Employee     │ │Pet Profile  │ │
│ │Service       │ │Service      │ │Service      │ │
│ │(Port: 8081)  │ │(Port: 8091) │ │(Port: 8082) │ │
│ └──────────────┘ └──────────────┘ └──────────────┘ │
│                                                   │
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ │
│ │Appointment   │ │Medical Record│ │Vaccination  │ │
│ │Service       │ │Service       │ │Service      │ │
│ │(Port: 8087)  │ │(Port: 8084)  │ │(Port: 8085) │ │
│ └──────────────┘ └──────────────┘ └──────────────┘ │
│                                                   │
│ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ │
│ │Medication    │ │QR Code      │ │Calculation   │ │
│ │Service       │ │Service      │ │Service      │ │
│ │(Port: 8086)  │ │(Port: 8083) │ │(Port: 8089) │ │
│ └──────────────┘ └──────────────┘ └──────────────┘ │
│                                                   │
│ ┌──────────────┐ ┌──────────────┐                 │
│ │Notification  │ │Log Service   │                 │
│ │Service       │ │(Port: 8088)  │                 │
│ │(Port: 8092)  │ └──────────────┘                 │
└───────────────────────────────────────────────────┘
```

### Communication Patterns

- **Synchronous**: REST API calls via Spring Cloud OpenFeign
- **Asynchronous**: RabbitMQ messaging for logging and notifications
- **Service Discovery**: Netflix Eureka for dynamic service registration and discovery

## Services Overview

### 1. API Gateway Service (Port: 8080)

- **Purpose**: Centralized entry point for all client requests
- **Key Features**:
  - JWT token validation and user role extraction
  - Request routing to appropriate microservices
  - CORS configuration for frontend integration
- **Technology**: Spring Cloud Gateway, JWT, Eureka Client

### 2. Eureka Server (Port: 8761)

- **Purpose**: Service discovery and registration
- **Key Features**:
  - Dynamic service registration
  - Load balancing support
  - Service health monitoring
- **Technology**: Netflix Eureka Server

### 3. User Authentication Service (Port: 8081)

- **Purpose**: User management and authentication
- **Key Features**:
  - User registration with role assignment
  - JWT token generation and validation
  - Password encryption (BCrypt)
  - Employee profile synchronization
- **Technology**: Spring Security, JWT, BCrypt, MySQL

### 4. Employee Service (Port: 8091)

- **Purpose**: Employee profile management
- **Key Features**:
  - Employee CRUD operations
  - Profile image upload and management
  - Bulk employee retrieval for other services
- **Technology**: Spring Web, JPA, File Upload, MySQL

### 5. Pet Profile Service (Port: 8082)

- **Purpose**: Pet and owner information management
- **Key Features**:
  - Pet and owner registration
  - QR code generation integration
  - Profile image management
  - Cascading delete operations
- **Technology**: Spring Web, JPA, OpenFeign, RabbitMQ, MySQL

### 6. Appointment Service (Port: 8087)

- **Purpose**: Veterinary appointment management
- **Key Features**:
  - Appointment booking and scheduling
  - Status management (SCHEDULED, COMPLETED, CANCELLED, RESCHEDULED)
  - Doctor-specific and date-based filtering
  - Audit logging via RabbitMQ
- **Technology**: Spring Web, JPA, OpenFeign, RabbitMQ, MySQL

### 7. Medical Record Service (Port: 8084)

- **Purpose**: Comprehensive pet medical history management
- **Key Features**:
  - Medical record CRUD operations
  - Health history aggregation from multiple services
  - Integration with vaccination, medication, and pet profile services
  - QR code-based health history access
- **Technology**: Spring Web, JPA, OpenFeign, RabbitMQ, MySQL

### 8. Vaccination Service (Port: 8085)

- **Purpose**: Pet vaccination tracking and scheduling
- **Key Features**:
  - Vaccination record management
  - Automatic next due date calculation
  - Upcoming vaccination queries
  - Doctor-specific vaccination tracking
- **Technology**: Spring Web, JPA, OpenFeign, RabbitMQ, MySQL

### 9. Medication Service (Port: 8086)

- **Purpose**: Prescription and medication administration tracking
- **Key Features**:
  - Medication record management
  - Status tracking (ACTIVE, COMPLETED, CANCELLED)
  - Pet and doctor-specific queries
  - Audit logging
- **Technology**: Spring Web, JPA, RabbitMQ, MySQL

### 10. QR Code Service (Port: 8083)

- **Purpose**: QR code generation for pet profiles
- **Key Features**:
  - QR code generation with pet profile links
  - Image storage and serving
  - QR code cleanup operations
- **Technology**: Spring Web, JPA, ZXing QR Code Library, MySQL

### 11. Calculation Service (Port: 8089)

- **Purpose**: Date calculation utilities
- **Key Features**:
  - Next vaccination date calculation based on vaccine type
  - Configurable intervals (1-year, 6-month, 21-day)
- **Technology**: Spring Web, JPA, MySQL

### 12. Log Service (Port: 8088)

- **Purpose**: Centralized audit logging
- **Key Features**:
  - Asynchronous log collection via RabbitMQ
  - Admin-only log access
  - Comprehensive audit trail
- **Technology**: Spring Web, JPA, RabbitMQ, MySQL

### 13. Notification Service (Port: 8092)

- **Purpose**: Email notification management
- **Key Features**:
  - Automated vaccination reminders
  - Scheduled email delivery (daily at 8:00 AM)
  - Custom email sending
  - Integration with pet and vaccination services
- **Technology**: Spring Web, JPA, OpenFeign, JavaMail, MySQL

## Technology Stack

### Core Technologies

- **Java**: 17
- **Spring Boot**: 3.3.4
- **Spring Cloud**: 2023.0.3

### Frameworks & Libraries

- **Spring Web**: REST API development
- **Spring Data JPA**: Database persistence
- **Spring Security**: Authentication and authorization
- **Spring Cloud Gateway**: API Gateway
- **Spring Cloud OpenFeign**: Declarative REST clients
- **Spring AMQP**: RabbitMQ integration
- **JWT**: Token-based authentication
- **ZXing**: QR code generation
- **JavaMail**: Email notifications

### Infrastructure

- **Netflix Eureka**: Service discovery
- **RabbitMQ**: Message queuing
- **MySQL**: Relational database
- **Maven**: Build and dependency management

## Database Schema

The system uses multiple MySQL databases, one per service:

- `vetcare_auth_db`: User authentication data
- `vetcare_employee_db`: Employee profiles
- `vetcare_pet_profile_db`: Pet and owner information
- `vetcare_appointment_db`: Appointment scheduling
- `vetcare_medical_db`: Medical records
- `vetcare_vaccination_db`: Vaccination tracking
- `vetcare_medication_db`: Medication records
- `vetcare_qr_db`: QR code metadata
- `vetcare_calculation_db`: Calculation utilities
- `vetcare_log_db`: Audit logs
- `vetcare_notification_db`: Notification data

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- RabbitMQ Server
- Git

## Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd vetcare-backend
```

### 2. Database Setup

Create the required MySQL databases:

```sql
CREATE DATABASE vetcare_auth_db;
CREATE DATABASE vetcare_employee_db;
CREATE DATABASE vetcare_pet_profile_db;
CREATE DATABASE vetcare_appointment_db;
CREATE DATABASE vetcare_medical_db;
CREATE DATABASE vetcare_vaccination_db;
CREATE DATABASE vetcare_medication_db;
CREATE DATABASE vetcare_qr_db;
CREATE DATABASE vetcare_calculation_db;
CREATE DATABASE vetcare_log_db;
CREATE DATABASE vetcare_notification_db;
```

### 3. RabbitMQ Setup

Ensure RabbitMQ is running with default credentials (guest/guest).

### 4. Build All Services

```bash
# Build all services
for service in */; do
  if [ -f "$service/pom.xml" ]; then
    cd "$service"
    mvn clean install -DskipTests
    cd ..
  fi
done
```

### 5. Start Services in Order

1. **Start Eureka Server**:

   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Start API Gateway**:

   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

3. **Start Core Services** (in any order):

   ```bash
   # User Auth Service
   cd user-auth-service
   mvn spring-boot:run

   # Employee Service
   cd employee-service
   mvn spring-boot:run

   # Pet Profile Service
   cd pet-profile-service
   mvn spring-boot:run

   # Other services...
   ```

## API Documentation

### Authentication

All API endpoints (except auth endpoints) require JWT token in Authorization header:

```
Authorization: Bearer <jwt-token>
```

### Key API Endpoints

#### Authentication

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

#### Pet Management

- `POST /api/pets/pet` - Register pet
- `GET /api/pets/pet/{id}` - Get pet details
- `GET /api/medical-records/history/{petId}` - Get complete pet health history

#### Appointments

- `POST /api/appointments` - Book appointment
- `GET /api/appointments/doctor/{doctorId}` - Get doctor's appointments

#### Admin Operations

- `GET /api/logs` - View audit logs (Admin only)

## Configuration

Each service has its own `application.properties` file with:

- Database connection settings
- Eureka server URL
- RabbitMQ configuration
- Service-specific properties

## Monitoring & Logging

- **Eureka Dashboard**: http://localhost:8761 - Service registry status
- **Centralized Logging**: All services publish logs to log-service via RabbitMQ
- **Database Monitoring**: Each service maintains its own database for isolation

## Security

- JWT-based authentication
- Role-based access control (ADMIN, STAFF, DOCTOR)
- Password encryption using BCrypt
- API Gateway authentication filter
- Secure inter-service communication

## Development

### Code Structure

Each microservice follows a standard Spring Boot structure:

```
src/main/java/com/vetcare/{service_name}/
├── controller/     # REST controllers
├── service/        # Business logic
├── entity/         # JPA entities
├── dto/            # Data transfer objects
├── repo/           # JPA repositories
├── config/         # Configuration classes
└── exception/      # Custom exceptions
```

### Testing

Run tests for individual services:

```bash
cd {service-name}
mvn test
```

## Contributing

1. Follow microservice architecture principles
2. Maintain database isolation per service
3. Use OpenFeign for inter-service communication
4. Publish audit logs via RabbitMQ
5. Follow REST API conventions
6. Implement proper error handling

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact the development team.
