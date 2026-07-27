package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ServiceTicketEntity
import com.example.ui.components.*
import com.example.ui.theme.GankColors
import com.example.ui.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

fun Double.toRupiahFormat(): String {
    return try {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        formatter.maximumFractionDigits = 0
        formatter.format(this).replace("Rp", "Rp ").trim()
    } catch (e: Exception) {
        "Rp ${this.toInt()}"
    }
}

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onNavigateToNewIntake: () -> Unit,
    onNavigateToDetail: (ServiceTicketEntity) -> Unit,
    onNavigateToAi: () -> Unit,
    onNavigateToSpareparts: () -> Unit
) {
    val tickets by viewModel.allTickets.collectAsState()
    val lowStockSpareparts by viewModel.lowStockSpareparts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val pendingCount = tickets.count { it.status != "Selesai" && it.status != "Diambil" }
    val completedCount = tickets.count { it.status == "Selesai" || it.status == "Diambil" }
    val totalRevenue = tickets.filter { it.status == "Selesai" || it.status == "Diambil" }.sumOf { it.finalCost }

    val filteredTickets = remember(tickets, searchQuery, selectedFilter) {
        tickets.filter { ticket ->
            val matchesFilter = when (selectedFilter) {
                "Menunggu" -> ticket.status == "Menunggu" || ticket.status == "Diagnosa"
                "Pengerjaan" -> ticket.status == "Pengerjaan" || ticket.status == "Menunggu Sparepart" || ticket.status == "QC"
                "Selesai" -> ticket.status == "Selesai" || ticket.status == "Diambil"
                else -> true
            }

            val query = searchQuery.trim().lowercase()
            val matchesSearch = query.isEmpty() ||
                    ticket.invoiceNumber.lowercase().contains(query) ||
                    ticket.customerName.lowercase().contains(query) ||
                    ticket.deviceBrand.lowercase().contains(query) ||
                    ticket.deviceModel.lowercase().contains(query) ||
                    ticket.customerPhone.lowercase().contains(query) ||
                    ticket.complaint.lowercase().contains(query)

            matchesFilter && matchesSearch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // GANK SERVICE Header Banner
        item {
            NeoBrutalistCard(
                backgroundColor = GankColors.Ink,
                shadowColor = GankColors.GankYellow
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(GankColors.GankYellow, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "GANK",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = GankColors.Ink,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TEKNISI TOOLKIT",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = GankColors.White,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(GankColors.GreenOK)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "OFFLINE LOCAL DATABASE • SYSTEM ACTIVE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = GankColors.Silver,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .background(GankColors.NeonBlue, RoundedCornerShape(6.dp))
                            .border(2.dp, GankColors.White, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PRO v1.0",
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Stats Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NeoBrutalistStatCard(
                        title = "Aktif Pending",
                        value = pendingCount.toString(),
                        icon = Icons.Default.HourglassTop,
                        badgeText = "SERVIS",
                        badgeColor = GankColors.NeonBlue,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    NeoBrutalistStatCard(
                        title = "Selesai",
                        value = completedCount.toString(),
                        icon = Icons.Default.CheckCircle,
                        badgeText = "OK",
                        badgeColor = GankColors.GreenOK,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NeoBrutalistStatCard(
                        title = "Stok Kritis",
                        value = lowStockSpareparts.size.toString(),
                        icon = Icons.Default.Warning,
                        badgeText = if (lowStockSpareparts.isNotEmpty()) "CEK" else "AMAN",
                        badgeColor = if (lowStockSpareparts.isNotEmpty()) GankColors.RedAlert else GankColors.GankYellow,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onClick = onNavigateToSpareparts
                    )
                    NeoBrutalistStatCard(
                        title = "Total Omset",
                        value = totalRevenue.toRupiahFormat(),
                        icon = Icons.Default.AttachMoney,
                        badgeText = "LUNAS",
                        badgeColor = GankColors.GankYellow,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }

        // Low Stock Alert Banner (Dynamic Ticker)
        if (lowStockSpareparts.isNotEmpty()) {
            item {
                NeoBrutalistCard(
                    backgroundColor = GankColors.RedAlert,
                    shadowColor = GankColors.Ink
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = GankColors.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "PERINGATAN STOK TIPIS (${lowStockSpareparts.size} ITEM)",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = GankColors.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = lowStockSpareparts.take(2).joinToString { "${it.name} (${it.stock})" },
                                    fontSize = 11.sp,
                                    color = GankColors.Paper,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(GankColors.White, RoundedCornerShape(6.dp))
                                .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                                .clickable { onNavigateToSpareparts() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "KELOLA STOK →",
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp,
                                color = GankColors.Ink,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        // Action Shortcuts Section
        item {
            Column {
                Text(
                    text = "PINTASAN UTAMA TEKNISI",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    NeoBrutalistButton(
                        text = "+ SERVIS BARU",
                        onClick = onNavigateToNewIntake,
                        containerColor = GankColors.GankYellow,
                        icon = Icons.Default.Add,
                        modifier = Modifier.weight(1f)
                    )
                    NeoBrutalistButton(
                        text = "AI DIAGNOSA",
                        onClick = onNavigateToAi,
                        containerColor = GankColors.NeonBlue,
                        icon = Icons.Default.AutoAwesome,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Damage Statistics Section
        item {
            DamageStatisticsCard(tickets = tickets)
        }

        // Search and Quick Filter Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DAFTAR NOTA & REKAP (${filteredTickets.size})",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // NeoBrutalist Search TextField
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GankColors.White, RoundedCornerShape(8.dp))
                        .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = GankColors.Ink,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Cari invoice, pelanggan, tipe HP...",
                                    fontSize = 13.sp,
                                    color = GankColors.Steel
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = GankColors.Ink
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_search_input")
                        )
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = GankColors.Ink,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { searchQuery = "" }
                            )
                        }
                    }
                }

                // Filter Chips Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("Semua", "Menunggu", "Pengerjaan", "Selesai")
                    items(filters) { filter ->
                        NeoBrutalistFilterChip(
                            text = filter.uppercase(),
                            isSelected = selectedFilter == filter,
                            onClick = { selectedFilter = filter }
                        )
                    }
                }
            }
        }

        // Tickets List / Empty State
        if (filteredTickets.isEmpty()) {
            item {
                NeoBrutalistCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = GankColors.Steel,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "Data Tidak Ditemukan" else "Belum Ada Data Servis",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = GankColors.Ink
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty())
                                "Coba cari dengan kata kunci lain atau ubah filter status."
                            else
                                "Klik tombol '+ SERVIS BARU' untuk membuat nota penerimaan servis baru.",
                            fontSize = 12.sp,
                            color = GankColors.Steel
                        )
                    }
                }
            }
        } else {
            items(filteredTickets) { ticket ->
                ServiceTicketCardItem(
                    ticket = ticket,
                    onClick = { onNavigateToDetail(ticket) }
                )
            }
        }
    }
}

@Composable
fun ServiceTicketCardItem(
    ticket: ServiceTicketEntity,
    onClick: () -> Unit
) {
    val statusColor = when (ticket.status) {
        "Menunggu" -> GankColors.Silver
        "Diagnosa" -> GankColors.NeonBlue
        "Pengerjaan" -> GankColors.GankYellow
        "Selesai" -> GankColors.GreenOK
        "Diambil" -> GankColors.White
        else -> GankColors.Paper
    }

    NeoBrutalistCard(
        onClick = onClick,
        backgroundColor = GankColors.White,
        modifier = Modifier.testTag("ticket_item_${ticket.invoiceNumber}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Header Row: Invoice Tag & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeoBrutalistBadge(
                        text = ticket.invoiceNumber,
                        containerColor = GankColors.Ink,
                        textColor = GankColors.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NeoBrutalistBadge(
                        text = ticket.status.uppercase(),
                        containerColor = statusColor,
                        textColor = GankColors.Ink
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Smartphone,
                        contentDescription = null,
                        tint = GankColors.Ink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ticket.deviceBrand.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Title Device & Customer
            Column {
                Text(
                    text = "${ticket.deviceBrand} ${ticket.deviceModel}",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = GankColors.Ink
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Pelanggan: ${ticket.customerName} • ${ticket.customerPhone}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GankColors.Steel
                )
            }

            // Complaint summary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GankColors.Paper, RoundedCornerShape(6.dp))
                    .border(1.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.ReportProblem,
                        contentDescription = null,
                        tint = GankColors.Ink,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = ticket.complaint,
                        fontSize = 12.sp,
                        color = GankColors.Ink,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Footer Row: Pricing & Process Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Estimasi: ${ticket.estimatedCost.toRupiahFormat()}",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                    if (ticket.downPayment > 0) {
                        Text(
                            text = "DP Terbayar: ${ticket.downPayment.toRupiahFormat()}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GankColors.Steel,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(GankColors.GankYellow, RoundedCornerShape(6.dp))
                        .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "DETAIL →",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
fun DamageStatisticsCard(
    tickets: List<ServiceTicketEntity>
) {
    val damageStats = remember(tickets) {
        listOf(
            DamageStatItem("Ganti LCD", 50, GankColors.GankYellow),
            DamageStatItem("Ganti Konektor", 20, GankColors.NeonBlue),
            DamageStatItem("Ganti Tombol", 10, GankColors.GreenOK),
            DamageStatItem("Fuse Putus", 10, GankColors.RedAlert),
            DamageStatItem("Lainnya", 10, GankColors.Silver)
        )
    }

    NeoBrutalistCard(backgroundColor = GankColors.White) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = GankColors.Ink,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "STATISTIK KERUSAKAN TERBANYAK",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }

                NeoBrutalistBadge(
                    text = "ANALISIS",
                    containerColor = GankColors.GankYellow,
                    textColor = GankColors.Ink
                )
            }

            Text(
                text = "Persentase jenis kerusakan handphone paling sering ditangani:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GankColors.Steel
            )

            Spacer(modifier = Modifier.height(2.dp))

            damageStats.forEach { item ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.label,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = GankColors.Ink
                        )
                        Text(
                            text = "${item.percentage}%",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = GankColors.Ink,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .background(GankColors.Paper, RoundedCornerShape(4.dp))
                            .border(2.dp, GankColors.Ink, RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(item.percentage / 100f)
                                .background(item.color, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }
}

data class DamageStatItem(
    val label: String,
    val percentage: Int,
    val color: Color
)

