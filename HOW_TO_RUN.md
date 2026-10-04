# How to Run & Demonstrate MEMO

**Project:** MEMO — Medical Evidence Management & Organization  
**Tagline:** *Every Report. One Medical Memory.*  
**Scope:** Android Frontend Prototype + DevOps Demonstration (Git, Jira, Jenkins, Docker)

---

## Table of Contents
1. [Prerequisites & System Requirements](#1-prerequisites--system-requirements)
2. [Method 1: Running the Android App (Android Studio)](#2-method-1-running-the-android-app-android-studio)
3. [Method 2: Command-Line Build (PowerShell)](#3-method-2-command-line-build-powershell)
4. [Method 3: Running Jenkins CI/CD in Docker](#4-method-3-running-jenkins-cicd-in-docker)
5. [Method 4: Containerized Build with Docker](#5-method-4-containerized-build-with-docker)
6. [Professor Demonstration Script (5–8 Minutes)](#6-professor-demonstration-script-58-minutes)
7. [Troubleshooting & Common Fixes](#7-troubleshooting--common-fixes)

---

## 1. Prerequisites & System Requirements

Before running the application, verify that your machine has:
* **Operating System:** Windows 10/11 (64-bit)
* **Java Development Kit:** OpenJDK 17 (verified via `java -version`)
* **Git:** Version 2.40+ (verified via `git --version`)
* **Android Studio:** Iguana (2023.2) or newer with Android SDK 34
* **Docker Desktop:** Version 4.28+ with WSL2 backend enabled

---

## 2. Method 1: Running the Android App (Android Studio)

This is the recommended method for live interactive demonstration.

### Step 1: Open the Project
1. Launch **Android Studio**.
2. Click **File ➔ Open...** (or click **Open** on the Welcome screen).
3. Select the **`android`** folder specifically:
   ```text
   c:\Users\singh\OneDrive\Desktop\SIH_Project\MEMO_WMAD_project\android
   ```
   *(Do NOT open the outer root directory; opening `android/` allows Android Studio to recognize the Gradle root).*

### Step 2: Sync Gradle
- Android Studio will automatically invoke the Gradle wrapper (Gradle 8.4) and download the necessary dependencies.
- Wait until the status bar at the bottom displays:
  ```text
  Gradle sync finished in ...
  ```

### Step 3: Launch an Android Device
* **Option A (Emulator):**
  1. Open the **Device Manager** in Android Studio (phone icon in the right toolbar).
  2. Click the **Play (▶)** button next to a virtual device (recommended: **Pixel 7 / 8 running API 33 or 34**).
* **Option B (Physical Phone):**
  1. Enable **Developer Options** and **USB Debugging** on your Android phone.
  2. Connect the phone via USB cable and select it from the device dropdown.

### Step 4: Run the Application
1. Click the green **Run (▶)** button in the top toolbar (or press `Shift + F10`).
2. The app will compile, install, and display the **MEMO Splash Screen**.

---

## 3. Method 2: Command-Line Build (PowerShell)

You can run automated test suites and compile installable APKs directly from PowerShell:

```powershell
# 1. Navigate to the android directory
cd c:\Users\singh\OneDrive\Desktop\SIH_Project\MEMO_WMAD_project\android

# 2. Execute unit tests
.\gradlew.bat test

# 3. Compile the debug APK
.\gradlew.bat assembleDebug
```

### Locating the Built APK
Once compilation completes, the APK is located at:
```text
android\app\build\outputs\apk\debug\app-debug.apk
```
You can install this APK onto any Android phone or drag-and-drop it directly into the running Android Emulator.

---

## 4. Method 3: Running Jenkins CI/CD in Docker

The project includes automated continuous integration using a containerized Jenkins server.

### Container Details
* **Container Name:** `memo-jenkins`
* **Port:** `8080`
* **URL:** [http://localhost:8080](http://localhost:8080)
* **Initial Admin Password:** `f07c7ccd16c342dba5a050a7c590707b`

### Starting the Jenkins Container (if stopped)
If the container is not running, execute:
```powershell
docker start memo-jenkins
```

To verify status:
```powershell
docker ps
```

### Accessing the Pipeline in Jenkins
1. Open your browser and go to: **[http://localhost:8080](http://localhost:8080)**.
2. Log in with your admin credentials.
3. Click on the **`Memo-pipeline`** job.
4. Click **Build Now** in the left-hand menu.
5. Jenkins executes the 7 stages from [Jenkinsfile](Jenkinsfile):
   - **Stage 1: Checkout SCM** — Clones code from GitHub (`develop` branch).
   - **Stage 2: Prepare Environment** — Checks Java 17 and Gradle permissions.
   - **Stage 3: Compile** — Compiles Kotlin source files.
   - **Stage 4: Unit Tests** — Executes JUnit test cases.
   - **Stage 5: Static Code Analysis (Lint)** — Runs Android Lint analysis.
   - **Stage 6: Assemble Debug APK** — Builds the binary APK.
   - **Stage 7: Archive Build Artifacts** — Stores `app-debug.apk` directly on Jenkins.

---

## 5. Method 4: Containerized Build with Docker

To demonstrate reproducible builds using Docker without installing Android SDK locally:

```powershell
# Navigate to the project root
cd c:\Users\singh\OneDrive\Desktop\SIH_Project\MEMO_WMAD_project

# Build the reproducible build image
docker build -t memo-android-builder -f Dockerfile.build .

# Run the build inside the container
docker run --rm -v ${PWD}/android:/workspace memo-android-builder ./gradlew assembleDebug
```

---

## 6. Professor Demonstration Script (5–8 Minutes)

Follow this exact flow during your academic presentation:

### Part 1: Software Engineering & DevOps Practices (~2 Minutes)

1. **Jira Project Management:**
   - Open your Jira Scrum Board in the browser.
   - Show **Sprint 1** with active user stories (`MEMO-1` through `MEMO-10`).
   - Highlight the **Acceptance Criteria** and **Definition of Done (DoD)**.
   - Point out the **Backlog** containing the pre-planned future epics (*FastAPI Backend, Supabase Auth, PaddleOCR, Cloud Deployment*).

2. **Git & GitHub Traceability:**
   - Open the GitHub repository: `shivamshivam137/MEMO-Medical-Evidence-Management`.
   - Show the branch structure: `main` (release) and `develop` (integration).
   - Show the commit log: point out how every commit message references a Jira issue ID (`feat(ui): ... [MEMO-4]`).

3. **Jenkins CI/CD Automation:**
   - Open [http://localhost:8080](http://localhost:8080).
   - Show the green **Stage View** of the 7-stage pipeline.
   - Point to the downloadable `app-debug.apk` archived artifact.

---

### Part 2: Interactive Application Demonstration (~5 Minutes)

Switch to the running Android app on the screen:

#### 1. Splash Screen
* Launch the app to show the clinical branding (*"MEMO — Every Report. One Medical Memory"*).

#### 2. Authentication
* Point out the login screen and click **"Continue with Demo (Aarav Mehta)"** for instant one-click sign in.

#### 3. Executive Medical Dashboard
* **Metrics Grid:** Point out the stat cards (*12 Total Records, 6 Blood Tests, 2 Urine Tests, 2 Imaging, 2 Prescriptions*).
* **Attention Alert:** Highlight the amber banner flagging that reports have values outside reference intervals.
* **Recent Reports:** Show the quick-access list.

#### 4. Filterable Medical Records List
* Tap the **Reports** tab at the bottom.
* Tap the category chips (**Blood Test**, **Urine Test**, **Prescription**, **Imaging**).
* Explain that filtering uses reactive Kotlin StateFlow without screen reloads.

#### 5. Report Detail Deep-Dive (Hero Feature)
* Open the **September 08, 2026: Comprehensive Metabolic & Glycemic Profile**.
* Show the **Extracted Lab Results Table**:
  - `HbA1c: 5.7 %` — Flagged with an amber **Elevated** tag (Ref: 4.0 – 5.6%).
  - `Fasting Blood Glucose: 98 mg/dL` — **Normal** (Ref: 70 – 99 mg/dL).
  - `LDL Cholesterol: 108 mg/dL` — **Elevated** (Ref: < 100 mg/dL).
* Highlight the **Medical Extraction Notice**: reminds patients that data was automatically digitized and to verify with originals.

#### 6. Longitudinal Timeline
* Tap the **Timeline** tab.
* Show the vertical connecting line indicators grouping records by month and year (from 2025 baseline through late 2026).

#### 7. Clinical Search Engine
* Tap the **Search** tab.
* Type **"HbA1c"** or tap the **"Lipid Profile"** suggestion chip.
* Show instant substring matching across tests, doctors, and facilities.

#### 8. Simulated Ingest & OCR Pipeline
* Tap the **Upload FAB (+)** or the **Upload** destination.
* Select `blood_report_cbc_novacare.pdf` from the test files.
* Click **"Start Simulated Ingest & Extraction"**.
* Watch the live 4-stage stepper:
  1. *Sandbox Upload*
  2. *OCR Processing & Layout Detection*
  3. *Clinical Entity & Biomarker Extraction*
  4. *Timeline Structuring*
* Click **"View Newly Ingested Record"** — the newly extracted record opens immediately and total count increments to 13.

---

### Part 3: Architecture Defense (~1 Minute)

Close with this architectural explanation:

> *"The app follows the **MVVM + Repository Pattern**. 
> All ViewModels depend strictly on the `IMemoRepository` interface. 
> For today's prototype, it is powered by `MockMemoRepository` backed by 12 curated clinical records. 
> In Phase 1, we will swap `MockMemoRepository` for `RetrofitMemoRepository` connecting to our FastAPI and Supabase backend. 
> Because of this interface seam, **the entire UI layer requires zero code modifications**."*

---

## 7. Troubleshooting & Common Fixes

| Issue | Cause | Fix |
|---|---|---|
| **Gradle Sync Fails in Android Studio** | Incorrect folder opened | Ensure you opened the `android/` subfolder, not the outer repository root. |
| **Jenkins Cannot Connect (localhost:8080)** | Container is stopped | Run `docker start memo-jenkins` in PowerShell. |
| **Android Emulator Lags** | Hardware virtualization disabled | Ensure Hyper-V and Virtual Machine Platform are enabled in Windows Features. |
| **Need Jenkins Admin Password Again** | Password forgotten | Run `docker exec memo-jenkins cat /var/jenkins_home/secrets/initialAdminPassword`. |
