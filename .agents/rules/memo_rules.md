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
- Implement one Jira story/block at a time.
- Format commit messages as: `feat(MEMO-XX): description` or `chore(MEMO-XX): description`.
