# 🚗 RideShare API

A modern, secure ride-sharing platform built with Spring Boot and MongoDB Atlas. This REST API enables users to request rides and drivers to accept and complete them, featuring JWT authentication and role-based access control.
---

## ✨ Features

- 🔐 **JWT Authentication** - Secure token-based authentication
- 👥 **Role-Based Access Control** - Separate permissions for users and drivers
- 🚖 **Ride Management** - Create, accept, and complete ride requests
- 📊 **Real-time Status Tracking** - Monitor ride status (REQUESTED, ACCEPTED, COMPLETED)
- ☁️ **Cloud Database** - Powered by MongoDB Atlas for scalability
- 🛡️ **Spring Security** - Industry-standard security implementation
- ✅ **Input Validation** - Bean validation for all requests

---

## 🏗️ Architecture

### Tech Stack
- **Backend Framework:** Spring Boot 4.0.0
- **Security:** Spring Security + JWT (jjwt 0.11.5)
- **Database:** MongoDB Atlas
- **Server:** Embedded Tomcat
- **Build Tool:** Maven
- **Java Version:** 17

### Project Structure
```
RideSharing/
├── src/main/java/com/sanaa/RideShare/
│   ├── config/          # Security & MongoDB configuration
│   ├── controller/      # REST API endpoints
│   ├── dto/            # Data Transfer Objects
│   ├── exception/      # Custom exceptions & handlers
│   ├── model/          # Domain entities
│   ├── repository/     # MongoDB repositories
│   ├── service/        # Business logic
│   └── util/           # JWT utilities & filters
└── src/main/resources/
    └── application.properties
```

---

##  Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB Atlas account (or local MongoDB instance)
- Your favorite API testing tool (Postman, cURL, etc.)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sanaa-dubh/RideSharing.git
   cd RideSharing
   ```

2. **Configure MongoDB**
   
   Create `src/main/resources/application.properties`:
   ```properties
   spring.application.name=
   server.port=
   spring.data.mongodb.uri=
   jwt.secret=
   jwt.expiration-ms=
   ```

   > 💡 **Note:** Replace the MongoDB URI with your actual credentials

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Verify it's running**
   
   The application should start on `http://localhost:8081`
   
   You should see:
   ```
   Started RideShareApplication in X.XXX seconds
   ```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8081
```

### Authentication Endpoints

#### Register a New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "alice",
  "password": "password123",
  "role": "ROLE_USER"
}
```

**Roles:**
- `ROLE_USER` - For passengers
- `ROLE_DRIVER` - For drivers

**Response:** `200 OK`

---

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZSIsInJvbGUi..."
}
```

> 🔑 Use this token in the `Authorization: Bearer <token>` header for protected endpoints

---

### User Endpoints (ROLE_USER)

#### Create a Ride Request
```http
POST /api/v1/rides
Authorization: Bearer <token>
Content-Type: application/json

{
  "pickupLocation": "MG Road, Bangalore",
  "dropLocation": "Koramangala, Bangalore"
}
```

**Response:**
```json
{
  "id": "675606c8a1b2c3d4e5f6g7h8",
  "userId": "675606a1b2c3d4e5f6g7h8i9",
  "driverId": null,
  "pickupLocation": "MG Road, Bangalore",
  "dropLocation": "Koramangala, Bangalore",
  "status": "REQUESTED",
  "createdAt": "2025-12-08T16:30:00.000+00:00"
}
```

---

#### Get My Rides
```http
GET /api/v1/user/rides
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": "675606c8a1b2c3d4e5f6g7h8",
    "userId": "675606a1b2c3d4e5f6g7h8i9",
    "driverId": "675606b2c3d4e5f6g7h8i9j0",
    "pickupLocation": "MG Road, Bangalore",
    "dropLocation": "Koramangala, Bangalore",
    "status": "ACCEPTED",
    "createdAt": "2025-12-08T16:30:00.000+00:00"
  }
]
```

---

### Driver Endpoints (ROLE_DRIVER)

#### Get Pending Ride Requests
```http
GET /api/v1/driver/rides/requests
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": "675606c8a1b2c3d4e5f6g7h8",
    "userId": "675606a1b2c3d4e5f6g7h8i9",
    "driverId": null,
    "pickupLocation": "MG Road, Bangalore",
    "dropLocation": "Koramangala, Bangalore",
    "status": "REQUESTED",
    "createdAt": "2025-12-08T16:30:00.000+00:00"
  }
]
```

---

#### Accept a Ride
```http
POST /api/v1/driver/rides/{rideId}/accept
Authorization: Bearer <token>
```

**Response:**
```json
{
  "id": "675606c8a1b2c3d4e5f6g7h8",
  "userId": "675606a1b2c3d4e5f6g7h8i9",
  "driverId": "675606b2c3d4e5f6g7h8i9j0",
  "pickupLocation": "MG Road, Bangalore",
  "dropLocation": "Koramangala, Bangalore",
  "status": "ACCEPTED",
  "createdAt": "2025-12-08T16:30:00.000+00:00"
}
```

---

### Shared Endpoints

#### Complete a Ride
```http
POST /api/v1/rides/{rideId}/complete
Authorization: Bearer <token>
```

> Can be called by either the user who requested the ride or the driver who accepted it

**Response:**
```json
{
  "id": "675606c8a1b2c3d4e5f6g7h8",
  "userId": "675606a1b2c3d4e5f6g7h8i9",
  "driverId": "675606b2c3d4e5f6g7h8i9j0",
  "pickupLocation": "MG Road, Bangalore",
  "dropLocation": "Koramangala, Bangalore",
  "status": "COMPLETED",
  "createdAt": "2025-12-08T16:30:00.000+00:00"
}
```

---

## 🧪 Testing with cURL

### Complete Test Flow

```bash
# 1. Register a user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"password123","role":"ROLE_USER"}'

# 2. Register a driver
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"bob_driver","password":"driver123","role":"ROLE_DRIVER"}'

# 3. Login as user (save the token)
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"password123"}'

# 4. Login as driver (save the token)
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"bob_driver","password":"driver123"}'

# 5. Create a ride (use user token)
curl -X POST http://localhost:8081/api/v1/rides \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <USER_TOKEN>" \
  -d '{"pickupLocation":"MG Road","dropLocation":"Koramangala"}'

# 6. Get pending requests (use driver token)
curl -X GET http://localhost:8081/api/v1/driver/rides/requests \
  -H "Authorization: Bearer <DRIVER_TOKEN>"

# 7. Accept ride (use driver token and ride ID from step 5)
curl -X POST http://localhost:8081/api/v1/driver/rides/<RIDE_ID>/accept \
  -H "Authorization: Bearer <DRIVER_TOKEN>"

# 8. Complete ride (use either token)
curl -X POST http://localhost:8081/api/v1/rides/<RIDE_ID>/complete \
  -H "Authorization: Bearer <TOKEN>"
```

---

## 🔒 Security

- **Password Encryption:** BCrypt with Spring Security
- **JWT Tokens:** HS256 algorithm with configurable expiration
- **Role-Based Access:** Method-level security with `@PreAuthorize`
- **CORS:** Configurable for production deployment
- **Input Validation:** Jakarta Bean Validation on all DTOs

---

## 📊 Database Schema

### Users Collection
```javascript
{
  "_id": ObjectId,
  "username": String,
  "password": String (BCrypt hashed),
  "role": String // "ROLE_USER" or "ROLE_DRIVER"
}
```

### Rides Collection
```javascript
{
  "_id": ObjectId,
  "userId": String,
  "driverId": String (nullable),
  "pickupLocation": String,
  "dropLocation": String,
  "status": String, // "REQUESTED", "ACCEPTED", "COMPLETED"
  "createdAt": Date
}
```

---

## 🛠️ Development

### Run Tests
```bash
./mvnw test
```

### Build JAR
```bash
./mvnw clean package
java -jar target/RideSharing-0.0.1-SNAPSHOT.jar
```

### Clean Build
```bash
./mvnw clean install
```

---

## 🐛 Troubleshooting

### Common Issues

**1. MongoDB Connection Failed**
```
Exception: Connection refused: no further information
```
**Solution:** Verify your MongoDB Atlas connection string in `application.properties`

---

**2. JWT Token Invalid**
```
Status: 401 Unauthorized
```
**Solution:** 
- Check if token is expired (default: 24 hours)
- Ensure `Authorization: Bearer <token>` header is set correctly
- Verify JWT secret is consistent

---

**3. Port Already in Use**
```
Port 8081 is already in use
```
**Solution:** Change the port in `application.properties`:
```properties
server.port=8082
```

---

## 🚀 Deployment

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/RideSharing-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t rideshare-api .
docker run -p 8081:8081 rideshare-api
```

---

## 📝 Environment Variables (Production)

For production deployment, use environment variables instead of hardcoded values:

```bash
export MONGODB_URI="mongodb+srv://..."
export JWT_SECRET="your-production-secret"
export JWT_EXPIRATION="86400000"
export SERVER_PORT="8081"
```

Update `application.properties`:
```properties
spring.data.mongodb.uri=${MONGODB_URI}
jwt.secret=${JWT_SECRET}
jwt.expiration-ms=${JWT_EXPIRATION}
server.port=${SERVER_PORT}
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 👤 Author

**Sanaa**
- GitHub: [@sanaa-duhh](https://github.com/sanaa-duhh)
- Project: [RideSharing](https://github.com/sanaa-duhh/RideSharing)

---
