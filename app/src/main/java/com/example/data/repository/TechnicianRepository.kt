package com.example.data.repository

import com.example.data.local.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TechnicianRepository(private val db: AppDatabase) {

    val allTickets: Flow<List<ServiceTicketEntity>> = db.serviceTicketDao().getAllTickets()
    val allCustomers: Flow<List<CustomerEntity>> = db.customerDao().getAllCustomers()
    val allSpareparts: Flow<List<SparepartEntity>> = db.sparepartDao().getAllSpareparts()
    val lowStockSpareparts: Flow<List<SparepartEntity>> = db.sparepartDao().getLowStockSpareparts()

    suspend fun getTicketById(id: String): ServiceTicketEntity? {
        return db.serviceTicketDao().getTicketById(id)
    }

    fun getLogsForTicket(ticketId: String): Flow<List<RepairLogEntity>> {
        return db.repairLogDao().getLogsForTicket(ticketId)
    }

    suspend fun insertTicket(ticket: ServiceTicketEntity) = withContext(Dispatchers.IO) {
        db.serviceTicketDao().insertTicket(ticket)
    }

    suspend fun updateTicket(ticket: ServiceTicketEntity) = withContext(Dispatchers.IO) {
        db.serviceTicketDao().updateTicket(ticket)
    }

    suspend fun deleteTicket(id: String) = withContext(Dispatchers.IO) {
        db.serviceTicketDao().deleteTicketById(id)
    }

    suspend fun insertSparepart(sparepart: SparepartEntity) = withContext(Dispatchers.IO) {
        db.sparepartDao().insertSparepart(sparepart)
    }

    suspend fun updateSparepart(sparepart: SparepartEntity) = withContext(Dispatchers.IO) {
        db.sparepartDao().updateSparepart(sparepart)
    }

    suspend fun deleteSparepart(id: String) = withContext(Dispatchers.IO) {
        db.sparepartDao().deleteSparepartById(id)
    }

    suspend fun insertRepairLog(log: RepairLogEntity) = withContext(Dispatchers.IO) {
        db.repairLogDao().insertLog(log)
    }

    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        // Seed default sample data if no service tickets exist
        // This gives immediate visual feedback for the user
        val sampleTickets = listOf(
            ServiceTicketEntity(
                invoiceNumber = "GNK-20260727-001",
                customerId = "cust-1",
                customerName = "Budi Santoso",
                customerPhone = "081234567890",
                customerAddress = "Jl. Merdeka No. 45, Jakarta",
                deviceBrand = "Xiaomi",
                deviceModel = "Redmi Note 11 Pro",
                deviceImei = "861234059876543",
                devicePassword = "PIN 1234",
                complaint = "Layar pecah & touch bagian atas tidak bisa ditekan",
                diagnosis = "Ganti Assembly LCD Original Crown",
                solution = "LCD diganti, QC Touch & Display PASSED",
                status = "Selesai",
                downPayment = 100000.0,
                estimatedCost = 450000.0,
                finalCost = 450000.0,
                warrantyDays = 30,
                checklistData = "LCD:OK,Touch:OK,Speaker:OK,Mic:OK,Wifi:OK,Bluetooth:OK,GPS:OK,SIM:OK,Fingerprint:OK,FaceUnlock:OK,Charging:OK,Camera:OK,Flash:OK,Sensor:OK,Vibrator:OK"
            ),
            ServiceTicketEntity(
                invoiceNumber = "GNK-20260727-002",
                customerId = "cust-2",
                customerName = "Rina Wijaya",
                customerPhone = "085678901234",
                customerAddress = "Jl. Mawar No. 12, Bandung",
                deviceBrand = "Samsung",
                deviceModel = "Galaxy A52",
                deviceImei = "359876123450987",
                devicePassword = "Pola L",
                complaint = "Tidak bisa dicas & HP kadang panas saat standby",
                diagnosis = "Papan charger sub-board korosi akibat lembab",
                solution = "Pengerjaan ganti papan charger Sub-Board + IC OVP",
                status = "Pengerjaan",
                downPayment = 50000.0,
                estimatedCost = 250000.0,
                finalCost = 250000.0,
                warrantyDays = 30,
                checklistData = "LCD:OK,Touch:OK,Speaker:OK,Mic:OK,Wifi:OK,Bluetooth:OK,GPS:OK,SIM:OK,Fingerprint:OK,FaceUnlock:OK,Charging:FAIL,Camera:OK,Flash:OK,Sensor:OK,Vibrator:OK"
            ),
            ServiceTicketEntity(
                invoiceNumber = "GNK-20260727-003",
                customerId = "cust-3",
                customerName = "Ahmad Hidayat",
                customerPhone = "087711223344",
                customerAddress = "Jl. Sudirman No. 88, Surabaya",
                deviceBrand = "iPhone",
                deviceModel = "iPhone 12 Pro",
                deviceImei = "351234098765432",
                devicePassword = "Passcode 000000",
                complaint = "Mati total setelah jatuh dari motor",
                diagnosis = "Jalur VPH_PWR short ke ground, IC Power PMIC perlu reball/replacement",
                solution = "",
                status = "Diagnosa",
                downPayment = 200000.0,
                estimatedCost = 850000.0,
                finalCost = 0.0,
                warrantyDays = 30,
                checklistData = "LCD:UNKNOWN,Touch:UNKNOWN,Speaker:UNKNOWN,Mic:UNKNOWN,Wifi:UNKNOWN,Bluetooth:UNKNOWN,GPS:UNKNOWN,SIM:UNKNOWN,Fingerprint:UNKNOWN,FaceUnlock:UNKNOWN,Charging:FAIL,Camera:UNKNOWN,Flash:UNKNOWN,Sensor:UNKNOWN,Vibrator:UNKNOWN"
            )
        )

        val sampleSpareparts = listOf(
            SparepartEntity(
                name = "LCD Assembly Redmi Note 11 Pro Black",
                category = "LCD / Touchscreen",
                stock = 4,
                minStockAlert = 2,
                costPrice = 280000.0,
                sellingPrice = 450000.0,
                supplier = "GANK Sparepart Central",
                barcode = "SP-LCD-RN11P",
                rackLocation = "Rak LCD-1"
            ),
            SparepartEntity(
                name = "Papan Cas Samsung A52 Original Sub-Board",
                category = "Charging Port",
                stock = 1,
                minStockAlert = 3,
                costPrice = 80000.0,
                sellingPrice = 180000.0,
                supplier = "Sub-Board Jaya",
                barcode = "SP-CAS-A52",
                rackLocation = "Rak CHARGE-2"
            ),
            SparepartEntity(
                name = "Baterai iPhone 12 Pro High Capacity 3100mAh",
                category = "Baterai",
                stock = 5,
                minStockAlert = 2,
                costPrice = 190000.0,
                sellingPrice = 380000.0,
                supplier = "PowerPro Battery",
                barcode = "SP-BAT-IP12P",
                rackLocation = "Rak BAT-A"
            ),
            SparepartEntity(
                name = "IC Power PM8350 Qualcomm Original",
                category = "IC Component",
                stock = 2,
                minStockAlert = 3,
                costPrice = 120000.0,
                sellingPrice = 250000.0,
                supplier = "GANK Chipset Store",
                barcode = "SP-IC-PM8350",
                rackLocation = "Rak IC-Laci 4"
            )
        )

        for (ticket in sampleTickets) {
            db.serviceTicketDao().insertTicket(ticket)
        }
        for (sp in sampleSpareparts) {
            db.sparepartDao().insertSparepart(sp)
        }
    }
}
