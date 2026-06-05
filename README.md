# Kanoonify

AI-powered legal awareness app for Indian citizens.

## Features

- Ask legal questions in natural language
- Get relevant Indian laws and fines instantly
- AI-powered situation analysis using OpenAI
- Category-based law browsing (Traffic, Criminal, Women Safety, Police Rights, Public Safety)
- Search-optimized keyword matching for real-life queries
- **Live legal & current-affairs news** with offline cache, pull-to-refresh, search and bookmarks
- Works on both Android and iOS via Kotlin Multiplatform

## Tech Stack

- Kotlin Multiplatform (KMP)
- Jetpack Compose Multiplatform (shared UI)
- MVVM Architecture with StateFlow
- SQLDelight (local database)
- Ktor (networking — NewsAPI.org & OpenAI integrations)
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

## Setup

After cloning the repo, copy the template config file and fill in your values:

```shell
cp local.properties.example local.properties
```

Then edit `local.properties`:

| Key | Required? | What it does |
|---|---|---|
| `sdk.dir` |  (Android) | Path to your local Android SDK. |
| `NEWS_API_KEY` | Optional | NewsAPI.org developer key. Without it the News tab still works but shows bundled sample articles instead of live headlines. Get a free key at <https://newsapi.org/register>. |

`local.properties` is gitignored — your keys never leave your machine. They're read at build time and baked into a generated Kotlin file under `composeApp/build/generated/secrets/` (also gitignored). CI builds can supply the same values via environment variables:

```shell
NEWS_API_KEY=xxxxxxxx ./gradlew :composeApp:assembleDebug
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
