package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SparepartEntity
import com.example.ui.components.NeoBrutalistBadge
import com.example.ui.components.NeoBrutalistButton
import com.example.ui.components.NeoBrutalistCard
import com.example.ui.theme.GankColors
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SparepartScreen(
    viewModel: MainViewModel
) {
    val spareparts by viewModel.allSpareparts.collectAsState()
    val lowStockSpareparts by viewModel.lowStockSpareparts.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var spName by remember { mutableStateOf("") }
    var spCategory by remember { mutableStateOf("LCD / Touchscreen") }
    var spStockText by remember { mutableStateOf("") }
    var spCostText by remember { mutableStateOf("") }
    var spSellText by remember { mutableStateOf("") }
    var spSupplier by remember { mutableStateOf("") }
    var spRackLocation by remember { mutableStateOf("Rak A") }

    val categories = listOf("LCD / Touchscreen", "Baterai", "Charging Port", "IC Component", "Flex Cable", "Lainnya")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GankColors.Paper)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "INVENTORY SPAREPART",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = GankColors.Ink,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "STOK KANIBALAN & BARU TEKNISI",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = GankColors.Steel,
                        fontFamily = FontFamily.Monospace
                    )
                }

                NeoBrutalistButton(
                    text = "+ STOK",
                    onClick = { showAddDialog = true },
                    containerColor = GankColors.GankYellow,
                    icon = Icons.Default.Add
                )
            }
        }

        // Low Stock Warning Banner
        if (lowStockSpareparts.isNotEmpty()) {
            item {
                NeoBrutalistCard(
                    backgroundColor = GankColors.RedAlert,
                    shadowColor = GankColors.Ink
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = GankColors.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PERINGATAN: ${lowStockSpareparts.size} ITEM SPAREPART HAMPIR HABIS!",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = GankColors.White,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        items(spareparts) { sp ->
            SparepartItemCard(sp)
        }
    }

    // Add Sparepart Dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text("Tambah Sparepart Baru", fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = spName,
                        onValueChange = { spName = it },
                        label = { Text("Nama Sparepart *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = spStockText,
                        onValueChange = { spStockText = it },
                        label = { Text("Jumlah Stok Saat Ini *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = spCostText,
                            onValueChange = { spCostText = it },
                            label = { Text("Harga Modal (Rp)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = spSellText,
                            onValueChange = { spSellText = it },
                            label = { Text("Harga Jual (Rp)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    OutlinedTextField(
                        value = spRackLocation,
                        onValueChange = { spRackLocation = it },
                        label = { Text("Lokasi Rak / Laci Storage") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (spName.isNotBlank()) {
                            val st = spStockText.toIntOrNull() ?: 1
                            val cst = spCostText.toDoubleOrNull() ?: 0.0
                            val sll = spSellText.toDoubleOrNull() ?: 0.0

                            viewModel.addSparepart(
                                name = spName,
                                category = spCategory,
                                stock = st,
                                costPrice = cst,
                                sellingPrice = sll,
                                supplier = if (spSupplier.isNotBlank()) spSupplier else "Supplier GANK",
                                barcode = "SP-${System.currentTimeMillis().toString().takeLast(6)}",
                                rackLocation = spRackLocation
                            )
                            spName = ""
                            spStockText = ""
                            spCostText = ""
                            spSellText = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GankColors.GankYellow)
                ) {
                    Text("SIMPAN STOK", fontWeight = FontWeight.Black, color = GankColors.Ink)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("BATAL")
                }
            }
        )
    }
}

@Composable
fun SparepartItemCard(sp: SparepartEntity) {
    val isLowStock = sp.stock <= sp.minStockAlert
    val marginProfit = sp.sellingPrice - sp.costPrice

    NeoBrutalistCard(backgroundColor = GankColors.White) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeoBrutalistBadge(
                        text = sp.category.uppercase(),
                        containerColor = GankColors.Paper,
                        textColor = GankColors.Ink
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    NeoBrutalistBadge(
                        text = "RAK: ${sp.rackLocation}",
                        containerColor = GankColors.NeonBlue,
                        textColor = GankColors.Ink
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = sp.name,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = GankColors.Ink
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Supplier: ${sp.supplier} • Barcode: ${sp.barcode.ifEmpty { "-" }}",
                    fontSize = 11.sp,
                    color = GankColors.Steel
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                NeoBrutalistBadge(
                    text = "STOK: ${sp.stock}",
                    containerColor = if (isLowStock) GankColors.RedAlert else GankColors.GankYellow,
                    textColor = if (isLowStock) GankColors.White else GankColors.Ink
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Jual: Rp ${sp.sellingPrice.toInt()}",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = GankColors.Ink,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Margin: +Rp ${marginProfit.toInt()}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GankColors.GreenOK,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
