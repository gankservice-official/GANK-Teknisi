package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceTicketDao {
    @Query("SELECT * FROM service_tickets ORDER BY createdAt DESC")
    fun getAllTickets(): Flow<List<ServiceTicketEntity>>

    @Query("SELECT * FROM service_tickets WHERE id = :id")
    suspend fun getTicketById(id: String): ServiceTicketEntity?

    @Query("SELECT * FROM service_tickets WHERE status = :status ORDER BY createdAt DESC")
    fun getTicketsByStatus(status: String): Flow<List<ServiceTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: ServiceTicketEntity)

    @Update
    suspend fun updateTicket(ticket: ServiceTicketEntity)

    @Query("DELETE FROM service_tickets WHERE id = :id")
    suspend fun deleteTicketById(id: String)

    @Query("DELETE FROM service_tickets WHERE invoiceNumber LIKE 'GNK-20260727-%' OR customerName IN ('Budi Santoso', 'Rina Wijaya', 'Ahmad Hidayat')")
    suspend fun deleteDummyTickets()

    @Query("DELETE FROM service_tickets")
    suspend fun deleteAllTickets()
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)
}

@Dao
interface SparepartDao {
    @Query("SELECT * FROM spareparts ORDER BY name ASC")
    fun getAllSpareparts(): Flow<List<SparepartEntity>>

    @Query("SELECT * FROM spareparts WHERE stock <= minStockAlert ORDER BY stock ASC")
    fun getLowStockSpareparts(): Flow<List<SparepartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSparepart(sparepart: SparepartEntity)

    @Update
    suspend fun updateSparepart(sparepart: SparepartEntity)

    @Query("DELETE FROM spareparts WHERE id = :id")
    suspend fun deleteSparepartById(id: String)

    @Query("DELETE FROM spareparts WHERE supplier IN ('GANK Sparepart Central', 'Sub-Board Jaya', 'PowerPro Battery', 'GANK Chipset Store') OR barcode LIKE 'SP-%'")
    suspend fun deleteDummySpareparts()

    @Query("DELETE FROM spareparts")
    suspend fun deleteAllSpareparts()
}

@Dao
interface RepairLogDao {
    @Query("SELECT * FROM repair_logs WHERE serviceTicketId = :serviceTicketId ORDER BY timestamp DESC")
    fun getLogsForTicket(serviceTicketId: String): Flow<List<RepairLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: RepairLogEntity)
}
