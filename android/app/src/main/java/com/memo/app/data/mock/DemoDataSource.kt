package com.memo.app.data.mock

import com.memo.app.data.model.LabResult
import com.memo.app.data.model.MedicalReport
import com.memo.app.data.model.Medication
import com.memo.app.data.model.ParameterStatus
import com.memo.app.data.model.ReportType
import com.memo.app.data.model.User

object DemoDataSource {

    val currentUser = User(
        id = "usr-001",
        name = "Aarav Mehta",
        email = "aarav.mehta@memo.demo",
        memberSince = "June 2025",
        bloodGroup = "B+",
        age = 42,
        gender = "Male"
    )

    private val initialReports = mutableListOf(
        MedicalReport(
            id = "rpt-001",
            title = "Complete Blood Count (CBC)",
            type = ReportType.BLOOD_TEST,
            date = "2026-10-02",
            organization = "NovaCare Diagnostics & Wellness",
            doctor = "Dr. Priya Sharma (Pathologist)",
            summary = "Routine complete hemogram test. All red blood cell, white blood cell, and platelet parameters within normal biological reference intervals.",
            hasAbnormalValues = false,
            originalFileName = "blood_report_cbc_novacare.pdf",
            labResults = listOf(
                LabResult("Hemoglobin", "13.4", "g/dL", "13.0 – 17.0", ParameterStatus.NORMAL),
                LabResult("WBC Count", "7,200", "/cumm", "4,000 – 11,000", ParameterStatus.NORMAL),
                LabResult("RBC Count", "4.9", "million/cumm", "4.5 – 5.5", ParameterStatus.NORMAL),
                LabResult("Platelet Count", "2.45", "lakh/cumm", "1.5 – 4.5", ParameterStatus.NORMAL),
                LabResult("Hematocrit (PCV)", "40.2", "%", "38.0 – 50.0", ParameterStatus.NORMAL),
                LabResult("MCV", "82.0", "fL", "80.0 – 100.0", ParameterStatus.NORMAL),
                LabResult("MCH", "27.3", "pg", "27.0 – 32.0", ParameterStatus.NORMAL),
                LabResult("ESR", "12", "mm/hr", "0 – 15", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-002",
            title = "Lipid Profile Panel",
            type = ReportType.BLOOD_TEST,
            date = "2026-10-02",
            organization = "NovaCare Diagnostics & Wellness",
            doctor = "Dr. Priya Sharma (Pathologist)",
            summary = "Lipid screen indicating elevated total cholesterol, elevated triglycerides, and borderline-high LDL cholesterol levels.",
            hasAbnormalValues = true,
            originalFileName = "lipid_panel_novacare.pdf",
            labResults = listOf(
                LabResult("Total Cholesterol", "215", "mg/dL", "< 200", ParameterStatus.ABNORMAL),
                LabResult("HDL Cholesterol", "48", "mg/dL", "> 40", ParameterStatus.NORMAL),
                LabResult("LDL Cholesterol", "138", "mg/dL", "< 100", ParameterStatus.ABNORMAL),
                LabResult("Triglycerides", "162", "mg/dL", "< 150", ParameterStatus.ABNORMAL),
                LabResult("VLDL Cholesterol", "32", "mg/dL", "5 – 40", ParameterStatus.NORMAL),
                LabResult("Total/HDL Ratio", "4.5", "ratio", "< 5.0", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-003",
            title = "Fasting Blood Glucose & HbA1c",
            type = ReportType.BLOOD_TEST,
            date = "2026-09-18",
            organization = "CityMed Central Laboratories",
            doctor = "Dr. Rajesh Iyer (Endocrinologist)",
            summary = "Glycemic control review. Fasting blood sugar slightly above baseline; HbA1c reflects mild pre-diabetic glycemic range.",
            hasAbnormalValues = true,
            originalFileName = "fasting_sugar_hba1c_citymed.pdf",
            labResults = listOf(
                LabResult("Fasting Blood Glucose", "102", "mg/dL", "70 – 100", ParameterStatus.ELEVATED),
                LabResult("HbA1c (Glycated Hb)", "5.8", "%", "4.0 – 5.6", ParameterStatus.ELEVATED),
                LabResult("Post-Prandial Blood Sugar", "138", "mg/dL", "70 – 140", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-004",
            title = "Thyroid Function Test (Total & Free)",
            type = ReportType.BLOOD_TEST,
            date = "2026-08-04",
            organization = "NovaCare Diagnostics & Wellness",
            doctor = "Dr. Priya Sharma (Pathologist)",
            summary = "Euthyroid profile. Both TSH and peripheral thyroid hormone concentrations are well-regulated.",
            hasAbnormalValues = false,
            originalFileName = "thyroid_panel_novacare.pdf",
            labResults = listOf(
                LabResult("TSH (Ultrasensitive)", "3.2", "mIU/L", "0.4 – 4.0", ParameterStatus.NORMAL),
                LabResult("Total T3", "1.1", "ng/mL", "0.8 – 2.0", ParameterStatus.NORMAL),
                LabResult("Total T4", "7.8", "μg/dL", "5.1 – 14.1", ParameterStatus.NORMAL),
                LabResult("Free T3", "3.1", "pg/mL", "2.0 – 4.4", ParameterStatus.NORMAL),
                LabResult("Free T4", "1.2", "ng/dL", "0.9 – 1.7", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-005",
            title = "Urine Routine & Microscopic Exam",
            type = ReportType.URINE_TEST,
            date = "2026-09-18",
            organization = "CityMed Central Laboratories",
            doctor = "Dr. Rajesh Iyer (Consultant)",
            summary = "Normal macroscopic and chemical urinalysis. No evidence of proteinuria, hematuria, or active urinary infection.",
            hasAbnormalValues = false,
            originalFileName = "urine_routine_citymed.pdf",
            labResults = listOf(
                LabResult("Color", "Pale Yellow", "", "Pale Yellow", ParameterStatus.NORMAL),
                LabResult("Appearance", "Clear", "", "Clear", ParameterStatus.NORMAL),
                LabResult("pH", "6.0", "pH", "4.5 – 8.0", ParameterStatus.NORMAL),
                LabResult("Specific Gravity", "1.020", "", "1.005 – 1.030", ParameterStatus.NORMAL),
                LabResult("Protein (Albumin)", "Nil", "", "Nil", ParameterStatus.NORMAL),
                LabResult("Glucose", "Nil", "", "Nil", ParameterStatus.NORMAL),
                LabResult("Ketones", "Nil", "", "Nil", ParameterStatus.NORMAL),
                LabResult("Pus Cells (WBC)", "2–3", "/HPF", "0 – 5", ParameterStatus.NORMAL),
                LabResult("RBCs", "0–1", "/HPF", "0 – 2", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-006",
            title = "Urine Microalbumin & Creatinine Ratio",
            type = ReportType.URINE_TEST,
            date = "2026-07-22",
            organization = "HealthFirst Hospital",
            doctor = "Dr. Anita Desai (Nephrologist)",
            summary = "Renal microvascular check. Albumin-to-creatinine ratio within normal physiological limits.",
            hasAbnormalValues = false,
            originalFileName = "urine_microalbumin_healthfirst.pdf",
            labResults = listOf(
                LabResult("Microalbumin (Urine)", "18", "mg/L", "< 20", ParameterStatus.NORMAL),
                LabResult("Creatinine (Urine)", "120", "mg/dL", "20 – 275", ParameterStatus.NORMAL),
                LabResult("Albumin/Creatinine Ratio (UACR)", "15", "mg/g", "< 30", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-007",
            title = "Cardiology Consultation & Prescription",
            type = ReportType.PRESCRIPTION,
            date = "2026-06-11",
            organization = "Metro Multispeciality Hospital",
            doctor = "Dr. S. K. Mehta (Cardiologist)",
            summary = "Follow-up consultation for mild dyslipidemia and lifestyle cardiovascular management.",
            hasAbnormalValues = false,
            originalFileName = "prescription_metro_hospital.jpg",
            medications = listOf(
                Medication("Atorvastatin", "10 mg", "Once daily (Night)", "3 Months", "Take after dinner with water"),
                Medication("Coenzyme Q10", "100 mg", "Once daily (Morning)", "3 Months", "Take after breakfast"),
                Medication("Omega-3 Fatty Acids", "1000 mg", "Once daily (Morning)", "3 Months", "Dietary supplement")
            )
        ),
        MedicalReport(
            id = "rpt-008",
            title = "Abdominal Ultrasound Report",
            type = ReportType.IMAGING,
            date = "2026-05-14",
            organization = "HealthFirst Imaging & Ultrasound Center",
            doctor = "Dr. Anita Desai (Radiologist)",
            summary = "Whole abdomen ultrasound showing mild diffusely increased hepatic echogenicity consistent with Grade 1 fatty liver.",
            hasAbnormalValues = true,
            originalFileName = "ultrasound_abdomen_summary.pdf",
            findings = "Liver: Normal size (13.8 cm). Diffuse increase in parenchymal echogenicity with preserved vascular architecture, consistent with Mild Grade I Fatty Infiltration. Gallbladder: Normal lumen, no calculi or wall thickening. Spleen, Pancreas, and Bilateral Kidneys: Normal morphology and acoustic profile. Impression: Mild hepatic steatosis (Grade 1)."
        ),
        MedicalReport(
            id = "rpt-009",
            title = "Daycare Procedure Discharge Summary",
            type = ReportType.DISCHARGE_SUMMARY,
            date = "2026-03-02",
            organization = "Metro Multispeciality Hospital",
            doctor = "Dr. Alok Verma (Gastroenterologist)",
            summary = "Diagnostic upper gastrointestinal endoscopy performed under conscious sedation. Mild superficial antral gastritis noted.",
            hasAbnormalValues = false,
            originalFileName = "discharge_summary_metro.pdf",
            findings = "Procedure: Diagnostic Upper GI Endoscopy. Findings: Esophagus normal. Stomach revealed mild patchy erythema in the antrum. Rapid Urease Test (RUT) for H. pylori was negative. Duodenum (D1/D2) normal. Advised pantoprazole 40mg for 14 days and dietary moderation."
        ),
        MedicalReport(
            id = "rpt-010",
            title = "Liver Function Panel (LFT)",
            type = ReportType.BLOOD_TEST,
            date = "2026-02-15",
            organization = "CityMed Central Laboratories",
            doctor = "Dr. Rajesh Iyer (Consultant)",
            summary = "Hepatic enzymes evaluation. Mild elevation in SGPT (ALT) reflecting ultrasound findings; remaining synthetic liver markers normal.",
            hasAbnormalValues = true,
            originalFileName = "lft_panel_citymed.pdf",
            labResults = listOf(
                LabResult("Total Bilirubin", "0.8", "mg/dL", "0.2 – 1.2", ParameterStatus.NORMAL),
                LabResult("Direct Bilirubin", "0.2", "mg/dL", "0.0 – 0.3", ParameterStatus.NORMAL),
                LabResult("SGOT (AST)", "34", "U/L", "10 – 40", ParameterStatus.NORMAL),
                LabResult("SGPT (ALT)", "48", "U/L", "7 – 45", ParameterStatus.ELEVATED),
                LabResult("Alkaline Phosphatase (ALP)", "92", "U/L", "40 – 130", ParameterStatus.NORMAL),
                LabResult("Total Protein", "7.1", "g/dL", "6.0 – 8.3", ParameterStatus.NORMAL),
                LabResult("Serum Albumin", "4.3", "g/dL", "3.5 – 5.0", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-011",
            title = "Annual Health Checkup Baseline",
            type = ReportType.BLOOD_TEST,
            date = "2025-01-15",
            organization = "NovaCare Diagnostics & Wellness",
            doctor = "Dr. Priya Sharma (Pathologist)",
            summary = "Baseline corporate annual physical panel. All hematological and basic metabolic indices within normal limits.",
            hasAbnormalValues = false,
            originalFileName = "annual_checkup_2025_novacare.pdf",
            labResults = listOf(
                LabResult("Hemoglobin", "13.6", "g/dL", "13.0 – 17.0", ParameterStatus.NORMAL),
                LabResult("Fasting Blood Sugar", "94", "mg/dL", "70 – 100", ParameterStatus.NORMAL),
                LabResult("Total Cholesterol", "188", "mg/dL", "< 200", ParameterStatus.NORMAL),
                LabResult("Serum Creatinine", "0.90", "mg/dL", "0.7 – 1.3", ParameterStatus.NORMAL),
                LabResult("Blood Urea Nitrogen", "14", "mg/dL", "7 – 20", ParameterStatus.NORMAL)
            )
        ),
        MedicalReport(
            id = "rpt-012",
            title = "Comprehensive Metabolic & Glycemic Profile",
            type = ReportType.BLOOD_TEST,
            date = "2026-09-08",
            organization = "NovaCare Diagnostics & Wellness",
            doctor = "Dr. Priya Sharma (Pathologist)",
            summary = "Primary quarterly follow-up panel. Demonstrates improvement in glycemic parameters following dietary intervention.",
            hasAbnormalValues = true,
            originalFileName = "comprehensive_profile_novacare.pdf",
            labResults = listOf(
                LabResult("HbA1c", "5.7", "%", "4.0 – 5.6", ParameterStatus.ELEVATED),
                LabResult("Fasting Blood Glucose", "98", "mg/dL", "70 – 99", ParameterStatus.NORMAL),
                LabResult("Total Cholesterol", "182", "mg/dL", "< 200", ParameterStatus.NORMAL),
                LabResult("LDL Cholesterol", "108", "mg/dL", "< 100", ParameterStatus.ELEVATED),
                LabResult("Serum Creatinine", "0.92", "mg/dL", "0.70 – 1.30", ParameterStatus.NORMAL),
                LabResult("SGPT (ALT)", "32", "U/L", "7 – 56", ParameterStatus.NORMAL)
            )
        )
    )

    val reports: MutableList<MedicalReport> = initialReports.toMutableList()

    fun addUploadedReport(fileName: String): MedicalReport {
        val newId = "rpt-${String.format("%03d", reports.size + 1)}"
        val isUrine = fileName.lowercase().contains("urine")
        val isPrescription = fileName.lowercase().contains("prescription")
        val isImaging = fileName.lowercase().contains("ultrasound") || fileName.lowercase().contains("xray")

        val newReport = when {
            isPrescription -> MedicalReport(
                id = newId,
                title = "Uploaded Prescription Note",
                type = ReportType.PRESCRIPTION,
                date = "2026-10-04",
                organization = "Metro Multispeciality Hospital",
                doctor = "Dr. S. K. Mehta (Cardiologist)",
                summary = "Automatically digitized prescription from uploaded medical document. Structured medication regimen extracted.",
                hasAbnormalValues = false,
                originalFileName = fileName,
                medications = listOf(
                    Medication("Atorvastatin", "10 mg", "Once daily", "90 Days", "Take with water at bedtime"),
                    Medication("Metformin", "500 mg", "Twice daily", "90 Days", "Take after meals")
                )
            )
            isImaging -> MedicalReport(
                id = newId,
                title = "Uploaded Ultrasound Report",
                type = ReportType.IMAGING,
                date = "2026-10-04",
                organization = "HealthFirst Imaging Center",
                doctor = "Dr. Anita Desai (Radiologist)",
                summary = "Digitized ultrasound radiology examination report with extracted organ morphology observations.",
                hasAbnormalValues = false,
                originalFileName = fileName,
                findings = "Ultrasonography demonstrated normal organ margins. Parenchymal acoustic texture homogeneous without discrete space-occupying lesions. Impression: Stable study."
            )
            isUrine -> MedicalReport(
                id = newId,
                title = "Uploaded Urinalysis Panel",
                type = ReportType.URINE_TEST,
                date = "2026-10-04",
                organization = "CityMed Central Laboratories",
                doctor = "Dr. Rajesh Iyer (Consultant)",
                summary = "Automated chemical and cellular urinalysis panel extracted from newly uploaded lab sheet.",
                hasAbnormalValues = false,
                originalFileName = fileName,
                labResults = listOf(
                    LabResult("Color", "Straw Yellow", "", "Pale Yellow", ParameterStatus.NORMAL),
                    LabResult("pH", "6.5", "pH", "4.5 – 8.0", ParameterStatus.NORMAL),
                    LabResult("Protein", "Nil", "", "Nil", ParameterStatus.NORMAL),
                    LabResult("Glucose", "Nil", "", "Nil", ParameterStatus.NORMAL)
                )
            )
            else -> MedicalReport(
                id = newId,
                title = "Uploaded Laboratory Report",
                type = ReportType.BLOOD_TEST,
                date = "2026-10-04",
                organization = "NovaCare Diagnostics & Wellness",
                doctor = "Dr. Priya Sharma (Pathologist)",
                summary = "Newly ingested laboratory document. Extracted key biomarker parameters and verified against standard reference intervals.",
                hasAbnormalValues = false,
                originalFileName = fileName,
                labResults = listOf(
                    LabResult("Hemoglobin", "13.8", "g/dL", "13.0 – 17.0", ParameterStatus.NORMAL),
                    LabResult("WBC Count", "6,800", "/cumm", "4,000 – 11,000", ParameterStatus.NORMAL),
                    LabResult("Fasting Glucose", "96", "mg/dL", "70 – 100", ParameterStatus.NORMAL),
                    LabResult("Serum Creatinine", "0.88", "mg/dL", "0.7 – 1.3", ParameterStatus.NORMAL)
                )
            )
        }

        reports.add(0, newReport) // Add at top (newest first)
        return newReport
    }
}
