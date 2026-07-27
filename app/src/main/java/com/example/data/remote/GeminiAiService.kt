package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiAiService {

    suspend fun analyzeServiceIssue(
        brandModel: String,
        symptoms: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineDiagnosticAdvice(brandModel, symptoms)
        }

        try {
            val endpointUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val url = URL(endpointUrl)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                connectTimeout = 30000
                readTimeout = 30000
                doOutput = true
            }

            val promptText = """
                Kamu adalah Asisten Master Teknisi HP Senior dari GANK SERVICE.
                Berikan diagnosa teknis profesional untuk smartphone berikut:
                - Perangkat: $brandModel
                - Keluhan / Gejala: $symptoms

                Tuliskan analisis dalam format poin-poin yang singkat, padat, dan bertingkat:
                1. 🔍 DUGAN KERUSAKAN UTAMA (Hardware / Software)
                2. ⚡ LANGKAH ANALISIS CEK TEKNISI (Pengukuran Multitester / Power Supply / Test Point)
                3. 🛠️ ESTIMASI SPAREPART & KOMPONEN YANG PERLU DIGANTI
                4. 💡 REKOMENDASI OPSI PERBAIKAN & SKEMA SOLUSI
            """.trimIndent()

            val requestBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", promptText)
                            })
                        })
                    })
                })
            }

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody.toString())
                writer.flush()
            }

            if (connection.responseCode == 200) {
                val responseText = BufferedReader(InputStreamReader(connection.inputStream)).use { it.readText() }
                val jsonResponse = JSONObject(responseText)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Hasil analisa tidak tersedia.")
                    }
                }
            }
            return@withContext getOfflineDiagnosticAdvice(brandModel, symptoms)
        } catch (e: Exception) {
            return@withContext getOfflineDiagnosticAdvice(brandModel, symptoms) + "\n\n(Catatan: Mode Offline Aktif)"
        }
    }

    private fun getOfflineDiagnosticAdvice(brandModel: String, symptoms: String): String {
        val lowerSymptoms = symptoms.lowercase()
        return when {
            lowerSymptoms.contains("matot") || lowerSymptoms.contains("mati total") || lowerSymptoms.contains("tidak hidup") -> {
                """
                🔍 DIAGNOSA OFFLINE GANK TEKNISI: MATI TOTAL (MATOT)
                📱 Perangkat: $brandModel
                
                1. ⚡ DUGAN KERUSAKAN:
                   - Baterai Drop Voltage (< 3.7V)
                   - Jalur VBAT / VPH_PWR Short Circuit
                   - IC Power (PMIC / PMI) atau Dioda Proteksi rusak.
                
                2. 🔬 LANGKAH PENGUKURAN:
                   - Cek impedansi konektor baterai dengan Multitester (Mode Diode/Buzzer).
                   - Hubungkan ke DC Power Supply pada tegangan 4.0V. Amati konsumsi arus saat tombol Power ditekan.
                   - Arus 0mA -> Cek Saklar Power & Jalur VREF PMIC.
                   - Arus Gantung 0.05A - 0.15A -> Cek CPU/eMMC jalur I2C / Clock Crystal.
                   - Arus Short Full -> Cari komponen yang panas menggunakan Rosin Smoke / MBR.
                
                3. 🛠️ SPAREPART DIBUTUHKAN:
                   - Baterai Original / IC Power (PMIC) / Cap Capacitor pengganti.
                """.trimIndent()
            }
            lowerSymptoms.contains("lcd") || lowerSymptoms.contains("layar") || lowerSymptoms.contains("blank") || lowerSymptoms.contains("sentuh") -> {
                """
                🔍 DIAGNOSA OFFLINE GANK TEKNISI: LAYAR / LCD / TOUCHSCREEN
                📱 Perangkat: $brandModel
                
                1. ⚡ DUGAN KERUSAKAN:
                   - Modul LCD/OLED Pecah / Flex Cable Sobek.
                   - Jalur Backlight / Display Driver IC (OLED PMIC).
                   - Konektor FPC LCD Teroksidasi / Pin Bengkok.
                
                2. 🔬 LANGKAH PENGUKURAN:
                   - Test pasang LCD baru berkualitas Original.
                   - Ukur tegangan VSN (-5V) & VSP (+5V) pada pin konektor FPC LCD.
                   - Jika layar gelap tapi HP getar/bunyi -> Cek LED Backlight Anoda (+18V s/d +30V).
                
                3. 🛠️ SPAREPART DIBUTUHKAN:
                   - Assembly Modul LCD Crown / Original Equipment.
                """.trimIndent()
            }
            lowerSymptoms.contains("cas") || lowerSymptoms.contains("charge") || lowerSymptoms.contains("tidak bisa diisi") -> {
                """
                🔍 DIAGNOSA OFFLINE GANK TEKNISI: MENGISI DAYA / CHARGING
                📱 Perangkat: $brandModel
                
                1. ⚡ DUGAN KERUSAKAN:
                   - Port Usb Type-C / Micro Korosi / Longgar.
                   - Board Sub-Dock Charging / Flex Main-Sub.
                   - IC Charger (OVP / BQ Series).
                
                2. 🔬 LANGKAH PENGUKURAN:
                   - Cek VBUS (+5V) pada konektor papan cas.
                   - Cek Jalur DP/DM (Data Plus/Data Minus) USB untuk protokol Quick Charge.
                   - Amati konsumsi arus USB Doctor Ampere meter (Harus 1.0A - 2.0A).
                
                3. 🛠️ SPAREPART DIBUTUHKAN:
                   - Konektor Cas Type-C / Papan Cas Sub-Board / Flex Kabel Utama.
                """.trimIndent()
            }
            else -> {
                """
                🔍 DIAGNOSA OFFLINE GANK TEKNISI: ANALISIS UMUM HARDWARE
                📱 Perangkat: $brandModel
                 Keluhan: $symptoms
                
                1. ⚡ DUGAN KERUSAKAN:
                   - Kerusakan sistem modul fisik atau jalur suplai daya sekunder.
                   - Potensi kotoran/korosi air pada pcb board.
                
                2. 🔬 LANGKAH PENGUKURAN TEKNISI:
                   - Lakukan pembongkaran casing & inspeksi visual menggunakan mikroskop.
                   - Pembersihan komponen dengan Cairan IPA (Isopropanol) ultra-cleaner.
                   - Lakukan pengukuran tegangan kerja LDO & Buck Regulator.
                """.trimIndent()
            }
        }
    }
}
