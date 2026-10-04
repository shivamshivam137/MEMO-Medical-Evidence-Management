# MEMO — Medical Evidence Management & Organization
> *Every Report. One Medical Memory.*

[![Android](https://img.shields.io/badge/Platform-Android_14-teal.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin_1.9-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack_Compose_Material3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM_Repository-green.svg)](https://developer.android.com/topic/architecture)

---

## 1. Project Overview

**MEMO** is an intelligent medical evidence aggregator designed to organize personal medical reports into a structured, searchable medical history.

### Current Phase: Frontend Prototype + DevOps Demonstration
- **Target:** Android Application Prototype (Kotlin, Jetpack Compose, Material 3, MVVM).
- **Data Source:** In-Memory Typed Repository with 12 Longitudinal Fictional Patient Records for *"Shivam Singh"*.
- **DevOps:** Jira Scrum Board, Git-Flow, Dockerized Build Tooling, and Automated Jenkins CI/CD.
- **Future Integration:** Architecture built on clean Repository interfaces (`IMemoRepository`) designed for zero-refactor swap to FastAPI + Supabase backend.

---

## 2. Architecture & Design System

- **MVVM Pattern:** Strict separation of UI (Jetpack Compose) and state management (StateFlow ViewModels).
- **Repository Abstraction:** `IMemoRepository` provides decoupling between the UI layer and data providers (`MockMemoRepository` today, `RetrofitRepository` later).
- **Design Tokens:**
  - Primary Brand Teal: `#0D9488`
  - Dark Surface / Slate: `#0F172A`
  - Surface Background: `#FAFBFC`
  - Warning / Alert: `#F59E0B`
  - Critical / Abnormal: `#EF4444`

---

## 3. Key Features Demonstrated

1. **Authentication:** Quick "Continue with Demo" credential auto-fill for instant evaluation.
2. **Executive Dashboard:** Health summary metrics, recent report highlights, and quick upload FAB.
3. **Reports Filter:** Interactive filter chips (Blood, Urine, Imaging, Prescription) with real-time query updates.
4. **Report Detail View:** Clean extracted parameters table with reference ranges, abnormal indicators, and medical extraction disclaimer.
5. **Medical Timeline:** Chronological timeline grouped by year and month with vertical connectors.
6. **Local Search Engine:** Instant substring matching across report titles, hospital names, and lab parameters.
7. **Simulated Ingest & OCR:** 3-step simulated extraction pipeline with animated feedback, adding a new record dynamically to the in-memory repository.

---

## 4. How to Build & Run

### Prerequisites
- Android Studio Iguana+ (or newer) with JDK 17
- Android SDK 34 (Android 14)

### Local Build
```bash
cd android
./gradlew assembleDebug
```
The generated APK will be located at:
`android/app/build/outputs/apk/debug/app-debug.apk`

---

## 5. Documentation
- [FRONTEND_PROTOTYPE_PLAN.md](docs/FRONTEND_PROTOTYPE_PLAN.md) — Comprehensive prototype execution plan
- [PRE_DEVELOPMENT_SETUP_CHECKLIST.md](docs/PRE_DEVELOPMENT_SETUP_CHECKLIST.md) — Pre-development setup checklist
- [DEVELOPMENT_PLAN.md](docs/DEVELOPMENT_PLAN.md) — Full-stack canonical roadmap
