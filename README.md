<h1>GizmoPoker</h1>

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-orange)
[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-blue)](https://kotlinlang.org/docs/reference/multiplatform.html)

![Platform](https://img.shields.io/badge/Android-3aab58)
![Platform](https://img.shields.io/badge/Desktop-097cd5)
![Platform](https://img.shields.io/badge/IOS-d32408)
![Platform](https://img.shields.io/badge/WasmJS-f7e025)

A Kotlin Multiplatform poker session tracker with support for Android, iOS, Desktop, and a backend server.

## Screenshots

| Sessions Screen | Charts Screen | News Feed Screen |
|:---:|:---:|:---:|
| <img src="assets/sessions.png" height="450" alt="Sessions Screen" /> | <img src="assets/Charts.png" height="450" alt="Charts Screen" /> | <img src="assets/Feed.png" height="450" alt="News Feed Screen" /> |

## Tech Stack

### Frontend
- **Kotlin Multiplatform** 2.2.20
- **Compose Multiplatform** 1.9.4
- **Ktor Client** 3.3.1 for API calls
- **Coil 3** for image loading
- **Koin** 4.1.1 for dependency injection
- **SQLDelight** 2.1.0 for local database
- **kotlinx-datetime** for date/time handling

### Backend (Server)
- **Ktor Server** 3.3.1
- **jOOQ** 3.20.8 for type-safe SQL
- **Flyway** for database migrations
- **SQLite** as the database
- **JWT** for authentication

## Project Structure

```
├── build-logic/         # Gradle convention plugins
│   └── convention/      # Shared build configuration
├── core/                # Core shared modules
│   ├── core-common/     # Utilities, extensions, base classes
│   ├── core-network/    # API client, network utilities
│   ├── core-database/   # SQLDelight database layer
│   ├── core-preferences/# Settings and preferences
│   ├── core-di/         # Koin DI setup
│   ├── core-ui/         # Theme, design system, shared components
│   └── core-resources/  # Shared resources (images, strings)
├── feature/             # Feature modules
│   ├── feature-auth/    # Login, signup, authentication
│   ├── feature-tracker/ # Session tracking
│   ├── feature-feed/    # News feed
│   └── feature-cards/   # Equity calculator, card games
├── composeApp/          # Main application (umbrella module)
│   ├── androidMain/     # Android-specific code
│   ├── desktopMain/     # Desktop-specific code
│   └── iosMain/         # iOS framework (GizmoCore)
├── server/              # Ktor backend server
├── common/              # Shared DTOs and models
├── evaluator/           # Poker hand evaluation logic
└── iosApp/              # iOS app wrapper
```

## Module Architecture

![Architecture Diagram](assets/architecture_diagram.png)

The project uses a **multi-module architecture** with convention plugins for consistent build configuration:

- **Convention Plugins**: `gizmo.kmp.library`, `gizmo.compose`, `gizmo.feature`, `gizmo.koin.ksp`
- **Core Modules**: Shared infrastructure used by all feature modules
- **Feature Modules**: Self-contained features with their own domain, data, and presentation layers
- **App Module**: Aggregates all modules and provides platform-specific entry points

## Features

- Track poker sessions with detailed statistics
- Multi-platform support (Android, iOS, Desktop)
- News feed with poker articles and strategy content
- User authentication with JWT
- Offline-first architecture with SQLDelight
- Type-safe database queries with jOOQ

