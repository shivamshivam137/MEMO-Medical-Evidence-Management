# Firebase Setup Guide for MEMO

This guide explains how to connect your **Firebase** project for **Authentication** and **Cloud Firestore Data Storage**.

---

## 1. Create a Firebase Project

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click **Add project** (or **Create a project**).
3. Name your project (e.g., `memo-medical-evidence`).
4. Enable or disable Google Analytics as desired, then click **Create project**.

---

## 2. Register Android App

1. On the project overview page, click the **Android** icon (`+ Add app`).
2. Enter the exact Android package name:
   ```text
   com.memo.app
   ```
3. (Optional) App nickname: `MEMO Android App`.
4. (Optional) Debug signing certificate SHA-1:
   - Run in terminal:
     ```bash
     cd android
     ./gradlew signingReport
     ```
   - Copy the SHA-1 from `debug` and paste it into Firebase Console.
5. Click **Register app**.

---

## 3. Download & Place `google-services.json`

1. Download the `google-services.json` file provided by Firebase.
2. Replace the template file located at:
   ```text
   android/app/google-services.json
   ```
   > **Note:** A placeholder `google-services.json` has already been placed in `android/app/google-services.json`. Replace it with your actual downloaded configuration file.

---

## 4. Enable Firebase Authentication

1. In the Firebase Console left sidebar, click **Build** > **Authentication**.
2. Click **Get Started**.
3. Under the **Sign-in method** tab:
   - Select **Email/Password**.
   - Toggle **Enable** to ON.
   - Click **Save**.

---

## 5. Enable Cloud Firestore Database

1. In the Firebase Console left sidebar, click **Build** > **Firestore Database**.
2. Click **Create database**.
3. Select a database location close to your users (e.g., `asia-south1` or `us-central1`).
4. In Security Rules, select **Start in production mode** or **test mode**.
5. Click **Enable**.

### Apply Firestore Security Rules

To enforce strict patient medical data privacy, copy and paste the rules from [`firestore.rules`](file:///c:/Users/singh/OneDrive/Desktop/shivam%20file/Projects/SIH_Project/MEMO_WMAD_project/firestore.rules) into the **Firestore Rules** tab:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      match /reports/{reportId} {
        allow read, write, delete: if request.auth != null && request.auth.uid == userId;
      }
    }
  }
}
```

---

## 6. How Data is Structured in Firestore

### Patient Profile Collection: `/users/{userId}`
```json
{
  "id": "firebase_auth_uid",
  "name": "Shivam Singh",
  "email": "shivam@example.com",
  "memberSince": "October 2026",
  "bloodGroup": "B+",
  "age": 35,
  "gender": "Male"
}
```

### Medical Reports Subcollection: `/users/{userId}/reports/{reportId}`
```json
{
  "id": "rpt-001",
  "title": "Complete Blood Count (CBC)",
  "type": "BLOOD_TEST",
  "date": "2026-10-02",
  "organization": "NovaCare Diagnostics & Wellness",
  "doctor": "Dr. Priya Sharma (Pathologist)",
  "summary": "Routine complete hemogram test...",
  "hasAbnormalValues": false,
  "originalFileName": "blood_report_cbc_novacare.pdf",
  "findings": "",
  "labResults": [
    {
      "testName": "Hemoglobin",
      "value": "13.4",
      "unit": "g/dL",
      "referenceRange": "13.0 – 17.0",
      "status": "NORMAL"
    }
  ],
  "medications": []
}
```

---

## 7. Verifying in the App

1. Launch the app:
   - On the **Login** screen, choose **Create Account**.
   - Enter your Full Name, Email, and Password (min 6 characters).
   - Tap **Create Firebase Account**.
2. Go to **Profile** (bottom navigation) and tap **Seed 12 Sample Reports to Firestore**.
3. View your Firebase Console under Firestore: you will see the new user and all 12 medical reports appear live!
4. Any new uploads in the **Upload** tab will automatically be stored in your Firestore collection.
