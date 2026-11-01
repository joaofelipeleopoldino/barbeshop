package com.example.data.repository

import com.example.data.local.BarberDatabase
import com.example.data.model.AppointmentEntity
import com.example.data.model.BarberEntity
import com.example.data.model.BarbershopServiceEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.CommercialLicenseEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.Locale
import java.util.UUID

class BarberRepository(private val database: BarberDatabase) {

    private val userDao = database.userDao()
    private val licenseDao = database.commercialLicenseDao()
    private val serviceDao = database.serviceDao()
    private val barberDao = database.barberDao()
    private val appointmentDao = database.appointmentDao()
    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()

    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()
    val allLicenses: Flow<List<CommercialLicenseEntity>> = licenseDao.getAllLicenses()
    val activeServices: Flow<List<BarbershopServiceEntity>> = serviceDao.getAllActiveServices()
    val allServicesForManagement: Flow<List<BarbershopServiceEntity>> = serviceDao.getAllServicesForManagement()
    val barbers: Flow<List<BarberEntity>> = barberDao.getAllBarbers()
    val appointments: Flow<List<AppointmentEntity>> = appointmentDao.getAllAppointments()
    val products: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val cartItems: Flow<List<CartItemEntity>> = cartDao.getCartItems()
    val orders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    // Authentication Methods
    suspend fun authenticate(identifier: String, pass: String): UserEntity? {
        val user = userDao.getUserByUsernameOrEmail(identifier.trim())
        return if (user != null && user.password == pass.trim()) {
            user
        } else {
            null
        }
    }

    suspend fun registerUser(
        username: String,
        email: String,
        pass: String,
        name: String,
        phone: String,
        role: String,
        businessName: String
    ): UserEntity {
        val existing = userDao.getUserByUsernameOrEmail(username) ?: userDao.getUserByEmail(email)
        if (existing != null) {
            return existing
        }

        val newUser = UserEntity(
            username = username.ifBlank { email.substringBefore("@") },
            email = email,
            password = pass,
            name = name,
            phone = phone,
            role = role,
            businessName = businessName,
            licensePlan = if (role == "COMMERCIAL") "COMMERCIAL_PRO" else if (role == "MASTER") "MASTER_UNLIMITED" else "CLIENT_FREE",
            licenseStatus = "ATIVA",
            authProvider = "EMAIL"
        )
        val id = userDao.insert(newUser)

        // If commercial, also create a commercial license
        if (role == "COMMERCIAL") {
            licenseDao.insert(
                CommercialLicenseEntity(
                    businessName = businessName.ifBlank { "Barbearia de $name" },
                    cnpjOrCpf = "12.345.678/0001-99",
                    ownerName = name,
                    ownerEmail = email,
                    planName = "BarberPro Pro SaaS",
                    monthlyFee = 199.90,
                    status = "ATIVA",
                    maxBarbers = 5,
                    currentBarbersCount = 2,
                    validUntil = "31/12/2026",
                    financialStatus = "EM DIA"
                )
            )
        }

        return newUser.copy(id = id.toInt())
    }

    suspend fun socialLogin(provider: String, name: String, email: String, role: String): UserEntity {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return existing
        }

        val newUser = UserEntity(
            username = email.substringBefore("@"),
            email = email,
            password = "social_oauth_login",
            name = name,
            role = role,
            businessName = if (role == "COMMERCIAL") "Barbearia $name" else "",
            authProvider = provider.uppercase(Locale.getDefault())
        )
        val id = userDao.insert(newUser)
        return newUser.copy(id = id.toInt())
    }

    suspend fun resetPassword(emailOrUsername: String, newPass: String): Boolean {
        val rows = userDao.updatePassword(emailOrUsername.trim(), newPass.trim())
        return rows > 0
    }

    suspend fun updateLicenseStatus(licenseId: Int, status: String) {
        licenseDao.updateStatus(licenseId, status)
    }

    // Appointment Methods
    suspend fun createAppointment(
        clientName: String,
        clientPhone: String,
        barberId: Int,
        barberName: String,
        serviceNames: String,
        totalPrice: Double,
        totalDurationMinutes: Int,
        appointmentDate: String,
        appointmentTime: String,
        notes: String
    ): Long {
        val appointment = AppointmentEntity(
            clientName = clientName,
            clientPhone = clientPhone,
            barberId = barberId,
            barberName = barberName,
            serviceNames = serviceNames,
            totalPrice = totalPrice,
            totalDurationMinutes = totalDurationMinutes,
            appointmentDate = appointmentDate,
            appointmentTime = appointmentTime,
            status = "CONFIRMADO",
            notes = notes
        )
        return appointmentDao.insert(appointment)
    }

    suspend fun updateAppointmentStatus(id: Int, status: String) {
        appointmentDao.updateStatus(id, status)
    }

    suspend fun cancelAppointment(id: Int) {
        appointmentDao.updateStatus(id, "CANCELADO")
    }

    suspend fun deleteAppointment(id: Int) {
        appointmentDao.deleteById(id)
    }

    // Cart and E-Commerce Methods
    suspend fun addToCart(product: ProductEntity) {
        val existing = cartDao.getItemByProductId(product.id)
        if (existing != null) {
            cartDao.update(existing.copy(quantity = existing.quantity + 1))
        } else {
            cartDao.insert(
                CartItemEntity(
                    productId = product.id,
                    productName = product.name,
                    productCategory = product.category,
                    price = product.price,
                    quantity = 1
                )
            )
        }
    }

    suspend fun incrementCartItem(cartItem: CartItemEntity) {
        cartDao.update(cartItem.copy(quantity = cartItem.quantity + 1))
    }

    suspend fun decrementCartItem(cartItem: CartItemEntity) {
        if (cartItem.quantity > 1) {
            cartDao.update(cartItem.copy(quantity = cartItem.quantity - 1))
        } else {
            cartDao.delete(cartItem)
        }
    }

    suspend fun removeCartItem(cartItem: CartItemEntity) {
        cartDao.delete(cartItem)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun checkout(items: List<CartItemEntity>, paymentMethod: String): OrderEntity {
        val total = items.sumOf { it.price * it.quantity }
        val summary = items.joinToString(", ") { "${it.quantity}x ${it.productName}" }
        val orderNum = "BPR-" + (1000..9999).random()
        val pixKey = "00020126580014BR.GOV.BCB.PIX0136${UUID.randomUUID()}520400005303986540${String.format(Locale.US, "%.2f", total)}5802BR5915DOM NAVALHA BAR6009SAO PAULO62070503***6304"

        for (item in items) {
            productDao.deductStock(item.productId, item.quantity)
        }

        val order = OrderEntity(
            orderNumber = orderNum,
            itemsSummary = summary,
            totalAmount = total,
            paymentMethod = paymentMethod,
            status = "PAGO",
            pixCode = if (paymentMethod == "PIX") pixKey else ""
        )

        val id = orderDao.insert(order)
        cartDao.clearCart()
        return order.copy(id = id.toInt())
    }

    suspend fun addNewService(service: BarbershopServiceEntity) {
        serviceDao.insert(service)
    }

    suspend fun toggleServiceActive(serviceId: Int, isActive: Boolean) {
        serviceDao.setServiceActive(serviceId, isActive)
    }

    suspend fun toggleBarberAvailability(barberId: Int, isAvailable: Boolean) {
        barberDao.setBarberAvailability(barberId, isAvailable)
    }

    suspend fun addNewProduct(product: ProductEntity) {
        productDao.insert(product)
    }
}
