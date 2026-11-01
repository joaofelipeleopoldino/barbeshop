package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    MASTER,        // Acesso total a tudo, gestão global SaaS e licenças
    COMMERCIAL,    // Cliente comercial (dono da barbearia que contrata a licença)
    CLIENT         // Cliente consumidor (agenda serviços e compra produtos)
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val phone: String = "",
    val role: String, // "MASTER", "COMMERCIAL", "CLIENT"
    val businessName: String = "", // Nome da barbearia para o cliente comercial
    val licensePlan: String = "PADRAO", // "MASTER_UNLIMITED", "COMMERCIAL_PRO", "CLIENT_FREE"
    val licenseStatus: String = "ATIVA", // "ATIVA", "VENCIDA", "TRIAL"
    val licenseExpiration: String = "31/12/2026",
    val authProvider: String = "EMAIL", // "EMAIL", "GOOGLE", "FACEBOOK", "INSTAGRAM"
    val isActive: Boolean = true,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "commercial_licenses")
data class CommercialLicenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val businessName: String,
    val cnpjOrCpf: String,
    val ownerName: String,
    val ownerEmail: String,
    val planName: String, // "BarberPro Pro SaaS", "BarberPro Enterprise"
    val monthlyFee: Double,
    val status: String, // "ATIVA", "PENDENTE", "SUSPENSA"
    val maxBarbers: Int,
    val currentBarbersCount: Int,
    val validUntil: String,
    val financialStatus: String = "EM DIA"
)

@Entity(tableName = "services")
data class BarbershopServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // Cabelo, Barba, Combos, Tratamentos
    val description: String,
    val durationMinutes: Int,
    val price: Double,
    val isPopular: Boolean = false,
    val isActive: Boolean = true
)

@Entity(tableName = "barbers")
data class BarberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val nickname: String,
    val role: String,
    val rating: Double,
    val reviewCount: Int,
    val specialties: String,
    val bio: String,
    val commissionRate: Double = 0.50, // 50% SaaS barber commission
    val isAvailable: Boolean = true
)

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String,
    val clientPhone: String,
    val barberId: Int,
    val barberName: String,
    val serviceNames: String,
    val totalPrice: Double,
    val totalDurationMinutes: Int,
    val appointmentDate: String, // ex: "02/11/2026"
    val appointmentTime: String, // ex: "14:30"
    val status: String = "CONFIRMADO", // CONFIRMADO, EM_ATENDIMENTO, CONCLUIDO, CANCELADO
    val notes: String = "",
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // Pomadas, Barba, Cabelo, Acessórios
    val description: String,
    val price: Double,
    val stockQuantity: Int,
    val rating: Double,
    val volumeOrSize: String,
    val isFeatured: Boolean = false
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val productName: String,
    val productCategory: String,
    val price: Double,
    val quantity: Int
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderNumber: String,
    val itemsSummary: String,
    val totalAmount: Double,
    val paymentMethod: String, // PIX, CARTAO
    val status: String = "PAGO", // PAGO, EM_SEPARACAO, CONCLUIDO
    val pixCode: String = "",
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

// UI and Business models for lookbook, subscription clubs and SaaS insights
data class LookbookStyle(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val recommendedBeard: String,
    val maintenance: String,
    val tags: List<String>
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val priceMonthly: Double,
    val badge: String,
    val features: List<String>,
    val isPopular: Boolean = false
)

data class SaaSOverviewMetrics(
    val dailyRevenue: Double,
    val monthlyRevenue: Double,
    val todayAppointmentsCount: Int,
    val averageTicket: Double,
    val occupancyRatePercentage: Int,
    val pendingAppointmentsCount: Int,
    val completedAppointmentsCount: Int
)

data class BarberCommissionSummary(
    val barberId: Int,
    val barberName: String,
    val role: String,
    val appointmentsCount: Int,
    val totalBilled: Double,
    val commissionAmount: Double
)
