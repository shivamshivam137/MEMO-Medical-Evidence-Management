# MEMO — Pre-Development Readiness & Setup Checklist

**Target Submission:** Tomorrow  
**Current Phase:** Frontend Prototype (Kotlin/Compose) + DevOps Demonstration (Git, Jira, Jenkins, Docker)  
**Role:** Senior Software Engineer & DevOps Lead  

---

## Table of Contents
1. [A. What You Need Before Starting](#a-what-you-need-before-starting)
2. [B. Git/GitHub Setup](#b-gitgithub-setup)
3. [C. Jira Setup](#c-jira-setup)
4. [D. Jira Content to Write](#d-jira-content-to-write)
5. [E. Jira Sprint Planning](#e-jira-sprint-planning)
6. [F. Android Development Environment](#f-android-development-environment)
7. [G. Antigravity AI Coding Agent Preparation](#g-antigravity-ai-coding-agent-preparation)
8. [H. Demo Data & Sample Files Preparation](#h-demo-data--sample-files-preparation)
9. [I. Docker Setup](#i-docker-setup)
10. [J. Jenkins Setup](#j-jenkins-setup)
11. [K. GitHub ↔ Jira ↔ Jenkins Integration](#k-github--jira--jenkins-integration)
12. [L. Project Documentation (README.md Template)](#l-project-documentation-readmemd-template)
13. [M. Professor Demo Evidence Checklist](#m-professor-demo-evidence-checklist)
14. [N. Exact Step-by-Step Setup Order](#n-exact-step-by-step-setup-order)
15. [O. Final Pre-Development Checklist](#o-final-pre-development-checklist)
16. [P. What You Should NOT Waste Time On Today](#p-what-you-should-not-waste-time-on-today)
17. [START CODING ONLY AFTER THESE ARE READY](#start-coding-only-after-these-are-ready-top-12-gating-items)

---

## A. What You Need Before Starting

Before running a single line of code or asking Antigravity to write UI components, verify that your workstation satisfies these prerequisites.

### 1. Hardware & System Requirements (Windows Laptop)
* **RAM:** Minimum 16 GB (recommended for running Android Studio/Emulator + Docker Desktop + IDE simultaneously).
* **Free Disk Space:** Minimum **25–30 GB free** on drive `C:\` (Android SDK, emulator system images, Gradle cache, and Docker images require substantial room).
* **Virtualization:** Ensure **Hyper-V** and **Virtual Machine Platform** (VT-x / AMD-V) are enabled in BIOS and Windows Features (required for both Android Emulator and Docker Desktop WSL2 backend).

### 2. Required Accounts & Credentials
* **GitHub Account:** Active, authenticated locally via Git CLI (SSH key or GitHub Personal Access Token / Git Credential Manager).
* **Atlassian/Jira Cloud Account:** Free tier (up to 10 users) is 100% sufficient.
* **Docker Desktop:** Free personal tier account (login optional, local daemon is what matters).

### 3. Software Installation Matrix
| Software / Tool | Minimum Version | Verified Command / Check | Purpose |
|---|---|---|---|
| **Git** | 2.40+ | `git --version` | Source control & commit traceability |
| **Android Studio** | Iguana / Jellyfish / Koala (2023.2+) | Open Studio Welcome Screen | IDE, Android SDK manager, Device Manager |
| **JDK (Java)** | JDK 17 (LTS) | `java -version` | Gradle 8.x + Android Gradle Plugin 8.x |
| **Docker Desktop** | 4.28+ with WSL2 | `docker info` | Containerized build environment demonstration |
| **Jenkins** | 2.440+ LTS | Port 8080 accessible | CI/CD automated pipeline demonstration |
| **PowerShell** | 7.x or 5.1 | `$PSVersionTable.PSVersion` | Script execution on Windows |

---

## B. Git/GitHub Setup

### 1. Repository Configuration
* **Repository Name:** `MEMO-Medical-Evidence-Management` (clear, academic, professional).
* **Visibility:** **Public** (makes linking Jira, viewing commits, and sharing with professors frictionless without managing organizational token permissions).
* **Creation Mode:** Create on GitHub as **EMPTY** (do **NOT** check "Initialize with README", `.gitignore`, or license). You will push your existing local workspace to avoid merge conflicts.

### 2. Comprehensive Android & Windows `.gitignore`
Place this `.gitignore` at the repository root before making the initial commit:

```gitignore
# Built application files
*.apk
*.aab
*.ap_

# Files for the ART/Dalvik VM
*.dex

# Java class files
*.class

# Generated files
bin/
gen/
out/
build/
*/build/

# Gradle files
.gradle/
build/
app/build/
.gradle-cache/

# Local configuration file (sdk.dir, etc.)
local.properties

# Android Studio / IntelliJ IDEA
.idea/
*.iml
*.iws
*.ipr
.idea/caches/
.idea/libraries/
.idea/modules.xml
.idea/workspace.xml

# Keystore files
*.jks
*.keystore

# Google Services (future Supabase / Firebase)
google-services.json

# OS-specific
.DS_Store
Thumbs.db
desktop.ini

# Scratch and Agent Artifacts
.gemini/
scratch/
```

### 3. Branching Strategy
You will demonstrate a professional **Git-Flow lite** model:

```
main (Production / Academic Release)
  ↑
develop (Integration Branch)
  ↑
feature/MEMO-XX-feature-name (Working Branches)
```

| Branch | Purpose | Protection Rule |
|---|---|---|
| `main` | Clean, stable release branch for the professor demo. Contains final tagged APK and documentation. | Protect against direct push; only PR merges from `develop`. |
| `develop` | Active integration branch. All feature branches merge here via Pull Request. | Default working branch during development. |
| `feature/MEMO-XX-*` | Short-lived branches where individual user stories are implemented. | Merged into `develop` and deleted. |

### 4. Step-by-Step Initial Git Commands (Run in Project Directory)
Execute in PowerShell inside `c:\Users\singh\OneDrive\Desktop\SIH_Project\MEMO_WMAD_project`:

```powershell
# 1. Initialize Git repository
git init

# 2. Add all existing documentation and .gitignore
git add .gitignore docs/ DEVELOPMENT_PLAN.md FRONTEND_PROTOTYPE_PLAN.md

# 3. Create initial commit
git commit -m "chore: initial project documentation and architecture baseline"

# 4. Set main branch
git branch -M main

# 5. Link to your remote GitHub repo (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/MEMO-Medical-Evidence-Management.git

# 6. Push main
git push -u origin main

# 7. Create and switch to develop branch
git checkout -b develop
git push -u origin develop
```

### 5. Recommended Repository Directory Structure
```text
MEMO_WMAD_project/
├── .gitignore
├── README.md
├── Jenkinsfile
├── Dockerfile.build
├── docs/
│   ├── DEVELOPMENT_PLAN.md           (Full-stack canonical roadmap)
│   ├── FRONTEND_PROTOTYPE_PLAN.md    (Frontend + DevOps execution plan)
│   ├── PRE_DEVELOPMENT_SETUP_CHECKLIST.md (This preparation guide)
│   └── demo_assets/                  (Sample medical files for upload demo)
└── android/                          (Android project root)
    ├── build.gradle.kts
    ├── settings.gradle.kts
    ├── gradle/
    ├── gradlew
    ├── gradlew.bat
    └── app/
        ├── build.gradle.kts
        └── src/
```

### 6. GitHub Issues & Projects Verdict
* **GitHub Issues:** **Do NOT use.** Because Jira is your primary project management tool, using GitHub Issues creates confusing duplication. Your commits and PRs will directly reference Jira IDs (`MEMO-XX`).
* **GitHub Projects:** **Avoid.** Having two Kanban boards (Jira + GitHub Projects) signals indecision. Use Jira for project tracking and GitHub strictly for version control and PR reviews.

---

## C. Jira Setup

### 1. Project Creation Details
* **Project Template:** **Scrum** (demonstrates Sprints, Velocity, Story Points, and Sprint Goals to your professor).
* **Project Name:** `MEMO - Medical Evidence Management`
* **Project Key:** `MEMO` (clean, 4 characters).
* **Board Columns:**
  `Backlog` ➔ `To Do` ➔ `In Progress` ➔ `Code Review` ➔ `Testing` ➔ `Done`

### 2. Epics to Create

#### ACTIVE EPICS (Current Frontend + DevOps Scope):
1. `MEMO-E1`: **DevOps & CI/CD Tooling** (Git flow, Docker build env, Jenkins pipeline).
2. `MEMO-E2`: **Design System & Navigation Shell** (Theme, typography, components, bottom navigation).
3. `MEMO-E3`: **Core Prototype UI & Mock Repository** (Dashboard, Reports List, Detail Views, Timeline).
4. `MEMO-E4`: **Interactive Simulated Workflows** (Search engine, Upload picker, Simulated OCR processing).

#### FUTURE / BACKLOG EPICS (Pre-created to demonstrate architectural maturity):
*Mark these with label `Phase-1-Backend` or `Future-Roadmap` in Jira:*
* `MEMO-E5`: *[FUTURE] FastAPI Microservices & Supabase Backend Integration*
* `MEMO-E6`: *[FUTURE] PaddleOCR Pipeline & Multi-Language Medical Information Extraction*
* `MEMO-E7`: *[FUTURE] RAG-Powered Longitudinal Medical Insights & Comparison*
* `MEMO-E8`: *[FUTURE] Cloud Infrastructure & Production Kubernetes Deployment*

---

### 3. User Stories Table (Ready for Jira Input)

| Key | Title | User Story Statement | Priority | Points | Acceptance Criteria | Subtasks |
|---|---|---|---|---|---|---|
| **MEMO-1** | Setup Android Compose Architecture & Dependencies | *As a developer, I want a configured Jetpack Compose project with Navigation and MVVM dependencies so development can proceed.* | Highest | 3 | Project compiles, Gradle builds cleanly with minSdk 26, targetSdk 34, Compose BOM linked. | 1. Initialize Android project<br>2. Configure `build.gradle.kts`<br>3. Verify build |
| **MEMO-2** | Medical Design Tokens & Navigation Shell | *As a user, I want a cohesive healthcare UI theme and bottom navigation bar so I can switch between records easily.* | High | 5 | Custom Slate & Teal palette, typography, 4-tab bottom navigation (`Dashboard`, `Reports`, `Timeline`, `Upload`). | 1. Implement `Color.kt` & `Theme.kt`<br>2. Build `BottomNavBar`<br>3. Setup `NavHost` |
| **MEMO-3** | In-Memory Medical Repository & Demo Dataset | *As an application, I need a typed Repository abstraction with 12 realistic clinical records so the UI functions without a backend.* | High | 5 | `IMemoRepository` interface defined; `MockMemoRepository` provides 12 fictional records for Aarav Mehta; supports search/filter. | 1. Define models (`MedicalReport`, `LabParameter`)<br>2. Implement `DemoDataSource`<br>3. Unit test repository |
| **MEMO-4** | Executive Medical Dashboard | *As a patient, I want to see my health summary, total records, and recent test alerts on launch.* | High | 5 | Shows user greeting, stat cards (Total Reports: 12, Pending: 0), recent 3 records, and quick upload FAB. | 1. Build `StatCard` composable<br>2. Build recent reports list<br>3. Connect to `DashboardViewModel` |
| **MEMO-5** | Filterable Medical Records List | *As a patient, I want to view all my lab and imaging reports with category filters so I can find records quickly.* | Medium | 5 | Displays list cards with report type badge, date, hospital; filter chips (All, Blood, Urine, Imaging). | 1. Build `ReportItemCard`<br>2. Add category filter chips<br>3. Connect to `ReportsViewModel` |
| **MEMO-6** | Report Detail View with Lab Extraction Display | *As a patient, I want to see extracted lab test values with normal reference ranges and an extraction disclaimer.* | High | 8 | Detail screen shows hospital metadata, parameters table (Name, Value, Unit, Status Badge: Normal/High), and disclaimer banner. | 1. Build parameters table<br>2. Build disclaimer card<br>3. Display simulated original document view |
| **MEMO-7** | Longitudinal Health Timeline | *As a doctor/patient, I want to view medical history grouped chronologically so health progression is clear.* | Medium | 5 | Vertical timeline grouped by Year/Month with connecting line indicator and report badges. | 1. Build timeline vertical node<br>2. Group demo records by date<br>3. Connect to `TimelineViewModel` |
| **MEMO-8** | Local Clinical Search Engine | *As a user, I want to search for test names (e.g., HbA1c), doctor names, or hospitals with instant result filtering.* | Medium | 3 | Real-time search query matches across report titles, extracted parameters, and hospital names. | 1. Build search bar with clear button<br>2. Implement search query debounce<br>3. Display filtered results |
| **MEMO-9** | Simulated Document Upload & OCR Flow | *As a patient, I want to pick a document and watch simulated OCR extraction so I understand the ingest workflow.* | High | 5 | System file picker opens; 3-second animated progress (`Scanning` ➔ `Extracting` ➔ `Complete`); new record appears in list. | 1. Connect file picker launcher<br>2. Build simulated stepper dialog<br>3. Append new mock report to repository |
| **MEMO-10** | CI/CD Pipeline & Docker Build Environment | *As a DevOps engineer, I want Jenkins to automatically compile, test, lint, and build the APK inside Docker.* | High | 5 | `Jenkinsfile` runs on commit; executes `./gradlew test` and `./gradlew assembleDebug`; archives `app-debug.apk`. | 1. Write `Dockerfile.build`<br>2. Create declarative `Jenkinsfile`<br>3. Execute successful build |

---

## D. Jira Content to Write

Copy these exact blocks into Jira:

### 1. Project Description
> **MEMO (Medical Evidence Management & Organization)** is an Android application designed to transform fragmented personal medical reports into a structured, longitudinal health timeline. This project phase implements the responsive Android frontend prototype in Jetpack Compose backed by an extensible Repository pattern and demonstrates a modern CI/CD deployment pipeline using Git, GitHub, Jenkins, and Docker.

### 2. Project Goal
> Deliver a fully demonstrable, interactive Android prototype featuring 12 longitudinal medical records with simulated OCR ingestion and automated Jenkins CI/CD artifact generation for academic evaluation.

### 3. Sprint 1 Goal
> Build and demonstrate the core MEMO user journey (Dashboard ➔ Filter ➔ Detail View with Lab Extraction ➔ Timeline ➔ Ingest Animation) and prove CI/CD pipeline automation with an archived debug APK.

### 4. Definition of Done (DoD)
> A user story is marked **Done** only when:
> 1. All acceptance criteria are verified on an Android device or emulator (API 34).
> 2. Code adheres to MVVM architecture with zero UI business logic.
> 3. Repository interfaces remain decoupled from mock data.
> 4. Associated unit tests pass cleanly (`./gradlew test`).
> 5. Android Lint reports zero fatal errors (`./gradlew lintDebug`).
> 6. Code is committed with conventional commit message referencing the Jira ID and merged to `develop` via Pull Request.

---

## E. Jira Sprint Planning

* **Sprint Name:** `MEMO Sprint 1 - Frontend Prototype & DevOps Demo`
* **Sprint Duration:** 1 Day / 1 Week (select 1 week in Jira dropdown, set dates for today/tomorrow).
* **Issues in Active Sprint:** `MEMO-1` through `MEMO-10` (Total: 49 Story Points).
* **Issues Remaining in Backlog:** `MEMO-E5`, `MEMO-E6`, `MEMO-E7`, `MEMO-E8` (the future backend and AI epics).
* **Sprint Status:** Click **Start Sprint** *before* you push the first feature branch.

---

## F. Android Development Environment

Verify these exact settings on your development machine before writing code:

### 1. Software Versions & Verification
* **Android Studio:** Open `Help -> About`. Verify version 2023.2 (Iguana) or newer.
* **JDK:** JDK 17 is mandatory. Check in terminal:
  ```powershell
  java -version
  # Must output: openjdk version "17.0.x" or similar
  ```
* **Android SDK:** Open Android Studio `Settings -> Languages & Frameworks -> Android SDK`:
  * **SDK Platforms:** Check `Android 14.0 ("UpsideDownCake")` (API level 34).
  * **SDK Tools:** Check `Android SDK Build-Tools 34.0.0`, `Android SDK Command-line Tools (latest)`, `Android SDK Platform-Tools`, `Android Emulator`.
* **Environment Variables:**
  * `JAVA_HOME`: `C:\Program Files\Java\jdk-17` (or your Android Studio bundled `jbr` path).
  * `ANDROID_HOME`: `C:\Users\singh\AppData\Local\Android\Sdk`.
  * Add `%ANDROID_HOME%\platform-tools` to system `PATH`.

### 2. Project Configuration Baseline
When creating the Android project, enforce these exact configurations:
* **Project Directory:** `android/` inside the repository.
* **Application / Package ID:** `com.memo.app`
* **App Name:** `MEMO`
* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Minimum SDK:** `26` (Android 8.0 Oreo — covers 95%+ devices while supporting Java 8 date/time desugaring).
* **Target / Compile SDK:** `34` (Android 14).
* **Orientation:** Lock to Portrait (`android:screenOrientation="portrait"` in `AndroidManifest.xml`).
* **Theme:** Material 3 Dynamic / Light Theme with Medical Slate (`#0F172A`) and Teal (`#0D9488`).

---

## G. Antigravity AI Coding Agent Preparation

To ensure Antigravity implements the application cleanly without altering your existing docs or hallucinating backend dependencies, prepare the workspace rules:

### 1. Create Workspace Instructions File: `.agents/rules/memo_rules.md`
Create this file in your project directory:

```markdown
# MEMO Agent Development Rules

## 1. Scope Boundaries
- This phase implements ONLY the Android frontend application inside the `android/` directory.
- DO NOT generate backend Python, FastAPI, Supabase client, or PaddleOCR code.
- DO NOT overwrite or delete `DEVELOPMENT_PLAN.md` or `FRONTEND_PROTOTYPE_PLAN.md`.

## 2. Architecture & Design Principles
- Package base: `com.memo.app`
- Pattern: MVVM (Model-View-ViewModel) + Repository Pattern.
- UI Toolkit: 100% Jetpack Compose with Material 3. NO XML layouts.
- Data Seam: All ViewModels MUST consume `IMemoRepository`.
- Data Source: Implement `MockMemoRepository` backed by `DemoDataSource.kt`.
- No Medical Claims: All extracted values must display standard reference indicators and an extraction disclaimer.

## 3. Commit Discipline
- Implement one Jira story at a time.
- Format commit messages as: `feat(MEMO-XX): description` or `fix(MEMO-XX): description`.
```

### 2. Antigravity Implementation Cadence
* Direct Antigravity to build feature by feature (e.g., Block 1 Setup ➔ Block 2 Design Tokens ➔ Block 3 Data Models & Repo ➔ etc.).
* Run a clean build after each block:
  ```powershell
  cd android
  ./gradlew assembleDebug
  ```
* Commit at each stable checkpoint before prompting for the next screen.

---

## H. Demo Data & Sample Files Preparation

### 1. Fictional Patient Profile
* **Patient Name:** `Aarav Mehta`
* **Age / Gender:** `42 Male`
* **Patient ID:** `MEMO-P-88219`
* **Blood Group:** `B Positive`
* **Emergency Contact:** `Dr. S. K. Mehta (Cardiologist)`

### 2. Fictional Healthcare Facilities
1. **NovaCare Diagnostics & Wellness** (Advanced pathology)
2. **CityMed Central Laboratories** (Routine biochemistry & urinalysis)
3. **Metro Multispeciality Hospital** (Inpatient discharge & clinical consults)
4. **HealthFirst Imaging & Ultrasound Center** (Radiology)

### 3. Demo Data Structure (12 Records Breakdown)
Store all 12 records as Kotlin data structures in `android/app/src/main/java/com/memo/app/data/mock/DemoDataSource.kt`:

1. **Jan 15, 2025** — *Annual Health Checkup & CBC* (NovaCare) — Status: Normal
2. **Mar 10, 2025** — *Lipid Profile Panel* (CityMed) — Status: Slightly Elevated LDL (138 mg/dL)
3. **May 22, 2025** — *Follow-up Fasting Blood Glucose & HbA1c* (CityMed) — Status: Pre-diabetic (HbA1c 6.2%)
4. **Jul 14, 2025** — *Abdominal Ultrasound* (HealthFirst) — Status: Mild Grade-1 Fatty Liver
5. **Aug 30, 2025** — *Comprehensive Metabolic Panel (CMP)* (NovaCare) — Status: Normal renal/liver markers
6. **Oct 18, 2025** — *Cardiology Consultation & Prescription* (Metro Multispeciality) — Status: Lifestyle changes
7. **Dec 05, 2025** — *Thyroid Function Test (TSH, Free T3/T4)* (CityMed) — Status: Normal (TSH 2.4 mIU/L)
8. **Jan 20, 2026** — *Urine Routine & Microscopic Exam* (CityMed) — Status: Normal, no proteinuria
9. **Feb 28, 2026** — *Hepatology Review & LFT* (Metro Multispeciality) — Status: ALT 48 U/L (Borderline)
10. **Apr 12, 2026** — *Follow-up HbA1c & Fasting Insulin* (NovaCare) — Status: Improved (HbA1c 5.8%)
11. **Jun 19, 2026** — *Repeat Lipid Profile* (CityMed) — Status: Normal (LDL 112 mg/dL)
12. **Sep 08, 2026** — *Comprehensive Wellness Panel* (NovaCare) — **PRIMARY DEMO RECORD**

### 4. Primary Demo Record (Used for Professor Deep-Dive)
* **Report Title:** Comprehensive Metabolic & Glycemic Profile
* **Facility:** NovaCare Diagnostics & Wellness
* **Date:** September 08, 2026
* **Key Lab Parameters:**
  * **HbA1c:** `5.7 %` (Reference: 4.0 – 5.6 %) ➔ Tag: **Borderline / Elevated**
  * **Fasting Blood Glucose:** `98 mg/dL` (Reference: 70 – 99 mg/dL) ➔ Tag: **Normal**
  * **Total Cholesterol:** `182 mg/dL` (Reference: < 200 mg/dL) ➔ Tag: **Normal**
  * **LDL Cholesterol:** `108 mg/dL` (Reference: < 100 mg/dL) ➔ Tag: **Borderline**
  * **Serum Creatinine:** `0.92 mg/dL` (Reference: 0.70 – 1.30 mg/dL) ➔ Tag: **Normal**
  * **SGPT (ALT):** `32 U/L` (Reference: 7 – 56 U/L) ➔ Tag: **Normal**

### 5. Sample Upload Files on Your Computer
Prepare a local folder `docs/demo_assets/` containing 4 synthetic files to select during the upload demo:
1. `blood_report_cbc_novacare.pdf` (1–2 page dummy medical lab PDF).
2. `prescription_metro_hospital.jpg` (Clear photo of synthetic doctor's prescription).
3. `lipid_panel_citymed.pdf` (Lab report with tabular figures).
4. `ultrasound_abdomen_summary.pdf` (Radiology imaging report).

*(Tip: Copy these to your Android Emulator's `/sdcard/Download/` folder using Device Explorer so you can select them instantly via the Android system file picker during the demonstration).*

---

## I. Docker Setup

### 1. Docker Strategy for This Phase: Option C (Reproducible Build Environment)
* **The Concept:** Do **not** build a fake backend container. Instead, use Docker for **DevOps Build Reproducibility** — an official Ubuntu + OpenJDK 17 + Android SDK container that builds the Android APK identically regardless of host OS.
* **Why this is academically superior:** It proves you understand Docker's role in enterprise CI/CD without wasting time writing dummy backend stubs.

### 2. Prepare `Dockerfile.build` (Place at Repository Root)
```dockerfile
# MEMO Android CI Build Environment
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Install base dependencies and OpenJDK 17
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-17-jdk \
    curl \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Install Android Command-line Tools
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    curl -o /tmp/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# Accept licenses and install platform-tools, build-tools 34, and android-34
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

WORKDIR /workspace
```

### 3. Verify Docker Locally (Run in PowerShell)
```powershell
docker build -t memo-android-builder -f Dockerfile.build .
```
*(Keep this cached image ready for the Jenkins pipeline).*

---

## J. Jenkins Setup

### 1. Recommended Jenkins Hosting Option
* **Recommendation:** **Option B: Run Jenkins via Docker.**
* **Why:** Running Jenkins in Docker (`docker run -d -p 8080:8080 jenkins/jenkins:lts-jdk17`) avoids installing Windows system services, keeps your laptop clean, and is easily started or stopped during your presentation.

### 2. Jenkins Container Launch Command
Run this in PowerShell to start Jenkins:
```powershell
docker run -d `
  --name memo-jenkins `
  -p 8080:8080 `
  -p 50000:50000 `
  -v jenkins_home:/var/jenkins_home `
  -v //var/run/docker.sock:/var/run/docker.sock `
  jenkins/jenkins:lts-jdk17
```

* Retrieve initial admin password:
  ```powershell
  docker logs memo-jenkins
  ```
* Open `http://localhost:8080`, install **Suggested Plugins**, and install the **Pipeline** and **Git** plugins.

### 3. Declarative `Jenkinsfile` (Place at Repository Root)
This is the exact pipeline script you will commit:

```groovy
pipeline {
    agent any

    environment {
        ANDROID_HOME = '/opt/android-sdk'
    }

    stages {
        stage('1. Checkout SCM') {
            steps {
                echo 'Checking out source code from GitHub repository...'
                checkout scm
            }
        }

        stage('2. Prepare Environment') {
            steps {
                echo 'Verifying Java & Gradle environment...'
                sh 'java -version'
                dir('android') {
                    sh 'chmod +x gradlew'
                }
            }
        }

        stage('3. Unit Tests') {
            steps {
                echo 'Executing Android Unit Tests...'
                dir('android') {
                    sh './gradlew testDebugUnitTest --continue'
                }
            }
            post {
                always {
                    junit testResults: 'android/app/build/test-results/**/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('4. Static Code Analysis (Lint)') {
            steps {
                echo 'Running Android Lint...'
                dir('android') {
                    sh './gradlew lintDebug'
                }
            }
        }

        stage('5. Assemble Debug APK') {
            steps {
                echo 'Building Android Debug APK artifact...'
                dir('android') {
                    sh './gradlew assembleDebug'
                }
            }
        }

        stage('6. Archive Build Artifacts') {
            steps {
                echo 'Archiving generated APK...'
                archiveArtifacts artifacts: 'android/app/build/outputs/apk/debug/*.apk', fingerprint: true
            }
        }
    }

    post {
        success {
            echo 'MEMO Build Pipeline Completed Successfully!'
        }
        failure {
            echo 'Build Failed. Check stage logs for details.'
        }
    }
}
```

---

## K. GitHub ↔ Jira ↔ Jenkins Integration

### 1. Jira ↔ GitHub Connection
* In Jira: Go to **Apps -> Explore More Apps -> GitHub for Jira** (official Atlassian app).
* Connect your GitHub account and select repository `MEMO-Medical-Evidence-Management`.
* *Result:* Every branch and commit containing `MEMO-XX` will automatically link to the respective Jira ticket under "Development" on the issue page.

### 2. Branch & Commit Conventions
* **Branch Naming:** `feature/MEMO-<ID>-<short-description>`  
  *Example:* `feature/MEMO-4-dashboard-summary`
* **Commit Messages:** Follow Conventional Commits:  
  *Example:* `feat(MEMO-4): implement health stats card and greeting banner`  
  *Example:* `test(MEMO-3): add unit tests for MockMemoRepository`
* **Pull Request Title:** `[MEMO-4] Implement Executive Medical Dashboard`  
  *PR Body:* `Closes MEMO-4. Adds Compose dashboard with health stats and recent reports.`

### 3. Jenkins ↔ GitHub Connection
* Because Jenkins is running on `localhost`, automated webhooks from public GitHub cannot reach your machine without tunneling (ngrok).
* **The Reliable Academic Solution:** Set the Jenkins pipeline trigger to **Poll SCM** (`H/5 * * * *` — checks every 5 minutes) or click **Build Now** manually in front of the professor to demonstrate the live execution.

---

## L. Project Documentation (`README.md` Template)

Create `README.md` at the repository root before coding:

```markdown
# MEMO — Medical Evidence Management & Organization
> *Every Report. One Medical Memory.*

[![Build Status](http://localhost:8080/buildStatus/icon?job=MEMO-Pipeline)](http://localhost:8080)
[![Android](https://img.shields.io/badge/Platform-Android_14-teal.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin_1.9-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack_Compose_Material3-blue.svg)](https://developer.android.com/jetpack/compose)

---

## 1. Project Overview
MEMO is an intelligent medical evidence aggregator that converts unstructured medical reports (blood tests, imaging, prescriptions) into a coherent, searchable, longitudinal health history.

### Current Phase: Frontend Prototype & DevOps Demonstration
- **Target:** Android Application Prototype (Kotlin, Jetpack Compose, Material 3, MVVM).
- **Data Source:** In-Memory Typed Repository with 12 Longitudinal Fictional Patient Records.
- **DevOps:** Jira Scrum Board, Git-Flow, Dockerized Build Tooling, and Automated Jenkins CI/CD.
- **Future Integration:** Architecture built on clean Repository interfaces designed for zero-refactor swap to FastAPI + Supabase backend.

---

## 2. Architecture & Design System
- **MVVM Pattern:** Strict separation of UI (Jetpack Compose) and business logic (StateFlow ViewModels).
- **Repository Abstraction:** `IMemoRepository` provides decoupling between the UI layer and data providers.
- **Palette:** Clinical Slate (`#0F172A`), Primary Teal (`#0D9488`), Amber Warnings (`#F59E0B`).

---

## 3. DevOps & CI/CD Pipeline
The project utilizes automated CI/CD:
1. **GitHub:** Version control with feature-branch workflow.
2. **Jira:** Agile tracking with linked commits (`MEMO-XX`).
3. **Docker:** Reproducible Android build environment container.
4. **Jenkins:** 6-stage automated pipeline compiling, linting, testing, and archiving `app-debug.apk`.

---

## 4. How to Build & Run
### Prerequisites
- Android Studio Iguana+ with JDK 17
- Android SDK 34 (Android 14)

### Local Build
```bash
cd android
./gradlew assembleDebug
```
The output APK will be located at `android/app/build/outputs/apk/debug/app-debug.apk`.
```

---

## M. Professor Demo Evidence Checklist

Capture these screenshots as you build to compile a backup slide deck or project report:

### 1. Jira Evidence
- [ ] Active Sprint Board with cards distributed across `Done`, `In Progress`, and `To Do`.
- [ ] Burndown chart showing progress.
- [ ] User Story detail view (`MEMO-4`) showing linked Git commits and branch.
- [ ] Backlog view displaying the Future Epics (Backend, OCR, AI) marked as backlog items.

### 2. GitHub Evidence
- [ ] Repository homepage with professional `README.md` and badges.
- [ ] Network / Insights graph showing feature branches merging into `develop`.
- [ ] Closed Pull Request showing code review comments and Jira ID reference.
- [ ] Commit history displaying conventional commit messages with Jira tags.

### 3. Jenkins & Docker Evidence
- [ ] Jenkins dashboard displaying the green pipeline stage view (Checkout ➔ Setup ➔ Tests ➔ Lint ➔ Build ➔ Archive).
- [ ] Test Results page showing passing unit tests.
- [ ] Artifacts panel showing downloadable `app-debug.apk`.
- [ ] Docker terminal output showing the build container (`docker build -t memo-android-builder`).

### 4. Android App Interactive Demo
- [ ] **Screen 1: Dashboard:** Health metrics overview, stat cards, recent reports.
- [ ] **Screen 2: Reports List:** Filter chips switching between Blood, Imaging, Urinalysis.
- [ ] **Screen 3: Report Detail:** Lab results with status tags (Normal/High) and medical disclaimer.
- [ ] **Screen 4: Timeline:** Chronological health progression.
- [ ] **Screen 5: Search:** Live search matching "HbA1c" or "NovaCare".
- [ ] **Screen 6: Ingest Demo:** Picking a test PDF and watching the simulated 3-step extraction animation.

---

## N. Exact Step-by-Step Setup Order

Execute in this strict chronological order before starting the frontend implementation:

1. **Verify Local Tools:** Run `java -version`, `git --version`, `docker --version`.
2. **Create GitHub Repository:** Create empty repo `MEMO-Medical-Evidence-Management`.
3. **Initialize Git Locally:** Add `.gitignore`, initial docs commit, push `main`, create `develop`.
4. **Setup Jira Project:** Create Scrum project `MEMO`, add columns, set DoD.
5. **Create Jira Epics & Stories:** Input epics `MEMO-E1` to `MEMO-E8`, create stories `MEMO-1` to `MEMO-10`.
6. **Start Jira Sprint:** Add stories 1–10, set sprint goal, click **Start Sprint**.
7. **Connect Jira to GitHub:** Install GitHub for Jira app, link repository.
8. **Initialize Base Android Project:** Create blank Compose project in `android/` with package `com.memo.app`, verify `./gradlew assembleDebug` compiles.
9. **Commit Base Project:** Commit initial project skeleton to `develop` (`chore: initialize Android Jetpack Compose project skeleton`).
10. **Prepare Demo Files:** Place 4 synthetic test files in `docs/demo_assets/`.
11. **Configure Dockerfile & Jenkinsfile:** Commit `Dockerfile.build` and `Jenkinsfile` to repository root.
12. **Start Jenkins & Run Initial Build:** Launch Jenkins container, create pipeline job, run build to verify green status.
13. **Create First Feature Branch:** `git checkout -b feature/MEMO-2-design-system`.
14. **Give Antigravity Implementation Prompt:** Begin coding the UI components.

---

## O. Final Pre-Development Checklist

```markdown
[ ] 1. GitHub public repository created and cloned locally
[ ] 2. .gitignore committed at root (ignoring .gradle, build, local.properties)
[ ] 3. Branches 'main' and 'develop' created and pushed to GitHub
[ ] 4. Jira Scrum project created with key 'MEMO'
[ ] 5. Board columns configured (Backlog, To Do, In Progress, Code Review, Testing, Done)
[ ] 6. 4 Active Epics and 4 Future Epics created in Jira
[ ] 7. 10 User Stories with acceptance criteria created in Jira
[ ] 8. Sprint 1 started with 10 user stories
[ ] 9. GitHub for Jira integration configured
[ ] 10. Android Studio verified with SDK 34 and JDK 17
[ ] 11. Blank Android project compiles cleanly (./gradlew assembleDebug)
[ ] 12. 4 synthetic demo files prepared in docs/demo_assets/
[ ] 13. Dockerfile.build created and verified locally
[ ] 14. Jenkins container running and pipeline job created
[ ] 15. README.md created with project scope and architecture badges
[ ] 16. .agents/rules/memo_rules.md created to guide the AI agent
[ ] 17. Working branch 'feature/MEMO-2-design-system' checked out
```

---

## P. What You Should NOT Waste Time On Today

To ensure you submit tomorrow on time, **strictly postpone**:
* **Supabase:** Do not create a Supabase project, database tables, or auth tokens.
* **FastAPI Backend:** Do not write Python microservices or endpoints.
* **Real OCR / PaddleOCR:** Do not install Python OCR libraries or train models.
* **Cloud Infrastructure:** Do not set up AWS, GCP, Azure, or Kubernetes clusters.
* **Complex Testing:** Do not attempt UI instrumented tests (Espresso); write only simple JVM unit tests for the Mock Repository.
* **Dark Theme:** Do not build dual themes today; stick to a clean, crisp medical light theme.
* **Multi-User Auth:** Do not build registration flows, password resets, or OTP verification. Use a single-click demo login.

---

## START CODING ONLY AFTER THESE ARE READY (Top 12 Gating Items)

Do not type application code or prompt the AI until these 12 items are complete:

1. **JDK 17 and Android SDK 34 verified** on your machine.
2. **GitHub repository initialized** with `main` and `develop` branches.
3. **Root `.gitignore` in place** so build caches never get tracked.
4. **Jira Scrum project `MEMO` active** with Sprint 1 started.
5. **Jira user stories `MEMO-1` through `MEMO-10` visible** on your board.
6. **Jira connected to GitHub** (or commit format `feat(MEMO-X): ...` memorized).
7. **Blank Android project compiles** (`./gradlew assembleDebug` exits 0).
8. **App package set to `com.memo.app`** with Min SDK 26, Target SDK 34.
9. **`Dockerfile.build` and `Jenkinsfile` committed** to the repository root.
10. **Jenkins pipeline runs once** and archives the blank template APK.
11. **4 demo files placed** in `docs/demo_assets/` for the upload demo.
12. **Agent rules file `.agents/rules/memo_rules.md` saved** to prevent AI drift into backend tasks.
