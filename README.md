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

<img width="332" height="726" alt="Screenshot 2026-06-06 at 2 37 58 AM" src="https://github.com/user-attachments/assets/83ba946b-6169-47a6-a6a6-f10d35bb3257" />
<img width="257" height="563" alt="Screenshot 2026-06-06 at 2 39 35 AM" src="https://github.com/user-attachments/assets/0b13ba80-865b-45c7-9640-38fcfa14f7c0" />
<img width="263" height="564" alt="Screenshot 2026-06-06 at 2 40 06 AM" src="https://github.com/user-attachments/assets/c74c537b-7ffd-44d4-9a16-2510465eba90" />
<img width="262" height="567" alt="Screenshot 2026-06-06 at 2 40 51 AM" src="https://github.com/user-attachments/assets/a238b791-89b8-45c7-9cf9-56a2ce3ced57" />
<img width="262" height="565" alt="Screenshot 2026-06-06 at 2 40 29 AM" src="https://github.com/user-attachments/assets/f8637851-0256-4e3a-bbf9-a796521e85c0" />

<img width="260" height="559" alt="Screenshot 2026-06-06 at 2 41 14 AM" src="https://github.com/user-attachments/assets/37f749c3-4ad7-4645-bd32-967f15eb0933" />
<img width="261" height="558" alt="Screenshot 2026-06-06 at 2 41 36 AM" src="https://github.com/user-attachments/assets/7b7e1c1b-ec69-43e6-b03a-e669b2cdb732" />
<img width="262" height="565" alt="Screenshot 2026-06-06 at 2 42 09 AM" src="https://github.com/user-attachments/assets/50e48dd1-a84e-4e3f-8b84-5d29cf791065" />

<img width="257" height="559" alt="Screenshot 2026-06-06 at 2 42 38 AM" src="https://github.com/user-attachments/assets/96384ec8-6505-464d-8be6-d21fccaf7c30" />
<img width="263" height="563" alt="Screenshot 2026-06-06 at 2 43 33 AM" src="https://github.com/user-attachments/assets/e7ec0059-9bba-4166-b032-b85d46d4a432" />
<img width="263" height="569" alt="Screenshot 2026-06-06 at 2 42 53 AM" src="https://github.com/user-attachments/assets/16971e05-0fa1-4baf-8648-e3c5adf2c5a5" />

<img width="261" height="561" alt="Screenshot 2026-06-06 at 2 44 05 AM" src="https://github.com/user-attachments/assets/b40590a5-aaf6-4b7e-a421-9de4d11fae9e" />
<img width="261" height="564" alt="Screenshot 2026-06-06 at 2 44 27 AM" src="https://github.com/user-attachments/assets/fa8e8676-692b-4ee0-9b86-867ec4a3118d" />
<img width="258" height="563" alt="Screenshot 2026-06-06 at 2 44 51 AM" src="https://github.com/user-attachments/assets/2dd4f83c-9a12-46dd-a627-44d9223fbaab" />
<img width="260" height="557" alt="Screenshot 2026-06-06 at 2 46 33 AM" src="https://github.com/user-attachments/assets/539a3b2f-2a8c-4be9-bce1-d606fd7ccb69" />
<img width="259" height="562" alt="Screenshot 2026-06-06 at 2 46 10 AM" src="https://github.com/user-attachments/assets/df650443-61f9-465c-9a52-2dd3062b1d7e" />
<img width="260" height="564" alt="Screenshot 2026-06-06 at 2 45 54 AM" src="https://github.com/user-attachments/assets/22e9caec-be72-469f-b4be-3180282cfcfd" />
<img width="257" height="563" alt="Screenshot 2026-06-06 at 2 45 19 AM" src="https://github.com/user-attachments/assets/fd4f084e-e247-4d2b-9015-2d4c5c1ffcd0" />


## Goal

Help Indian citizens understand their legal rights during police interactions, traffic stops, and everyday situations — in simple, real-life language.
