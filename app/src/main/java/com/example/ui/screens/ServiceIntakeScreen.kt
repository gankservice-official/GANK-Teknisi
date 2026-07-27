package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.GankColors
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ServiceIntakeScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSuccessSaved: () -> Unit
) {
    val tickets by viewModel.allTickets.collectAsState()
    
    var invoiceNumber by remember(tickets.size) {
        mutableStateOf("GNK-${1000 + tickets.size + 1}")
    }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }
    var deviceBrand by remember { mutableStateOf("Xiaomi") }
    var deviceModel by remember { mutableStateOf("") }
    var deviceImei by remember { mutableStateOf("") }
    var devicePassword by remember { mutableStateOf("") }
    var complaint by remember { mutableStateOf("") }
    var estimatedCostText by remember { mutableStateOf("") }
    var downPaymentText by remember { mutableStateOf("") }

    // 15-Point Checklist state
    val checklistItems = remember {
        mutableStateMapOf(
            "LCD" to "OK",
            "Touchscreen" to "OK",
            "Speaker" to "OK",
            "Mic" to "OK",
            "Wifi" to "OK",
            "Bluetooth" to "OK",
            "GPS" to "OK",
            "SIM" to "OK",
            "Fingerprint" to "OK",
            "Face Unlock" to "OK",
            "Charging" to "OK",
            "Camera" to "OK",
            "Flash" to "OK",
            "Sensor" to "OK",
            "Vibrator" to "OK"
        )
    }

    val brands = listOf("Xiaomi", "Samsung", "iPhone", "Oppo", "Vivo", "Realme", "Infinix", "Asus", "Lainnya")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = GankColors.Ink
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PENERIMAAN SERVIS BARU",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Section 1: Data Nota & Pelanggan
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "1. DATA NOTA & PELANGGAN",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                NeoBrutalistTextField(
                    value = invoiceNumber,
                    onValueChange = { invoiceNumber = it },
                    label = "Nomor Nota / Invoice * (Manual Input)",
                    placeholder = "mis. GNK-1001 / 00123 (Samakan dengan GANK Manager)",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                NeoBrutalistTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = "Nama Pelanggan *",
                    placeholder = "mis. Budi Santoso",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                NeoBrutalistTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = "Nomor HP / WhatsApp *",
                    placeholder = "mis. 08123456789",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                NeoBrutalistTextField(
                    value = customerAddress,
                    onValueChange = { customerAddress = it },
                    label = "Alamat / Catatan",
                    placeholder = "mis. Jl. Sudirman No. 12",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Section 2: Device & Complaint
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "2. DATA HANDPHONE & KERUSAKAN",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "BRAND SMARTPHONE:",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    brands.take(4).forEach { b ->
                        Box(
                            modifier = Modifier
                                .background(
                                    if (deviceBrand == b) GankColors.GankYellow else GankColors.Paper,
                                    RoundedCornerShape(6.dp)
                                )
                                .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                .clickable { deviceBrand = b }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = b,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = GankColors.Ink
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                NeoBrutalistTextField(
                    value = deviceModel,
                    onValueChange = { deviceModel = it },
                    label = "Model / Tipe HP *",
                    placeholder = "mis. Redmi Note 11 Pro",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                NeoBrutalistTextField(
                    value = deviceImei,
                    onValueChange = { deviceImei = it },
                    label = "Nomor IMEI / Serial (Opsional)",
                    placeholder = "mis. 864210987654321",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                NeoBrutalistTextField(
                    value = devicePassword,
                    onValueChange = { devicePassword = it },
                    label = "PIN / Pola / Password Kunci Layar",
                    placeholder = "mis. 1234 / Pola L",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                NeoBrutalistTextField(
                    value = complaint,
                    onValueChange = { complaint = it },
                    label = "Keluhan / Kerusakan Utama *",
                    placeholder = "mis. Mati total, layar pecah, tidak bisa ngecas",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Section 3: Cost & DP
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "3. ESTIMASI BIAYA & UANG MUKA (DP)",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NeoBrutalistTextField(
                        value = estimatedCostText,
                        onValueChange = { estimatedCostText = it },
                        label = "Estimasi Biaya (Rp)",
                        placeholder = "350000",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    NeoBrutalistTextField(
                        value = downPaymentText,
                        onValueChange = { downPaymentText = it },
                        label = "Uang Muka DP (Rp)",
                        placeholder = "100000",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section 4: 15-Point Checklist Sebelum Servis
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "4. CHECKLIST FISIK & FUNGSI SEBELUM SERVIS (15 POIN)",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tekan poin untuk menentukan kondisi fisik & fungsi SAAT HP DITERIMA (Awal):",
                    fontSize = 11.sp,
                    color = GankColors.Steel
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    checklistItems.keys.toList().chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { key ->
                                val status = checklistItems[key] ?: "OK"
                                val statusColor = when (status) {
                                    "OK" -> GankColors.GreenOK
                                    "RUSAK" -> GankColors.RedAlert
                                    else -> GankColors.Silver
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(GankColors.Paper, RoundedCornerShape(6.dp))
                                        .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                        .clickable {
                                            checklistItems[key] = when (status) {
                                                "OK" -> "RUSAK"
                                                "RUSAK" -> "N/A"
                                                else -> "OK"
                                            }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = key,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = GankColors.Ink
                                        )
                                        Box(
                                            modifier = Modifier
                                                .background(statusColor, RoundedCornerShape(4.dp))
                                                .border(1.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = status,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 10.sp,
                                                color = GankColors.Ink,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Submit Button
        item {
            val isFormValid = invoiceNumber.isNotBlank() && customerName.isNotBlank() && customerPhone.isNotBlank() && deviceModel.isNotBlank() && complaint.isNotBlank()

            NeoBrutalistButton(
                text = "SIMPAN NOTA SERVIS BARU",
                onClick = {
                    val est = estimatedCostText.toDoubleOrNull() ?: 0.0
                    val dp = downPaymentText.toDoubleOrNull() ?: 0.0
                    val checklistString = checklistItems.entries.joinToString(",") { "${it.key}:${it.value}" }

                    viewModel.createServiceTicket(
                        invoiceNumber = invoiceNumber,
                        customerName = customerName,
                        customerPhone = customerPhone,
                        customerAddress = customerAddress,
                        deviceBrand = deviceBrand,
                        deviceModel = deviceModel,
                        deviceImei = deviceImei,
                        devicePassword = devicePassword,
                        complaint = complaint,
                        estimatedCost = est,
                        downPayment = dp,
                        checklistData = checklistString,
                        postChecklistData = checklistString
                    )
                    onSuccessSaved()
                },
                enabled = isFormValid,
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Save,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
