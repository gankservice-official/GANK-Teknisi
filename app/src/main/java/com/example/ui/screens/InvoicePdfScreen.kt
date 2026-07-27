package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvoicePdfScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val selectedTicket by viewModel.selectedTicket.collectAsState()
    val storeProfile by viewModel.storeProfile.collectAsState()
    val context = LocalContext.current

    val ticket = selectedTicket ?: return

    val remainingCost = ticket.estimatedCost - ticket.downPayment
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(ticket.createdAt))

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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = GankColors.Ink)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "NOTA DIGITAL SERVIS",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Receipt Card
        item {
            NeoBrutalistCard(
                backgroundColor = GankColors.White,
                borderWidth = 3.dp,
                shadowColor = GankColors.Ink
            ) {
                // Header
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = storeProfile.name,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = storeProfile.tagline.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = GankColors.Steel,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "WA: ${storeProfile.phone} • ${storeProfile.address}",
                        fontSize = 10.sp,
                        color = GankColors.Steel
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(GankColors.Ink)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Invoice Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "NO NOTA:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                        Text(text = ticket.invoiceNumber, fontSize = 13.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "TANGGAL:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                        Text(text = dateStr, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "PELANGGAN: ${ticket.customerName} (${ticket.customerPhone})", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "PERANGKAT: ${ticket.deviceBrand} ${ticket.deviceModel}", fontSize = 12.sp, fontWeight = FontWeight.Black)
                if (ticket.deviceImei.isNotBlank()) {
                    Text(text = "IMEI/SN: ${ticket.deviceImei}", fontSize = 11.sp, color = GankColors.Steel)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "KELUHAN / DIAGNOSA:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel)
                Text(text = ticket.complaint, fontSize = 12.sp, fontWeight = FontWeight.Medium)

                // Checklist Summary on Invoice
                if (ticket.checklistData.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "CHECKLIST KONDISI PERANGKAT:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GankColors.Steel, fontFamily = FontFamily.Monospace)
                    
                    val preSummary = ticket.checklistData.split(",").mapNotNull {
                        val p = it.split(":")
                        if (p.size == 2) "${p[0]}: ${p[1]}" else null
                    }.take(6).joinToString(" • ")

                    Text(text = "• Sebelum: $preSummary ...", fontSize = 9.sp, color = GankColors.Ink)

                    if (ticket.postChecklistData.isNotBlank()) {
                        val postSummary = ticket.postChecklistData.split(",").mapNotNull {
                            val p = it.split(":")
                            if (p.size == 2) "${p[0]}: ${p[1]}" else null
                        }.take(6).joinToString(" • ")
                        Text(text = "• Sesudah (QC): $postSummary ...", fontSize = 9.sp, color = GankColors.Ink)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(GankColors.Silver)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Pricing Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Estimasi Biaya Servis:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Rp ${ticket.estimatedCost.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Uang Muka (DP):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "- Rp ${ticket.downPayment.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "SISA PELAKSANAAN:", fontSize = 14.sp, fontWeight = FontWeight.Black)
                    Text(
                        text = "Rp ${remainingCost.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GankColors.Paper, RoundedCornerShape(4.dp))
                        .border(1.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "SYARAT & KETENTUAN GARANSI:",
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "1. Garansi pengerjaan berlaku ${ticket.warrantyDays} hari sejak tanggal pengambilan.\n2. Segel utuh & nota digital wajib ditunjukkan.\n3. Garansi gugur jika terkena cairan / pecah fisik / Human Error.",
                            fontSize = 9.sp,
                            lineHeight = 12.sp,
                            color = GankColors.Steel
                        )
                    }
                }
            }
        }

        // WhatsApp Share Action
        item {
            NeoBrutalistButton(
                text = "KIRIM NOTA KE WHATSAPP PELANGGAN",
                onClick = {
                    val message = """
                        *${storeProfile.name.uppercase()} - NOTA DIGITAL SERVIS HP*
                        No. Nota: ${ticket.invoiceNumber}
                        Perangkat: ${ticket.deviceBrand} ${ticket.deviceModel}
                        Pelanggan: ${ticket.customerName}
                        Keluhan: ${ticket.complaint}
                        
                        Estimasi Biaya: Rp ${ticket.estimatedCost.toInt()}
                        DP: Rp ${ticket.downPayment.toInt()}
                        Sisa Pelunasan: Rp ${remainingCost.toInt()}
                        
                        Status: *${ticket.status.uppercase()}*
                        Garansi: ${ticket.warrantyDays} Hari
                        
                        Terima kasih telah mempercayakan servis HP Anda di ${storeProfile.name}!
                    """.trimIndent()

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://api.whatsapp.com/send?phone=${ticket.customerPhone}&text=${Uri.encode(message)}")
                    }
                    context.startActivity(intent)
                },
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Share,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
