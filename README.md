# GoChat
A Clean Architecture Messaging Application

CSC207 • Group 5101-05 • Fall 2025

<p align="center"> <img src="https://img.shields.io/badge/Language-Java-orange?style=flat-square"> <img src="https://img.shields.io/badge/Architecture-Clean%20Architecture-blue?style=flat-square"> <img src="https://img.shields.io/badge/Build-Maven-yellow?style=flat-square"> <img src="https://img.shields.io/badge/Status-In%20Development-green?style=flat-square"> </p>

---

# Table of Contents

* [Overview](#overview)
* [Team Members](#team-members)
* [User Stories](#user-stories)
* [Use Cases](#use-cases)
* [Domain Entities](#domain-entities)
* [External APIs](#external-apis)
* [Architecture](#architecture)
* [Testing](#testing)
* [Project Status](#project-status)
* [How to Run](#how-to-run)

---

# Overview

GoChat is a messaging application designed using the principles of Clean Architecture.
It allows users to:

* Create and manage accounts
* Search for other users
* Start conversations and send messages
* Delete, reply to, react to, and report messages
* View and search chat history
* Create and manage group chats
* Change chat background themes
* Translate messages in different languages

The project emphasizes modular design, separated layers, and testability.

---

# Team Members

* Tina Ly
* Qianlang Yang
* Hayato Kukihara
* Miles Zhu
* Yuan Lin
* Xurui Wang

---

# User Stories

### 1. Account Management

Users can register, log in, update credentials, and log out securely.

### 2. User Search and Starting Chats

Users can search for specific users and initiate chats.

### 3. Messaging

Users can send, receive, delete, reply to, react to, and report messages.

### 4. Chat History

Users can view and search through their past messages.

### 5. Chat Customization

Users can change background themes to personalize the chat experience.

### 6. Group Chats

Users can create group chats, add/remove participants, and rename conversations.

### 7. Translation

Users can translate messages written in unfamiliar languages.

---

# Use Cases

The system supports the following use cases as outlined in the project blueprint:

1. User Registration
2. User Login
3. User Logout
4. Change Username
5. Change Password
6. Search for User and Start Chat
7. Send Message
8. Receive Message
9. Reply to Message
10. Delete Message
11. React to Message
12. Report Message
13. View Chat History
14. Search Chat History
15. Change Chat Background
16. Create Group Chat
17. Add Users to Group Chat
18. Remove Users from Group Chat
19. Rename Group Chat
20. Translate Messages

---

# Domain Entities

## User

```
userID: int  
username: String  
email: String  
password: String
```

## Message

```
messageID: int  
repliedToID: int  
senderID: int  
receiverID: int  
content: String  
timestamp: DateTime  
status: String  
reaction: String
```

## Chat

```
chatID: int  
participants: List<User>  
messages: HashMap<int, Message>  
background: java.awt.Color
```

---

# External APIs

## LibreTranslate API

Used for translating messages between languages.
Supports auto-detection and multiple target languages.

## Firebase

Potential use cases include:

* Authentication
* Real-time messaging
* Cloud data storage

## Postman Demo API

Used for testing API calls and prototypes during development.

---

# Architecture

The project follows the Clean Architecture model with the following structure:

```
src/
  entity/              # Core business objects
  use_case/            # Application logic
  interface_adapters/  # Presenters, controllers, view models
  view/                # UI components
  data/                # Data storage adapters
test/
pom.xml
README.md
```

This structure enforces a separation of concerns between layers and improves testability.

---

# Testing

* All interactors require unit tests.
* Use Case and Entity layers must reach **100% coverage** by the final deadline.
* JUnit is used for automated testing.
* Mocking is used to isolate business logic where appropriate.

---

# Project Status

| Feature                  | Status            |
| ------------------------ | ----------------- |
| User Authentication      | Complete          |
| Messaging Pipeline       | In Progress       |
| Chat History / Search    | In Progress       |
| Group Chat Features      | In Progress       |
| Background Customization | Implemented       |
| Translation              | Implemented       |
| UI Layer                 | Early development |

---

# How to Run

Basic steps:

1. Clone the repository:

```
git clone https://github.com/7tina/team-project.git
```

2. Open in IntelliJ
3. Load Maven project
4. Run main application entry point
