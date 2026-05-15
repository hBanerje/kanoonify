# Kanoonify

AI-powered legal awareness app for Indian citizens.

## Features

- Ask legal questions in natural language
- Get relevant Indian laws and fines instantly
- AI-powered situation analysis using OpenAI
- Category-based law browsing (Traffic, Criminal, Women Safety, Police Rights, Public Safety)
- Search-optimized keyword matching for real-life queries
- Works on both Android and iOS via Kotlin Multiplatform

## Tech Stack

- Kotlin Multiplatform (KMP)
- Jetpack Compose Multiplatform (shared UI)
- MVVM Architecture with StateFlow
- SQLDelight (local database)
- Ktor (networking)
- OpenAI API (AI integration)
- JSON-based law engine (80+ Indian laws)
- Compose Navigation

## Project Structure

```
composeApp/
  src/
    commonMain/    — Shared code (UI, ViewModels, Repository, Domain models)
    androidMain/   — Android-specific (Driver, MainActivity, assets)
    iosMain/       — iOS-specific (Driver, MainViewController)
iosApp/            — iOS Xcode entry point
```

## Build and Run

### Android

```shell
./gradlew :composeApp:assembleDebug
```

### iOS

Open the `/iosApp` directory in Xcode and run, or use the KMP run configuration in Android Studio / Fleet.

## Screenshots

(Coming soon)

## Goal

Help Indian citizens understand their legal rights during police interactions, traffic stops, and everyday situations — in simple, real-life language.
