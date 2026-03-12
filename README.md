# 🚀 Enterprise AI API Gateway

A highly optimized, reactive, and secure API Gateway built to proxy, manage, and accelerate AI model interactions. This project acts as an intelligent middleware between users and multiple Large Language Models (LLMs), featuring real-time streaming, context-aware memory, and robust security.

---

## ✨ Core Features

* **🧠 Intelligent Multi-Model Routing:** Uses the **Strategy Pattern** to dynamically route prompts to either **Groq (Llama 3)** for speed or **Google Gemini (2.5 Flash)** for complex/heavy tasks.
* **⚡ Reactive Streaming (SSE):** Built with Spring WebFlux to stream AI responses word-by-word in real-time, eliminating blocking latency.
* **🗣️ Conversational Memory:** Implements Spring AI's `MessageChatMemoryAdvisor` to give the models "memory," allowing for seamless, context-aware conversations.
* **🔒 Hybrid Security (JWT + Refresh Tokens):** Custom stateless authentication system with short-lived access tokens and Redis-backed, long-lived Refresh Tokens.
* **🛡️ Enterprise Rate Limiting:** Redis-backed rate limiter prevents API abuse and controls AI billing costs (restricted to 5 requests per minute per user).
* **💾 Semantic Caching:** Hashes and caches user prompts and AI responses in Redis. Repeated questions are returned instantly (0ms latency) without calling the LLM.

---

## 🛠️ Architecture & Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.5.x
* **AI Integration:** Spring AI 1.1.2 (`spring-ai-openai`, `spring-ai-google-genai`)
* **Security:** Spring Security, io.jsonwebtoken (0.12.5), BCrypt
* **Database:** MySQL (using Spring `JdbcTemplate`)
* **Cache & Session:** Dockerized Redis (`spring-boot-starter-data-redis`)

---

## 🚀 Getting Started

### 1. Prerequisites
Ensure you have the following installed:
* [Java 21](https://jdk.java.net/21/)
* [Docker](https://www.docker.com/) (For Redis and MySQL containers)
* API Keys for [Groq](https://console.groq.com/) and [Google Gemini](https://aistudio.google.com/)

### 2. Infrastructure Setup (Docker)
Start your Redis and MySQL containers:
```bash
# Start Redis for Caching, Rate Limiting, and Memory
docker run --name gateway-redis -p 6379:6379 -d redis:latest

# Start MySQL for User Management
docker run --name gateway-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root123 -e MYSQL_DATABASE=api_gateway_db -d mysql:latest

```

### 3. Environment Variables

Set your API keys in your environment variables before running the application:

```bash
export GROQ_API_KEY="your_groq_api_key"
export GEMINI_API_KEY="your_gemini_api_key"

```

### 4. Run the Application

Run the Spring Boot application using Maven:

```bash
./mvnw spring-boot:run

```

---

## 📡 API Endpoints

### 🔐 Authentication (`/api/auth`)

| Method | Endpoint | Description | Body / Params |
| --- | --- | --- | --- |
| `POST` | `/signup` | Register a new user | `{"username": "user", "password": "123"}` |
| `POST` | `/login` | Login and get JWT + Refresh Token | `{"username": "user", "password": "123"}` |
| `POST` | `/refresh` | Get a new JWT using a Refresh Token | `?token=YOUR_REFRESH_UUID` |

### 🤖 AI Generation (`/api/call`)

| Method | Endpoint | Description | Headers | Body |
| --- | --- | --- | --- | --- |
| `POST` | `/prompt` | Stream an AI response | `Authorization: Bearer <JWT>` | `{"prompt": "What is Java 21?"}` |

*Note: The `/prompt` endpoint produces `text/event-stream` for real-time frontend rendering.*

---

## 💡 How the Router Works

When a user submits a prompt, the `RouterService` analyzes it:

1. **Cache Check:** Does this prompt exist in Redis? If yes, return the answer instantly.
2. **Complexity Check:** Is the prompt > 500 characters or does it contain keywords like "summarize", "analyze", or "complex"?
* **Yes ➡️** Routes to **Gemini Strategy**
* **No ➡️** Routes to **Groq Strategy**


3. **Execute & Save:** The selected model streams the answer back to the user, saves it to the Chat Memory, and caches the final response in Redis.

---

*Built with ❤️ using Java & Spring Boot.*

```
