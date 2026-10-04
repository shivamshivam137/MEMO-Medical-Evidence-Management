# How to Clone, Setup, & Run MEMO

**Project:** MEMO — Medical Evidence Management & Organization  
**Tagline:** *Every Report. One Medical Memory.*  
**Repository:** [https://github.com/shivamshivam137/MEMO-Medical-Evidence-Management](https://github.com/shivamshivam137/MEMO-Medical-Evidence-Management)  
**Scope:** Native Android App (Kotlin + Jetpack Compose) with CI/CD (Docker & Jenkins)

---

## Quick Navigation
1. [Prerequisites & Required Software](#1-prerequisites--required-software)
2. [Step 1: Clone the Repository](#step-1-clone-the-repository)
3. [Step 2: Run the Frontend App in Android Studio (Recommended)](#step-2-run-the-frontend-app-in-android-studio-recommended)
4. [Step 3: Run / Build from Terminal (PowerShell / Bash)](#step-3-run--build-from-terminal-powershell--bash)
5. [Step 4: Install the APK on an Android Phone](#step-4-install-the-apk-on-an-android-phone)
6. [Step 5: (Optional) Run the Jenkins CI/CD Pipeline in Docker](#step-5-optional-run-the-jenkins-cicd-pipeline-in-docker)
7. [App Features Walkthrough](#app-features-walkthrough)
8. [Troubleshooting & FAQs](#troubleshooting--faqs)

---

## 1. Prerequisites & Required Software

Before you begin, install the following required software on your computer (Windows, macOS, or Linux):

### 1.1 Essential Requirements (To run the app)

| Tool | Recommended Version | Download Link & Notes | Verification Command |
|---|---|---|---|
| **Git** | 2.40+ | [git-scm.com](https://git-scm.com/downloads) | `git --version` |
| **Java JDK** | **OpenJDK 17** (Temurin / Oracle / Zulu) | [adoptium.net (Temurin 17 LTS)](https://adoptium.net/) | `java -version` |
| **Android Studio** | **Iguana (2023.2)**, **Koala**, or newer | [developer.android.com/studio](https://developer.android.com/studio) | Includes Android SDK & Emulator |

> [!IMPORTANT]
> **Java 17 is required**: Android Gradle Plugin 8.3+ requires Java 17. Verify your terminal reports version `17.x` when you run `java -version`. Ensure `JAVA_HOME` environment variable points to your JDK 17 installation.

### 1.2 Android Studio Components to Install
During the Android Studio installation (or inside **Tools ➔ SDK Manager**):
1. **SDK Platforms**: Check **Android 14.0 ("UpsideDownCake") / API Level 34**.
2. **SDK Tools**: Check:
   - Android SDK Build-Tools 34 (34.0.0)
   - Android SDK Command-line Tools
   - Android Emulator
   - Android SDK Platform-Tools

### 1.3 Optional Requirements (For CI/CD Demo)
* **Docker Desktop**: [docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop/) (Required only if running the Jenkins pipeline or containerized builds).

---

## Step 1: Clone the Repository

Open your terminal (**PowerShell** on Windows, or **Terminal** on macOS/Linux) and run:

```bash
# 1. Clone the repository
git clone https://github.com/shivamshivam137/MEMO-Medical-Evidence-Management.git

# 2. Navigate into the cloned folder
cd MEMO-Medical-Evidence-Management

# 3. Switch to the active develop branch (contains latest features)
git checkout develop
```

Verify you see the following folder structure:
```text
MEMO-Medical-Evidence-Management/
├── android/                 <-- Native Android app code (Jetpack Compose)
│   ├── app/
│   │   ├── src/main/java/com/memo/app/   <-- UI, screens, models, viewmodels
│   │   └── build.gradle.kts
│   ├── gradlew / gradlew.bat
│   └── settings.gradle.kts
├── docs/                    <-- Project documentation & architecture
├── Dockerfile.build         <-- Containerized build configuration
├── Jenkinsfile              <-- Jenkins CI/CD pipeline script
└── HOW_TO_RUN.md
```

---

## Step 2: Run the Frontend App in Android Studio (Recommended)

This is the standard and most interactive way to view and interact with the app.

### 1. Open Android Studio
Launch Android Studio from your desktop or start menu.

### 2. Open the `android/` Directory
* On the welcome screen, click **Open** (or in menu bar: **File ➔ Open...**).
* **CRITICAL:** Browse into the cloned project and select the **`android`** subfolder:
  ```text
  .../MEMO-Medical-Evidence-Management/android
  ```
  *(⚠️ Do NOT select the outer root directory. Selecting the `android/` folder ensures Android Studio detects the Gradle build scripts properly).*

### 3. Wait for Gradle Sync
* Android Studio will automatically download Gradle 8.4 and all required Jetpack Compose libraries.
* This takes 1–3 minutes on the first run.
* Wait until the bottom status bar reads:  
  `Gradle sync finished in ...`

### 4. Create or Start an Android Emulator
* In Android Studio, click the **Device Manager** icon (phone icon on the top-right toolbar).
* If you do not have an emulator:
  1. Click **Create Device**.
  2. Select **Phone ➔ Pixel 7** or **Pixel 8**.
  3. Select **Release Name: UpsideDownCake (API 34)** or **Tiramisu (API 33)** and click Download if prompted.
  4. Click **Finish**.
* Click the **Play (▶)** button next to the device to boot the virtual phone.

*(Alternatively: Connect your physical Android phone using a USB cable, and enable **USB Debugging** in your phone's Developer Options).*

### 5. Launch the App
1. In the top toolbar, ensure **`app`** is selected in the run configuration dropdown and your emulator/phone is selected in the device dropdown.
2. Click the green **Run (▶)** button (or press `Shift + F10`).
3. Android Studio will compile the app and launch it on your emulator.

---

## Step 3: Run / Build from Terminal (PowerShell / Bash)

You can run tests and compile the installable APK without opening Android Studio GUI:

### On Windows (PowerShell):
```powershell
# 1. Move into the android subfolder
cd android

# 2. Run automated unit tests (JUnit 5 + Coroutines)
.\gradlew.bat test

# 3. Build the debug APK binary
.\gradlew.bat assembleDebug
```

### On macOS / Linux:
```bash
# 1. Move into the android subfolder
cd android

# 2. Ensure gradlew is executable
chmod +x gradlew

# 3. Run unit tests
./gradlew test

# 4. Build the debug APK binary
./gradlew assembleDebug
```

### Where to Find the Generated APK
Once compilation completes successfully (`BUILD SUCCESSFUL`), your APK is ready at:
```text
android/app/build/outputs/apk/debug/app-debug.apk
```

---

## Step 4: Install the APK on an Android Phone

### Option A: Using ADB (Android Debug Bridge)
If your phone is connected with USB Debugging enabled:
```bash
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

### Option B: Direct Phone Install
1. Copy `app-debug.apk` to your phone via USB cable, Google Drive, or WhatsApp.
2. Open the file on your Android phone.
3. If prompted, allow **"Install unknown apps"** from your file manager.
4. Tap **Install** and open **MEMO**.

---

## Step 5: (Optional) Run the Jenkins CI/CD Pipeline in Docker

If you want to demonstrate the automated CI/CD pipeline:

### 1. Start Docker Desktop
Make sure Docker Desktop is running on your machine.

### 2. Run the Jenkins Container
If you already created the container:
```powershell
docker start memo-jenkins
```

If setting up Jenkins from scratch:
```powershell
docker run -d `
  --name memo-jenkins `
  -p 8080:8080 -p 50000:50000 `
  -v jenkins_home:/var/jenkins_home `
  jenkins/jenkins:lts-jdk17
```

### 3. Open Jenkins
* Open your browser and navigate to: **[http://localhost:8080](http://localhost:8080)**.
* Log in with your admin credentials.
* Open the **`Memo-pipeline`** job and click **Build Now**.
* Jenkins will execute all 7 stages:
  1. **Checkout SCM** (from GitHub `develop`)
  2. **Prepare Environment** (JDK 17 + Gradle check)
  3. **Compile** (Kotlin source files)
  4. **Unit Tests** (Runs JUnit test suite)
  5. **Static Analysis (Lint)** (Android Lint report)
  6. **Assemble Debug APK** (Builds `app-debug.apk`)
  7. **Archive Build Artifacts** (Provides downloadable APK in Jenkins UI)

---

## App Features Walkthrough

Once the app opens on your phone or emulator, explore the full flow:

1. **Splash Screen:** Displays the clinical branding (*"MEMO — Every Report. One Medical Memory"*).
2. **Instant Demo Login:** On the login screen, click **"Continue with Demo (Shivam Singh)"** for 1-click access.
3. **Medical Dashboard:**
   - Review the metrics grid (12 Total Records, 6 Blood Tests, 2 Urine Tests, 2 Imaging, 2 Prescriptions).
   - Review the amber **Attention Alert** flagging biomarker anomalies.
4. **Filterable Reports Screen:**
   - Tap the **Reports** tab at the bottom.
   - Filter by categories (*Blood Test, Urine Test, Imaging, Prescription*).
5. **Report Detail Screen:**
   - Click on the **Comprehensive Metabolic & Glycemic Profile**.
   - Inspect the biomarker table with automatic reference range tagging:
     - `HbA1c: 5.7 %` — Marked **Elevated** (Amber)
     - `Fasting Blood Glucose: 98 mg/dL` — Marked **Normal** (Green)
     - `LDL Cholesterol: 108 mg/dL` — Marked **Elevated** (Amber)
6. **Longitudinal Timeline:**
   - Tap the **Timeline** tab to view your chronological health history chronologically linked by date.
7. **Clinical Search:**
   - Tap **Search** and search for *"HbA1c"* or *"Lipid"* to see instant substring matching.
8. **Simulated Ingest & OCR Extraction:**
   - Tap the **(+) Upload** button.
   - Select a sample report file and click **"Start Simulated Ingest & Extraction"**.
   - Watch the live 4-stage pipeline (*Upload ➔ OCR ➔ Entity Extraction ➔ Structuring*).
   - View the newly created record live.

---

## Troubleshooting & FAQs

### Q1: "Could not find or load main class ... / Java version error"
* **Solution:** Make sure JDK 17 is installed and active. Check `java -version`. In Android Studio, go to **Settings (Preferences on Mac) ➔ Build, Execution, Deployment ➔ Build Tools ➔ Gradle** and ensure **Gradle JDK** is set to **Java 17**.

### Q2: Android Studio says "Project root doesn't contain a Gradle build"
* **Solution:** You opened the outer repo root folder. Close the project, click **Open**, and select the **`android`** subfolder specifically.

### Q3: "Building on the built-in node can be a security issue" in Jenkins
* **Solution:** This is a standard advisory warning from Jenkins meant for multi-user enterprise servers. In your local Docker environment, it is completely safe to ignore.

### Q4: Emulator does not start or runs very slowly
* **Solution:** Make sure Hardware Virtualization (VT-x / AMD-V) and Hyper-V / Windows Hypervisor Platform are enabled in Windows Features and your BIOS.

### Q5: Running `.\gradlew.bat` in PowerShell hangs on downloading Gradle
* **Solution:** On the very first run, Gradle needs to download its wrapper and dependencies (approx. 200MB). Allow a couple of minutes for the initial download to complete.
