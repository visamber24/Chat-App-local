# Chat App Local

A real-time Android chat application built with **Kotlin**, **Jetpack Compose**, **Ktor**, **WebSockets**, and **MongoDB**.

The repository contains both the Android client and the Ktor backend server, so you can run the complete chat system locally.

## Features

- Real-time messaging using WebSockets
- Username-based chat sessions
- Message history loaded through a REST endpoint
- Messages persisted in MongoDB
- Modern Android UI with Jetpack Compose and Material 3
- Koin dependency injection on the Android client and server
- Ktor client with CIO engine
- Ktor backend with Netty
- Kotlin Serialization for JSON
- Docker-ready backend

## Architecture

```text
┌─────────────────────────────┐
│       Android Client        │
│ Kotlin + Jetpack Compose    │
└──────────────┬──────────────┘
               │
       ┌───────┴────────┐
       │                │
 GET /messages     WebSocket
                  /chat-socket
       │                │
       └───────┬────────┘
               │
┌──────────────▼──────────────┐
│        Ktor Server          │
│ Kotlin + Netty + Koin       │
└──────────────┬──────────────┘
               │
┌──────────────▼──────────────┐
│           MongoDB           │
│      Message Persistence    │
└─────────────────────────────┘
```

## Tech Stack

### Android Client

| Technology | Purpose |
|---|---|
| Kotlin | Main programming language |
| Jetpack Compose | UI |
| Material 3 | UI components |
| Navigation Compose | Screen navigation |
| Ktor Client | HTTP and WebSocket communication |
| CIO | Ktor client engine |
| Kotlin Serialization | JSON serialization |
| Koin | Dependency injection |
| Coroutines / Flow | Asynchronous and reactive operations |

### Backend

| Technology | Purpose |
|---|---|
| Kotlin | Main programming language |
| Ktor | Backend framework |
| Netty | Server engine |
| Ktor WebSockets | Real-time communication |
| Kotlin Serialization | JSON serialization |
| Koin | Dependency injection |
| MongoDB Kotlin Driver | Database access |
| Docker | Containerized backend deployment |

## Project Structure

```text
Chat-App-local/
├── chatClient/
│   └── app/
│       └── src/main/java/com/lazysloth/chatapp/
│           ├── data/
│           │   ├── dto/
│           │   └── remote/
│           ├── di/
│           ├── domain/
│           │   └── model/
│           ├── presentation/
│           │   ├── chat/
│           │   └── username/
│           ├── ui/
│           │   └── theme/
│           ├── util/
│           ├── ChatApp.kt
│           └── MainActivity.kt
│
├── chatapp_server/
│   └── src/main/kotlin/com/example/
│       ├── data/
│       ├── plugins/
│       ├── routes/
│       ├── ChatSession.kt
│       ├── MainModule.kt
│       ├── Routing.kt
│       ├── Security.kt
│       └── main.kt
│
└── .gitignore
```

## Requirements

Before running the project, install:

- Android Studio
- Android SDK
- JDK 21 for the backend
- MongoDB locally or a MongoDB Atlas database
- Git
- Docker, optional

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/visamber24/Chat-App-local.git
cd Chat-App-local
```

## Backend Setup

The backend requires a MongoDB connection string through the `MONGODB_URI` environment variable.

The server itself defaults to port `8080`, but the current Android client is configured to connect to port `5005`.

For local development, the easiest option is therefore to run the backend on port `5005`.

### Linux / macOS

```bash
cd chatapp_server

export MONGODB_URI="mongodb://localhost:27017"
export PORT=5005

./gradlew run
```

### Windows PowerShell

```powershell
cd chatapp_server

$env:MONGODB_URI="mongodb://localhost:27017"
$env:PORT="5005"

.\gradlew.bat run
```

If you use MongoDB Atlas, replace the local MongoDB URI with your Atlas connection string.

> Do not commit your MongoDB username, password, or connection string to GitHub.

Once started, the backend should be available locally on:

```text
http://localhost:5005
```

## Android Client Setup

Open the `chatClient` folder in Android Studio.

Before running the app, make sure the server address matches the device you are using.

### Android Emulator

An Android emulator uses `10.0.2.2` to access the host computer.

Use:

```text
HTTP:      http://10.0.2.2:5005
WebSocket: ws://10.0.2.2:5005
```

The HTTP message service already contains emulator detection, but the WebSocket URL is currently hard-coded, so you may need to update `ChatSocketService.kt`.

### Physical Android Device

Your phone cannot use `localhost` to access the server running on your computer.

Connect your phone and computer to the same local network, find your computer's local IPv4 address, and use it in both:

```text
MessageService.kt
ChatSocketService.kt
```

For example:

```text
http://192.168.1.10:5005
ws://192.168.1.10:5005
```

Replace `192.168.1.10` with your computer's actual LAN IP address.

Then build and run the Android app from Android Studio.

## API Endpoints

### Get Message History

```http
GET /messages
```

Returns the stored chat messages from MongoDB.

### Real-Time Chat

```text
WebSocket /chat-socket
```

Used for sending and receiving chat messages in real time.

## Environment Variables

| Variable | Required | Description |
|---|---|---|
| `MONGODB_URI` | Yes | MongoDB connection URI |
| `PORT` | No | Server port. Ktor defaults to `8080` |

For the current Android client configuration, using:

```text
PORT=5005
```

is convenient.

## Running the Backend with Docker

Build the Docker image:

```bash
cd chatapp_server
docker build -t chat-app-server .
```

Run it:

```bash
docker run --rm \
  -p 5005:8080 \
  -e MONGODB_URI="your-mongodb-uri" \
  chat-app-server
```

This maps port `5005` on your computer to port `8080` inside the container.

The Android client can then connect to port `5005`.

## How Messaging Works

1. The user enters a username.
2. The Android client opens a WebSocket connection to `/chat-socket`.
3. The server creates a chat session for that user.
4. Messages sent by a connected user are received by the Ktor server.
5. The server distributes messages to connected chat members.
6. Messages are stored in MongoDB.
7. Previous messages can be loaded through `GET /messages`.

## Common Connection Problems

### App cannot connect to the server

Check that:

- The backend is running.
- The Android client is using the correct IP address.
- The client and server use the same port.
- Your phone and computer are on the same network when using a physical device.
- Your firewall is not blocking the server port.

### MongoDB connection fails

Make sure `MONGODB_URI` is set before starting the backend.

### Emulator can load HTTP messages but WebSocket does not connect

Check `ChatSocketService.kt`.

For the Android emulator, its WebSocket URL should point to:

```text
ws://10.0.2.2:5005
```

### Username conflict

The server prevents duplicate active usernames in the same chat room. Choose another username if the current one is already connected.

## Possible Future Improvements

- Authentication and user accounts
- Configurable server URL instead of hard-coded IP addresses
- HTTPS and secure WebSockets (`wss://`)
- Offline message caching
- Multiple chat rooms
- Image and file sharing
- Message delivery/read status
- Typing indicators
- Push notifications

## Author

**Visamber Barman**

GitHub: [@visamber24](https://github.com/visamber24)

---

If you find this project useful, consider giving the repository a ⭐.
