package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.ui.theme.GankColors
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ServiceListScreen(
    viewModel: MainViewModel,
    onNavigateToDetail: (ServiceTicketEntity) -> Unit,
    onNavigateToNewIntake: () -> Unit
) {
    val tickets by viewModel.allTickets.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filterOptions = listOf("Semua", "Menunggu", "Diagnosa", "Pengerjaan", "QC", "Selesai", "Diambil")

    val filteredTickets = tickets.filter { ticket ->
        val matchesStatus = if (statusFilter == "Semua") true else ticket.status.equals(statusFilter, ignoreCase = true)
        val matchesSearch = if (searchQuery.isBlank()) true else {
            ticket.invoiceNumber.contains(searchQuery, ignoreCase = true) ||
            ticket.customerName.contains(searchQuery, ignoreCase = true) ||
            ticket.deviceBrand.contains(searchQuery, ignoreCase = true) ||
            ticket.deviceModel.contains(searchQuery, ignoreCase = true) ||
            ticket.complaint.contains(searchQuery, ignoreCase = true)
        }
        matchesStatus && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DAFTAR SERVIS HP",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = GankColors.Ink,
                fontFamily = FontFamily.Monospace
            )
            NeoBrutalistButton(
                text = "+ TAMBAH",
                onClick = onNavigateToNewIntake,
                containerColor = GankColors.GankYellow,
                icon = Icons.Default.Add
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Cari Invoice / Nama Pelanggan / HP...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GankColors.Ink) },
            modifier = Modifier
                .fillMaxWidth()
                .background(GankColors.White, RoundedCornerShape(8.dp))
                .border(3.dp, GankColors.Ink, RoundedCornerShape(8.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GankColors.GankYellow,
                unfocusedBorderColor = GankColors.Ink
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Horizontal Scrollable List
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filterOptions) { filter ->
                val isSelected = statusFilter == filter
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) GankColors.GankYellow else GankColors.White,
                            RoundedCornerShape(6.dp)
                        )
                        .border(2.dp, GankColors.Ink, RoundedCornerShape(6.dp))
                        .clickable { viewModel.setStatusFilter(filter) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        fontSize = 12.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Service Tickets
        if (filteredTickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tidak ada data servis ditemukan.",
                    fontWeight = FontWeight.Bold,
                    color = GankColors.Steel,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTickets) { ticket ->
                    ServiceTicketCardItem(
                        ticket = ticket,
                        onClick = { onNavigateToDetail(ticket) }
                    )
                }
            }
        }
    }
}
