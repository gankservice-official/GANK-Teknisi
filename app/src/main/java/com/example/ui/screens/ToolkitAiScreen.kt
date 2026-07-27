package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.GankColors
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ToolkitAiScreen(
    viewModel: MainViewModel
) {
    var brandModelInput by remember { mutableStateOf("") }
    var symptomsInput by remember { mutableStateOf("") }

    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val aiResult by viewModel.aiDiagnosisResult.collectAsState()

    val quickPresets = listOf(
        "Mati Total (MATOT)" to "HP mati total tiba-tiba, tidak merespon cas dan tombol power.",
        "LCD Blank / Garis" to "Layar ada garis-garis / blank hitam tapi HP masih getar.",
        "Tidak Bisa Dicas" to "Konektor cas longgar, persentase baterai tidak naik saat dicolok.",
        "Short Circuit" to "Baterai cepat panas, boros, ada indikasi konslet di PCB.",
        "Bootloop Logo" to "Nyangkut di logo awal, tidak mau masuk ke halaman home."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            NeoBrutalistCard(
                backgroundColor = GankColors.Ink,
                shadowColor = GankColors.GankYellow
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GankColors.GankYellow,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI HARDWARE DIAGNOSTICS",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = GankColors.White,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "GEMINI 3.5 FLASH • SKEMA & ANALISA IC",
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = GankColors.GankYellow,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Input Form Card
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "MASUKKAN DETAIL KERUSAKAN HP:",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                NeoBrutalistTextField(
                    value = brandModelInput,
                    onValueChange = { brandModelInput = it },
                    label = "Tipe / Brand HP *",
                    placeholder = "mis. Samsung A52 / iPhone 12",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                NeoBrutalistTextField(
                    value = symptomsInput,
                    onValueChange = { symptomsInput = it },
                    label = "Gejala Kerusakan / Hasil Pengukuran DC *",
                    placeholder = "mis. Mati total, konslet 0.2A",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "PRESET KELUHAN UMUM:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = GankColors.Steel,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    quickPresets.forEach { (title, desc) ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GankColors.Paper, RoundedCornerShape(6.dp))
                                .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                .clickable {
                                    symptomsInput = desc
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "⚡ $title",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = GankColors.Ink,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NeoBrutalistButton(
                    text = if (isAiLoading) "MENGANALISA MODEL..." else "ANALISA DENGAN AI",
                    onClick = {
                        val model = if (brandModelInput.isNotBlank()) brandModelInput else "Smartphone Umum"
                        val symptoms = if (symptomsInput.isNotBlank()) symptomsInput else "Mati Total"
                        viewModel.runAiDiagnosis(model, symptoms)
                    },
                    enabled = !isAiLoading,
                    containerColor = GankColors.GankYellow,
                    icon = Icons.Default.Psychology,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // AI Result Output Area
        if (isAiLoading) {
            item {
                NeoBrutalistCard(backgroundColor = GankColors.White) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = GankColors.Ink)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Sedang menghubungkan ke Gemini AI & menganalisa skema hardware...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        } else if (aiResult.isNotBlank()) {
            item {
                NeoBrutalistCard(
                    backgroundColor = GankColors.White,
                    borderWidth = 3.dp,
                    shadowColor = GankColors.Ink
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HASIL DIAGNOSA MASTER TEKNISI AI",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                        NeoBrutalistBadge(
                            text = "GEMINI 3.5",
                            containerColor = GankColors.NeonBlue,
                            textColor = GankColors.Ink
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(GankColors.White, RoundedCornerShape(6.dp))
                            .border(2.5.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = aiResult,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GankColors.Ink,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
