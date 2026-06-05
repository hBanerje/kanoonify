# Kanoonify

> **AI-powered legal awareness app for Indian citizens — built once, ships to Android and iOS.**

Kanoonify helps people understand their legal rights, fines, and next steps during everyday situations — police stops, traffic challans, workplace harassment, consumer disputes, cyber crime — in plain language (English + Hinglish). It bundles 80 commonly-encountered Indian laws and 400+ Constitution articles offline, layers a Gemini-powered AI advisor on top, streams live legal news, and lets users consult verified lawyers behind a biometric lock — all from a single Kotlin Multiplatform codebase with a shared Compose Multiplatform UI.

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin_Multiplatform-2.3.21-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/lp/multiplatform/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.10.3-4285F4?logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Material 3](https://img.shields.io/badge/Material_3-1.10.0--alpha05-757575?logo=materialdesign&logoColor=white)](https://m3.material.io/)
[![Platforms](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS-blue)](#build-and-run)
[![License](https://img.shields.io/badge/License-See%20LICENSE-lightgrey)](LICENSE)

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Setup](#setup)
- [Build and Run](#build-and-run)
- [Security & Secret Management](#security--secret-management)
- [Datasets](#datasets)
- [Roadmap](#roadmap)

---

## Features

### 🤖 Ask Kanoonify — Legal Q&A
A chat-style assistant for plain-language legal questions. Type a natural query like *"no helmet fine india"* or *"police ne phone cheen liya"* and get a structured answer:
- **📋 Applicable law** — title, IPC/MV Act section, category, description
- **🛡 Your rights** — context-aware (traffic stop vs. police arrest vs. women safety)
- **✅ What you should do** — actionable next steps (e-Challan portal, FIR steps, lawyer contact)

Matching uses a hybrid keyword index across an 80-law corpus with both **English and Hinglish keywords** (e.g., `daaru peeke gaadi chalana`, `peechha karna crime hai`). For free-form questions the **Google Gemini 2.0 Flash** API explains the law in simple language.

### 📚 Browse Laws — Hierarchical Legal Library
Three-level drill-down: **Categories → Sub-categories → Laws → Detail**.
- **5 top-level categories**: Traffic Rules, Criminal, Women Safety, Police Rights, Public Safety
- **80 curated laws** covering MV Act, IPC, CrPC, POSH, PCA, IT Act, NDPS, COTPA, RTI, Consumer Protection, and more
- **Law detail view** shows description, exact punishment, applicable section, and category badge
- **Bookmark any law** to your personal library

### 🏛 Constitution of India — All Articles
A searchable, scrollable index of **400+ Constitutional articles** with title, subtitle, and a plain-English explanation.
- **Fast in-memory search** across article number, title and body
- **"🤖 Explain with AI"** button on each article — sends the text to Gemini for a layperson-friendly breakdown
- Save articles to your library for quick recall

### ⚖️ Consult a Lawyer — Verified Directory
- **12 verified lawyers** with specialisation, experience, rating, language, location, fee, and online status
- **Search & filter** by name, specialisation, or language
- **🔒 Biometric-gated profile view** — `BiometricPrompt` (Android `BIOMETRIC_STRONG | BIOMETRIC_WEAK | DEVICE_CREDENTIAL`) and `LAContext` (iOS Face ID / Touch ID) must succeed before personal contact details unlock
- **Chat screen** with typing indicator, message bubbles and per-lawyer conversation state

### 📰 Legal & Current Affairs News
Live curated headlines from **NewsAPI.org**, organised into 11 categories (Latest, Politics, Parliament, Law, Finance, Tech, Business, Sports, India, World, Corporate).
- **Smart category mapping** — native NewsAPI categories use `/v2/top-headlines`; Law / Politics / Parliament / Corporate route to `/v2/everything` with curated boolean queries (`India AND ("Supreme Court" OR "High Court" OR judgment)`)
- **Offline-first** with SQLDelight cache + bundled sample fallback — feed is **never empty** even on a fresh clone with no API key
- **Pull-to-refresh**, **debounced search** (300 ms), **save / share / open original** actions
- **Recent searches** persisted across launches; per-article bookmark indicator updates in real-time across screens via reactive `Flow`

### 🔍 Global Search
A dedicated tab to search across laws, articles, rights, and lawyers, with **recent search history**, **popular topics**, and **trending now** carousels.

### 📌 Saved / My Library
A unified library of bookmarked items, filterable by type (All / Laws / Articles / AI / News / Notes). Bookmarks survive app restarts via SQLDelight; saved state is observed reactively so the bookmark icon updates everywhere instantly.

### 👤 Profile
- User stats (searches, saved, COI reads, consultations)
- Settings (notifications, language, theme, biometric lock, privacy)
- Security (Face ID / Fingerprint, secure documents, app lock)
- Legal & support (privacy policy, terms, contact, about)
- Premium upsell card

### 🎨 Premium UI Layer
A custom Compose component library: animated gradient borders, glow buttons, floating orb backgrounds, shimmer placeholders, typing-text effects, premium glass cards, neon trending chips, monogram avatars, segmented filter chips — built entirely with Compose Multiplatform primitives so they render identically on Android and iOS.

### 🌐 Platform Parity
A single shared UI (`commonMain`) renders on:
- **Android** (Material You + system status/nav bar padding)
- **iOS** (via `ComposeUIViewController`, with native Face ID / Touch ID, native share sheet, native URL handler)

Platform-specific behaviour (URL opening, biometric prompt, JSON asset loading, SQLite driver) is bridged with Kotlin **`expect`/`actual`** declarations — no platform code leaks into the shared layer.

---

## Tech Stack

### Languages & Build
| | |
|---|---|
| **Kotlin** | 2.3.21 (with K2 compiler) |
| **Swift** | iOS app entry point (`iOSApp.swift`, `ContentView.swift`) |
| **JVM target** | 11 |
| **Gradle** | 8.14.3 (Kotlin DSL + Version Catalog) |
| **Android Gradle Plugin** | 8.11.2 |
| **Android SDK** | `compileSdk 36`, `minSdk 24`, `targetSdk 36` |

### Multiplatform & UI
| Library | Version | Purpose |
|---|---|---|
| Kotlin Multiplatform | 2.3.21 | Shared business logic across Android + iOS (`iosArm64`, `iosSimulatorArm64`) |
| Compose Multiplatform | 1.10.3 | Shared declarative UI |
| Compose Material 3 | 1.10.0-alpha05 | Material You components |
| Compose Foundation / UI / Runtime | 1.10.3 | Layout, animation, drawing |
| Compose Components Resources | 1.10.3 | Multiplatform string / drawable resources |
| Compose Navigation (JetBrains) | 2.9.2 | Type-safe routes with `@Serializable` |
| AndroidX Lifecycle ViewModel-Compose | 2.10.0 | `StateFlow` + `collectAsState` |
| AndroidX Lifecycle Runtime-Compose | 2.10.0 | Lifecycle-aware composables |
| Compose Icons (Tabler + Feather) | 1.1.1 | KMP-pure icon set |

### Architecture & State
- **MVVM** with `StateFlow` / `SharedFlow` for one-way data flow
- **Clean Architecture** layering — `domain` (pure Kotlin models) → `data` (sources, mappers, repository) → `presentation` (Compose screens, ViewModels, state)
- **Repository pattern** with primary/fallback data sources and offline cache
- **Compose Navigation** with **Base64-encoded typed payloads** for compound route arguments

### Networking
| | |
|---|---|
| **Ktor Client** | 3.4.3 (core + content-negotiation + JSON) |
| **Engines** | CIO (commonMain), Android (androidMain), Darwin (iosMain) |
| **Serialization** | kotlinx-serialization-json 1.11.0 |
| **APIs** | Google Gemini 2.0 Flash (legal explanations) · NewsAPI.org v2 (live news) |
| **Auth** | `X-Api-Key` header for NewsAPI (never sent as query param) |
| **Concurrency** | Coroutines + Flow throughout |

### Persistence
- **SQLDelight 2.0.2** — type-safe SQL with generated Kotlin APIs
  - `android-driver` for Android
  - `native-driver` for iOS (links system `-lsqlite3`)
  - `coroutines-extensions` for reactive queries
- **Schema** (`KanoonifyDatabase`):
  - `Law` — local copy of the 80-law dataset
  - `CachedArticle` + `SavedArticle` + `RecentNewsSearch` for the news module (with indexes on `category`, `publishedAt`, `savedAt`)

### Authentication & Security
- **AndroidX Biometric** 1.2.0-alpha05 — `BiometricPrompt` with `BIOMETRIC_STRONG | BIOMETRIC_WEAK | DEVICE_CREDENTIAL` fallback
- **AndroidX Fragment KTX** 1.8.5 — `FragmentActivity` host (required by `BiometricPrompt`)
- **iOS** — `LAContext` Face ID / Touch ID via Kotlin/Native interop
- **Secret management** — keys live in `local.properties` (gitignored), injected at build time into a generated `BuildSecrets.kt` (never committed)

### Android-Specific
- AndroidX Activity Compose 1.13.0
- Custom `JsonLoader` (reads `assets/laws.json` + `coi_articles.json`)
- `FragmentActivity` host (`MainActivity`) for biometric integration

### iOS-Specific
- Xcode project (`iosApp.xcodeproj`) with **SwiftUI** entry hosting `ComposeView`
- `ComposeUIViewController` bridge
- Static framework `ComposeApp` consumed by Xcode
- Ktor Darwin engine + SQLDelight Native driver

### Testing & Tooling
- `kotlin-test` (commonTest)
- JUnit 4.13.2 (Android unit tests)
- Compose UI Tooling + Preview

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       PRESENTATION                          │
│  Compose Multiplatform screens · ViewModels · UI state      │
│  (StateFlow + SharedFlow events)                            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                         DOMAIN                              │
│  Pure Kotlin models · Use-cases · Auth contracts            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                          DATA                               │
│  Repositories (primary + fallback + cache strategy)         │
│  ├── remote  · Ktor clients · DTO ↔ Domain mappers          │
│  ├── local   · SQLDelight · JSON asset loaders              │
│  └── auth    · Biometric repository                         │
└─────────────────────────────────────────────────────────────┘
                         │
        ┌────────────────┴───────────────────────┐
        │                                        │
┌───────▼─────────┐                    ┌─────────▼──────────┐
│   androidMain   │                    │      iosMain       │
│  expect/actual  │                    │   expect/actual    │
│  • SQLDelight   │                    │  • SQLDelight      │
│    AndroidDriver│                    │    NativeDriver    │
│  • Ktor Android │                    │  • Ktor Darwin     │
│  • BiometricPrm │                    │  • LAContext       │
│  • Intent share │                    │  • UIActivityVC    │
└─────────────────┘                    └────────────────────┘
```

**Key principles**
- One ViewModel per screen, no global state singletons
- Repositories own retry / cache / fallback policy — data sources are dumb
- All errors propagate up; the UI decides whether to show / retry / degrade
- Zero platform code in `commonMain` — bridged via `expect`/`actual`

---

## Project Structure

```
Kanoonify/
├── composeApp/
│   ├── build.gradle.kts          ← generateBuildSecrets task lives here
│   └── src/
│       ├── commonMain/
│       │   ├── composeResources/ ← strings.xml + drawables shared across platforms
│       │   ├── sqldelight/       ← Law.sq + News.sq schemas
│       │   └── kotlin/com/multiplatform/kanoonify/
│       │       ├── App.kt
│       │       ├── data/
│       │       │   ├── auth/             ← BiometricRepository
│       │       │   ├── datasource/
│       │       │   ├── local/            ← JsonLoader (expect), LawDataSource, COIDataSource
│       │       │   ├── mapper/           ← LawMapper
│       │       │   ├── remote/           ← AiService (Gemini), OpenAIService stub
│       │       │   └── repository/       ← LawRepository
│       │       ├── domain/
│       │       │   ├── auth/             ← BiometricAuthenticator, BiometricResult
│       │       │   └── model/            ← Law, LawItem, Lawyer, Article, AskAnswer, ChatMessage…
│       │       ├── db/                   ← DatabaseHelper, DatabaseDriverFactory (expect)
│       │       ├── news/                 ← Full self-contained feature module
│       │       │   ├── data/
│       │       │   │   ├── datasource/   ← Remote, Sample, interface
│       │       │   │   ├── local/        ← NewsCache (SQLDelight)
│       │       │   │   ├── mapper/       ← NewsApiMapper
│       │       │   │   ├── remote/       ← NewsApiService, NewsApiModels (DTOs)
│       │       │   │   └── repository/   ← NewsRepository
│       │       │   ├── domain/model/     ← NewsArticle, NewsCategory
│       │       │   ├── platform/         ← UrlOpener (expect)
│       │       │   └── presentation/
│       │       │       ├── components/   ← NewsCard, NewsImage, SaveButton, CategoryChip…
│       │       │       ├── screens/      ← NewsFeedScreen, NewsSearchScreen, NewsDetailScreen
│       │       │       ├── state/        ← NewsState, NewsUiEvent
│       │       │       └── viewmodel/    ← NewsViewModel
│       │       ├── platform/auth/        ← PlatformBiometricAuth (expect)
│       │       ├── presentation/
│       │       │   ├── screens/
│       │       │   │   ├── navigation/   ← AppNavGraph + @Serializable routes
│       │       │   │   ├── screens/      ← Splash, Landing, Ask, Search, Saved, Profile,
│       │       │   │   │                   Categories, SubCategory, LawList, LawDetail,
│       │       │   │   │                   Laws, COI, COIDetail,
│       │       │   │   │                   LawyerList, LawyerProfile, LawyerChat
│       │       │   │   ├── viewmodel/    ← One VM per screen
│       │       │   │   └── components/
│       │       │   ├── theme/            ← KanoonifyTheme, Color, Dimens, Type, PremiumColors
│       │       │   └── ui/components/    ← 30+ shared design-system widgets
│       │       └── utils/                ← SystemClock (expect)
│       ├── androidMain/
│       │   ├── AndroidManifest.xml       ← INTERNET, USE_BIOMETRIC, USE_FINGERPRINT
│       │   ├── assets/                   ← laws.json, coi_articles.json
│       │   ├── res/                      ← Launcher icons, themes
│       │   └── kotlin/                   ← MainActivity, DatabaseDriverFactory (actual),
│       │                                   JsonLoader (actual), UrlOpener.android,
│       │                                   PlatformBiometricAuth.android, BiometricActivityHolder
│       └── iosMain/
│           └── kotlin/                   ← MainViewController, DatabaseDriverFactory (actual),
│                                           JsonLoader (actual), UrlOpener.ios,
│                                           PlatformBiometricAuth.ios (LAContext)
├── iosApp/
│   ├── iosApp.xcodeproj/
│   └── iosApp/
│       ├── iOSApp.swift                  ← @main entry
│       ├── ContentView.swift             ← Hosts ComposeView
│       ├── Info.plist
│       ├── laws.json                     ← iOS bundle copy
│       └── coi_articles.json             ← iOS bundle copy
├── gradle/
│   ├── libs.versions.toml                ← Single source of truth for all versions
│   └── wrapper/
├── build.gradle.kts                      ← Root
├── settings.gradle.kts
├── local.properties                      ← gitignored (SDK path + secrets)
├── local.properties.example              ← Committed template
└── README.md
```

---

## Setup

### Prerequisites
- **JDK 17+**
- **Android Studio** (Iguana or newer) **or** IntelliJ IDEA / Fleet with KMP plugin
- **Xcode 15+** (for iOS builds — macOS only)
- **Android SDK** with build-tools for API 36

### 1. Clone & seed configuration

```shell
git clone <your-fork-url> Kanoonify
cd Kanoonify
cp local.properties.example local.properties
```

### 2. Fill in `local.properties`

| Key | Required? | What it does |
|---|---|---|
| `sdk.dir` | ✅ (Android) | Absolute path to your local Android SDK |
| `NEWS_API_KEY` | ⚪ Optional | NewsAPI.org developer key. Without it the News tab still works — it falls back to bundled sample articles. Get a free key at <https://newsapi.org/register>. |

> You can also supply `NEWS_API_KEY` via an environment variable instead of editing `local.properties` (useful for CI):
> ```shell
> NEWS_API_KEY=xxxxxxxx ./gradlew :composeApp:assembleDebug
> ```

---

## Build and Run

### Android

```shell
./gradlew :composeApp:assembleDebug
# install on a connected device/emulator
./gradlew :composeApp:installDebug
```

Or just hit **▶ Run** in Android Studio with the `composeApp` configuration selected.

### iOS

1. Open `iosApp/iosApp.xcodeproj` in **Xcode**.
2. Select a simulator (iPhone 15+) or a paired device.
3. Press **⌘R**.

Alternatively, use the **iOS Application** run configuration in Android Studio / Fleet with the KMP plugin.

The shared Kotlin framework (`ComposeApp.framework`) is built automatically by Gradle and linked into the Xcode project; you don't need to run a separate Gradle command first.

---

## Security & Secret Management

Kanoonify uses **build-time secret injection** so API keys never enter source control:

```
local.properties          (gitignored)
        │
        ▼  read by Gradle
build.gradle.kts → generateBuildSecrets task
        │
        ▼  writes
build/generated/secrets/commonMain/kotlin/com/multiplatform/kanoonify/BuildSecrets.kt
        │
        ▼  imported by
AppNavGraph.kt → NewsApiService(apiKey = BuildSecrets.NEWS_API_KEY)
        │
        ▼  sent over the wire as
HTTP header: X-Api-Key: ********    (never as URL query param)
```

**Properties of this approach:**
- Keys live in `local.properties` (already in `.gitignore`)
- Generated `BuildSecrets.kt` is under `build/` — also gitignored
- CI / GitHub Actions can inject via env var without any file (`NEWS_API_KEY=… ./gradlew …`)
- Works for **both Android and iOS** (generated into `commonMain`)
-  Empty key > app skips remote calls entirely and falls back to bundled data — no 401s, no broken UI
- Key sent via `X-Api-Key` header, not URL — invisible to proxies, logs, crash reports

---

## Datasets

| Dataset | Path | Size | Format |
|---|---|---|---|
| **Laws** | `composeApp/src/androidMain/assets/laws.json` + `iosApp/iosApp/laws.json` | **80 laws** | `id`, `title`, `category`, `description`, `punishment`, `keywords[]` |
| **Constitution of India** | `iosApp/iosApp/coi_articles.json` | **400+ articles** | `id`, `title`, `subtitle`, `description` |

The law dataset covers MV Act (driving offences, accidents, registration), IPC (theft, robbery, assault, murder, kidnapping, defamation), CrPC (FIR, bail, arrest rights), POSH Act, Domestic Violence Act, Dowry Prohibition Act, NDPS, COTPA, IT Act (cyber crime, hacking, obscenity), Consumer Protection Act, RTI Act, Constitution Articles 14/19/20/21/22, Prevention of Corruption Act, SC/ST Atrocities Act, and more — with both **English and Hinglish keywords** for natural-language matching.

---

## Roadmap

- [ ] User accounts & cross-device sync (Saved Library, history)
- [ ] State-language localisation (Hindi, Marathi, Tamil, Bengali, Telugu)
- [ ] Voice input for Ask Kanoonify
- [ ] Lawyer chat with real-time messaging (currently mocked)
- [ ] PDF export for saved laws & AI conversations
- [ ] Push notifications for legal news bookmarks
- [ ] Premium tier with unlimited AI queries
- [ ] Wear OS / watchOS quick-rights companion

---

## Goal

Help every Indian citizen understand their legal rights during police interactions, traffic stops, workplace situations, and everyday disputes — in simple, real-life language they actually speak.

> *"Kanoon sirf bade logon ke liye nahi hota."*


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
