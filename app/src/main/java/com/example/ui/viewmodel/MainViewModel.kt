package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.RepairLogEntity
import com.example.data.local.ServiceTicketEntity
import com.example.data.local.SparepartEntity
import com.example.data.remote.GeminiAiService
import com.example.data.repository.TechnicianRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TechnicianRepository
    
    val allTickets: StateFlow<List<ServiceTicketEntity>>
    val allSpareparts: StateFlow<List<SparepartEntity>>
    val lowStockSpareparts: StateFlow<List<SparepartEntity>>

    private val _selectedTicket = MutableStateFlow<ServiceTicketEntity?>(null)
    val selectedTicket: StateFlow<ServiceTicketEntity?> = _selectedTicket.asStateFlow()

    private val _currentLogs = MutableStateFlow<List<RepairLogEntity>>(emptyList())
    val currentLogs: StateFlow<List<RepairLogEntity>> = _currentLogs.asStateFlow()

    private val _statusFilter = MutableStateFlow("Semua")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // AI Diagnostics State
    private val _aiDiagnosisResult = MutableStateFlow("")
    val aiDiagnosisResult: StateFlow<String> = _aiDiagnosisResult.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TechnicianRepository(database)

        allTickets = repository.allTickets.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allSpareparts = repository.allSpareparts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        lowStockSpareparts = repository.lowStockSpareparts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun setStatusFilter(filter: String) {
        _statusFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectTicket(ticket: ServiceTicketEntity) {
        _selectedTicket.value = ticket
        viewModelScope.launch {
            repository.getLogsForTicket(ticket.id).collect { logs ->
                _currentLogs.value = logs
            }
        }
    }

    fun createServiceTicket(
        invoiceNumber: String = "",
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        deviceBrand: String,
        deviceModel: String,
        deviceImei: String,
        devicePassword: String,
        complaint: String,
        estimatedCost: Double,
        downPayment: Double,
        checklistData: String,
        postChecklistData: String = ""
    ) {
        viewModelScope.launch {
            val count = allTickets.value.size + 1
            val finalInvoiceNo = if (invoiceNumber.isNotBlank()) invoiceNumber.trim() else "GNK-${1000 + count}"
            val newTicket = ServiceTicketEntity(
                invoiceNumber = finalInvoiceNo,
                customerId = "cust-${System.currentTimeMillis()}",
                customerName = customerName,
                customerPhone = customerPhone,
                customerAddress = customerAddress,
                deviceBrand = deviceBrand,
                deviceModel = deviceModel,
                deviceImei = deviceImei,
                devicePassword = devicePassword,
                complaint = complaint,
                estimatedCost = estimatedCost,
                downPayment = downPayment,
                checklistData = checklistData,
                postChecklistData = if (postChecklistData.isNotBlank()) postChecklistData else checklistData,
                status = "Menunggu"
            )
            repository.insertTicket(newTicket)
            // Initial log
            repository.insertRepairLog(
                RepairLogEntity(
                    serviceTicketId = newTicket.id,
                    title = "Penerimaan Servis Baru",
                    description = "Perangkat diterima dengan keluhan: $complaint. DP: Rp ${downPayment.toInt()}"
                )
            )
        }
    }

    fun updateTicketChecklists(ticket: ServiceTicketEntity, preChecklist: String, postChecklist: String) {
        viewModelScope.launch {
            val updated = ticket.copy(
                checklistData = preChecklist,
                postChecklistData = postChecklist,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateTicket(updated)
            _selectedTicket.value = updated
            repository.insertRepairLog(
                RepairLogEntity(
                    serviceTicketId = ticket.id,
                    title = "Update Checklist Fisik & Fungsi",
                    description = "Teknisi memperbarui checklist kondisi HP (Sebelum & Sesudah Servis)."
                )
            )
        }
    }

    fun updateTicketStatus(ticket: ServiceTicketEntity, newStatus: String, notes: String = "") {
        viewModelScope.launch {
            val updated = ticket.copy(
                status = newStatus,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateTicket(updated)
            _selectedTicket.value = updated
            repository.insertRepairLog(
                RepairLogEntity(
                    serviceTicketId = ticket.id,
                    title = "Update Status: $newStatus",
                    description = if (notes.isNotBlank()) notes else "Status servis diperbarui menjadi $newStatus"
                )
            )
        }
    }

    fun addRepairLog(ticketId: String, title: String, description: String) {
        viewModelScope.launch {
            repository.insertRepairLog(
                RepairLogEntity(
                    serviceTicketId = ticketId,
                    title = title,
                    description = description
                )
            )
        }
    }

    fun addSparepart(
        name: String,
        category: String,
        stock: Int,
        costPrice: Double,
        sellingPrice: Double,
        supplier: String,
        barcode: String,
        rackLocation: String
    ) {
        viewModelScope.launch {
            val newSp = SparepartEntity(
                name = name,
                category = category,
                stock = stock,
                costPrice = costPrice,
                sellingPrice = sellingPrice,
                supplier = supplier,
                barcode = barcode,
                rackLocation = rackLocation
            )
            repository.insertSparepart(newSp)
        }
    }

    fun runAiDiagnosis(brandModel: String, symptoms: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiDiagnosisResult.value = ""
            val result = GeminiAiService.analyzeServiceIssue(brandModel, symptoms)
            _aiDiagnosisResult.value = result
            _isAiLoading.value = false
        }
    }

    fun deleteDummyData() {
        viewModelScope.launch {
            repository.deleteDummyData()
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            repository.deleteAllData()
        }
    }
}
