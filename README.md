# Project Setup Guide

## 1. Generate `private.pem` and `public.pem` keys

Run the following command to generate a private key and a public key:

```sh
openssl genrsa -out private.pem 2048 
openssl rsa -in private.pem -pubout -out public.pem
```

## 2. Start the application with Docker Compose

Build and start the services in detached mode:

```sh
docker compose up -d --build
```

Your application should now be running.

## 3. API Endpoints

### 3.1. Authenticate User

**POST** `/auth/login`

- **Description:** Authenticate user and receive a JWT token.
- **Request Body:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response:**
  ```json
  {
    "jwt": "eyJhbGcQg....",
    "userInfo": {
        "id": 9071439718629376,
        "username": "user2",
        "fullName": "User One",
        "email": "user1@example.com",
        "dateOfBirth": "1990-01-01"
    }
  }
  ```

---

### 3.2. Get Current User

**GET** `/user/me`

- **Description:** Get information about the currently authenticated user.
- **Headers:**
    - `Authorization: Bearer <jwt-token>`
- **Response:**
  ```json
  {
    "id": 1,
    "username": "user1",
    "fullName": "User One",
    "email": "user1@example.com",
    "dateOfBirth": "1990-01-01"
  }
  ```

---

### 3.3. Register New User

**POST** `/auth/register`

- **Description:** Register a new user.
- **Request Body:**
  ```json
  {
    "username": "string",
    "password": "string",
    "fullName": "string",
    "email": "string",
    "dateOfBirth": "YYYY-MM-DD"
  }
  ```
- **Response:**
  ```json
  {
    "jwt": "eyJhbGcQg....",
    "userInfo": {
        "id": 9071439718629376,
        "username": "user2",
        "fullName": "User One",
        "email": "user1@example.com",
        "dateOfBirth": "1990-01-01"
    }
  }
  ```