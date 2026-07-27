package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ServiceTicketEntity
import com.example.ui.components.NeoBrutalistBadge
import com.example.ui.components.NeoBrutalistButton
import com.example.ui.components.NeoBrutalistCard
import com.example.ui.theme.GankColors
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ServiceDetailScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavigateToInvoice: (ServiceTicketEntity) -> Unit
) {
    val selectedTicket by viewModel.selectedTicket.collectAsState()
    val repairLogs by viewModel.currentLogs.collectAsState()

    var showAddLogDialog by remember { mutableStateOf(false) }
    var logTitleInput by remember { mutableStateOf("") }
    var logDescInput by remember { mutableStateOf("") }

    val ticket = selectedTicket ?: return

    val statusList = listOf("Menunggu", "Diagnosa", "Pengerjaan", "QC", "Selesai", "Diambil")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = GankColors.Ink)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "DETAIL SERVIS HP",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = ticket.invoiceNumber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = GankColors.Steel,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Status Flow Controller Card
        item {
            NeoBrutalistCard(
                backgroundColor = GankColors.Ink,
                shadowColor = GankColors.GankYellow
            ) {
                Text(
                    text = "STATUS SERVIS SAAT INI:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = GankColors.Silver,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ticket.status.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = GankColors.GankYellow,
                    fontFamily = FontFamily.Monospace
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "UBAH STATUS ALUR SERVIS:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = GankColors.White,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statusList.take(3).forEach { st ->
                        val isCurrent = ticket.status == st
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isCurrent) GankColors.GankYellow else GankColors.White,
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { viewModel.updateTicketStatus(ticket, st) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = st,
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp,
                                color = GankColors.Ink,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statusList.drop(3).forEach { st ->
                        val isCurrent = ticket.status == st
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isCurrent) GankColors.GankYellow else GankColors.White,
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { viewModel.updateTicketStatus(ticket, st) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = st,
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

        // Print Invoice Action Button
        item {
            NeoBrutalistButton(
                text = "CETAK / LIHAT NOTA DIGITAL (PDF)",
                onClick = { onNavigateToInvoice(ticket) },
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Receipt,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Device & Customer Card
        item {
            NeoBrutalistCard(backgroundColor = GankColors.White) {
                Text(
                    text = "INFORMASI SERVIS",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Perangkat:", fontSize = 11.sp, color = GankColors.Steel)
                        Text(
                            text = "${ticket.deviceBrand} ${ticket.deviceModel}",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = GankColors.Ink
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Biaya Estimasi:", fontSize = 11.sp, color = GankColors.Steel)
                        Text(
                            text = "Rp ${ticket.estimatedCost.toInt()}",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Pelanggan: ${ticket.customerName} (${ticket.customerPhone})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                if (ticket.devicePassword.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .background(GankColors.Paper, RoundedCornerShape(4.dp))
                            .border(1.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🔑 Kunci Layar: ${ticket.devicePassword}",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Keluhan Utama:", fontSize = 11.sp, color = GankColors.Steel)
                Text(
                    text = ticket.complaint,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = GankColors.Ink
                )
            }
        }

        // Checklist Sebelum & Sesudah Servis
        item {
            var isEditingPostChecklist by remember { mutableStateOf(false) }
            
            // Parse pre-service checklist
            val preChecklistMap = remember(ticket.checklistData) {
                val map = mutableStateMapOf<String, String>()
                ticket.checklistData.split(",").mapNotNull {
                    val parts = it.split(":")
                    if (parts.size == 2) parts[0] to parts[1] else null
                }.forEach { (k, v) -> map[k] = v }
                map
            }

            // Parse post-service checklist
            val postChecklistDataEffective = if (ticket.postChecklistData.isNotBlank()) ticket.postChecklistData else ticket.checklistData
            val postChecklistMap = remember(postChecklistDataEffective) {
                val map = mutableStateMapOf<String, String>()
                postChecklistDataEffective.split(",").mapNotNull {
                    val parts = it.split(":")
                    if (parts.size == 2) parts[0] to parts[1] else null
                }.forEach { (k, v) -> map[k] = v }
                map
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // 1. Checklist Sebelum Servis Card
                NeoBrutalistCard(backgroundColor = GankColors.White) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHECKLIST SEBELUM SERVIS",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                        NeoBrutalistBadge(
                            text = "PENERIMAAN",
                            containerColor = GankColors.Paper,
                            textColor = GankColors.Ink
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kondisi fisik & fungsi awal saat HP diterima dari pelanggan:",
                        fontSize = 11.sp,
                        color = GankColors.Steel
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        preChecklistMap.keys.toList().chunked(3).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                row.forEach { key ->
                                    val status = preChecklistMap[key] ?: "OK"
                                    val bg = when (status) {
                                        "OK" -> GankColors.GreenOK
                                        "RUSAK" -> GankColors.RedAlert
                                        else -> GankColors.Silver
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .background(GankColors.Paper, RoundedCornerShape(4.dp))
                                            .border(1.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = key, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Box(
                                                modifier = Modifier
                                                    .background(bg, RoundedCornerShape(2.dp))
                                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                                            ) {
                                                Text(text = status, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Checklist Sesudah Servis Card (Interactive QC)
                NeoBrutalistCard(
                    backgroundColor = GankColors.White,
                    shadowColor = GankColors.GankYellow
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHECKLIST SESUDAH SERVIS (QC)",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                        NeoBrutalistBadge(
                            text = "HASIL REPAIR",
                            containerColor = GankColors.GankYellow,
                            textColor = GankColors.Ink
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tekan poin untuk memperbarui kondisi fungsi & fisik SETELAH SELESAI PERBAIKAN:",
                        fontSize = 11.sp,
                        color = GankColors.Steel
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        postChecklistMap.keys.toList().chunked(2).forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                pair.forEach { key ->
                                    val status = postChecklistMap[key] ?: "OK"
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
                                                postChecklistMap[key] = when (status) {
                                                    "OK" -> "RUSAK"
                                                    "RUSAK" -> "N/A"
                                                    else -> "OK"
                                                }
                                                isEditingPostChecklist = true
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = key,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
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

                    Spacer(modifier = Modifier.height(12.dp))
                    NeoBrutalistButton(
                        text = "SIMPAN HASIL CHECKLIST QC",
                        onClick = {
                            val preStr = preChecklistMap.entries.joinToString(",") { "${it.key}:${it.value}" }
                            val postStr = postChecklistMap.entries.joinToString(",") { "${it.key}:${it.value}" }
                            viewModel.updateTicketChecklists(ticket, preStr, postStr)
                            isEditingPostChecklist = false
                        },
                        containerColor = if (isEditingPostChecklist) GankColors.GankYellow else GankColors.Paper,
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Repair Log History Timeline
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CATATAN PERBAIKAN TEKNISI",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Box(
                    modifier = Modifier
                        .background(GankColors.NeonBlue, RoundedCornerShape(4.dp))
                        .border(2.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                        .clickable { showAddLogDialog = true }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+ TULIS CATATAN",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        if (repairLogs.isEmpty()) {
            item {
                Text(
                    text = "Belum ada catatan pengerjaan tambahan.",
                    fontSize = 12.sp,
                    color = GankColors.Steel
                )
            }
        } else {
            items(repairLogs) { log ->
                NeoBrutalistCard(backgroundColor = GankColors.White) {
                    Text(
                        text = log.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = GankColors.Ink
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = log.description,
                        fontSize = 12.sp,
                        color = GankColors.Steel
                    )
                }
            }
        }
    }

    // Add Log Dialog
    if (showAddLogDialog) {
        AlertDialog(
            onDismissRequest = { showAddLogDialog = false },
            title = {
                Text("Tambah Catatan Pengerjaan", fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = logTitleInput,
                        onValueChange = { logTitleInput = it },
                        label = { Text("Judul (mis. Ganti IC Power / Reball)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GankColors.Ink,
                            unfocusedTextColor = GankColors.Ink,
                            focusedContainerColor = GankColors.Paper,
                            unfocusedContainerColor = GankColors.Paper,
                            focusedBorderColor = GankColors.Ink,
                            unfocusedBorderColor = GankColors.Steel
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = logDescInput,
                        onValueChange = { logDescInput = it },
                        label = { Text("Detail Catatan / Komponen Diganti") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = GankColors.Ink,
                            unfocusedTextColor = GankColors.Ink,
                            focusedContainerColor = GankColors.Paper,
                            unfocusedContainerColor = GankColors.Paper,
                            focusedBorderColor = GankColors.Ink,
                            unfocusedBorderColor = GankColors.Steel
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (logTitleInput.isNotBlank()) {
                            viewModel.addRepairLog(ticket.id, logTitleInput, logDescInput)
                            logTitleInput = ""
                            logDescInput = ""
                            showAddLogDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GankColors.GankYellow)
                ) {
                    Text("SIMPAN", fontWeight = FontWeight.Black, color = GankColors.Ink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddLogDialog = false }) {
                    Text("BATAL")
                }
            }
        )
    }
}
