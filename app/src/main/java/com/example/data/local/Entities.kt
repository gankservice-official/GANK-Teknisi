package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "service_tickets")
data class ServiceTicketEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val invoiceNumber: String,
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val deviceBrand: String,
    val deviceModel: String,
    val deviceImei: String,
    val devicePassword: String = "",
    val complaint: String,
    val diagnosis: String = "",
    val solution: String = "",
    val status: String = "Menunggu", // Menunggu, Diagnosa, Menunggu Sparepart, Pengerjaan, QC, Selesai, Diambil
    val downPayment: Double = 0.0,
    val estimatedCost: Double = 0.0,
    val finalCost: Double = 0.0,
    val warrantyDays: Int = 30,
    val checklistData: String = "", // 15-point pre-service checklist string
    val postChecklistData: String = "", // 15-point post-service checklist string
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val phone: String,
    val address: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val customerId: String,
    val brand: String,
    val model: String,
    val imei: String = "",
    val chipset: String = "",
    val ram: String = "",
    val rom: String = "",
    val androidVersion: String = ""
)

@Entity(tableName = "spareparts")
data class SparepartEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val category: String = "LCD / Touchscreen",
    val stock: Int = 0,
    val minStockAlert: Int = 3,
    val costPrice: Double = 0.0,
    val sellingPrice: Double = 0.0,
    val supplier: String = "GANK Supplier",
    val barcode: String = "",
    val rackLocation: String = "Rak A"
)

@Entity(tableName = "repair_logs")
data class RepairLogEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val serviceTicketId: String,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
