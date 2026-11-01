package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BarberDatabase
import com.example.data.model.AppointmentEntity
import com.example.data.model.BarberCommissionSummary
import com.example.data.model.BarberEntity
import com.example.data.model.BarbershopServiceEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.CommercialLicenseEntity
import com.example.data.model.LookbookStyle
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SaaSOverviewMetrics
import com.example.data.model.SubscriptionPlan
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.BarberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppScreen {
    LOGIN,
    HOME,
    SERVICES,
    BOOKING,
    SHOP,
    MY_APPOINTMENTS,
    SAAS_DASHBOARD, // Acesso do Cliente Comercial / Dono da Barbearia
    MASTER_ADMIN    // Acesso total do Usuário Master
}

class BarberAppViewModel(application: Application) : AndroidViewModel(application) {

    private val database = BarberDatabase.getDatabase(application, viewModelScope)
    private val repository = BarberRepository(database)

    // Current authenticated user (Default to Client for customer preview, or easily switchable to Master/Commercial)
    private val _currentUser = MutableStateFlow<UserEntity?>(
        UserEntity(
            id = 3,
            username = "cliente",
            email = "gabriel.cliente@gmail.com",
            password = "cliente123",
            name = "Gabriel Albuquerque",
            phone = "(11) 98765-4321",
            role = "CLIENT",
            businessName = "",
            licensePlan = "CLIENT_VIP",
            licenseStatus = "ATIVA"
        )
    )
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Users and Licenses list for Master management
    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLicenses: StateFlow<List<CommercialLicenseEntity>> = repository.allLicenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Database Flows
    val services: StateFlow<List<BarbershopServiceEntity>> = repository.activeServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allServicesForManagement: StateFlow<List<BarbershopServiceEntity>> = repository.allServicesForManagement
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val barbers: StateFlow<List<BarberEntity>> = repository.barbers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val appointments: StateFlow<List<AppointmentEntity>> = repository.appointments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<ProductEntity>> = repository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation state
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Filters & Search
    private val _selectedServiceCategory = MutableStateFlow("Todos")
    val selectedServiceCategory: StateFlow<String> = _selectedServiceCategory.asStateFlow()

    private val _selectedProductCategory = MutableStateFlow("Todos")
    val selectedProductCategory: StateFlow<String> = _selectedProductCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Booking Flow State
    private val _bookingSelectedServices = MutableStateFlow<Set<BarbershopServiceEntity>>(emptySet())
    val bookingSelectedServices: StateFlow<Set<BarbershopServiceEntity>> = _bookingSelectedServices.asStateFlow()

    private val _bookingSelectedBarber = MutableStateFlow<BarberEntity?>(null)
    val bookingSelectedBarber: StateFlow<BarberEntity?> = _bookingSelectedBarber.asStateFlow()

    private val _bookingSelectedDate = MutableStateFlow(getTodayFormattedDate())
    val bookingSelectedDate: StateFlow<String> = _bookingSelectedDate.asStateFlow()

    private val _bookingSelectedTime = MutableStateFlow("14:00")
    val bookingSelectedTime: StateFlow<String> = _bookingSelectedTime.asStateFlow()

    private val _bookingClientName = MutableStateFlow("Gabriel Albuquerque")
    val bookingClientName: StateFlow<String> = _bookingClientName.asStateFlow()

    private val _bookingClientPhone = MutableStateFlow("(11) 98765-4321")
    val bookingClientPhone: StateFlow<String> = _bookingClientPhone.asStateFlow()

    private val _bookingNotes = MutableStateFlow("")
    val bookingNotes: StateFlow<String> = _bookingNotes.asStateFlow()

    private val _lastBookedAppointment = MutableStateFlow<AppointmentEntity?>(null)
    val lastBookedAppointment: StateFlow<AppointmentEntity?> = _lastBookedAppointment.asStateFlow()

    // Cart & Checkout State
    private val _isCartSheetOpen = MutableStateFlow(false)
    val isCartSheetOpen: StateFlow<Boolean> = _isCartSheetOpen.asStateFlow()

    private val _lastCompletedOrder = MutableStateFlow<OrderEntity?>(null)
    val lastCompletedOrder: StateFlow<OrderEntity?> = _lastCompletedOrder.asStateFlow()

    // Loyalty Stamps
    private val _loyaltyStamps = MutableStateFlow(7)
    val loyaltyStamps: StateFlow<Int> = _loyaltyStamps.asStateFlow()

    // Lookbook Styles
    val lookbookStyles = listOf(
        LookbookStyle(
            id = "fade_navalhado",
            title = "Skin Fade Navalhado",
            category = "Cortes Modernos",
            description = "Degradê raspado até a pele com navalha/shaver, subindo em transição perfeita até o topo texturizado.",
            recommendedBeard = "Barba Lenhador com linhas bem desenhadas na bochecha.",
            maintenance = "Manutenção a cada 10 a 15 dias.",
            tags = listOf("Tendência", "Zero na pele", "Urbano")
        ),
        LookbookStyle(
            id = "pompadour_classico",
            title = "Pompadour Clássico Rockabilly",
            category = "Estilo Retrô",
            description = "Topete alto e volumoso penteado para trás com brilho ou acabamento semi-brilho, laterais alinhadas na tesoura.",
            recommendedBeard = "Barba tradicional rala ou cavanhaque vintage.",
            maintenance = "Manutenção a cada 20 dias com pomada fixadora.",
            tags = listOf("Vintage", "Elegante", "Old School")
        ),
        LookbookStyle(
            id = "taper_fade",
            title = "Taper Fade Executivo",
            category = "Clássico Moderno",
            description = "Degradê focado apenas nas costeletas e na nuca, mantendo as laterais sociais e o topo com caimento natural.",
            recommendedBeard = "Barba sombreada ou bem aparada no nível 2.",
            maintenance = "Ideal para ambiente corporativo. Manutenção a cada 15 dias.",
            tags = listOf("Executivo", "Discreto", "Versátil")
        ),
        LookbookStyle(
            id = "buzz_cut",
            title = "Buzz Cut Militar com Linhas",
            category = "Prático & Estiloso",
            description = "Corte uniforme bem rente à cabeça com acabamento no pezinho e detalhe sutil de risco na lateral.",
            recommendedBeard = "Barba cheia e bem hidratada para equilibrar o visual.",
            maintenance = "Manutenção rápida a cada 7 a 10 dias.",
            tags = listOf("Ultra Prático", "Marcante", "Verão")
        )
    )

    // Subscription Plans
    val subscriptionPlans = listOf(
        SubscriptionPlan(
            id = "plan_gentleman",
            name = "Clube Gentleman",
            priceMonthly = 89.90,
            badge = "Essencial",
            features = listOf(
                "2 Cortes de Cabelo por mês",
                "1 Barboterapia cortesia",
                "10% de desconto em toda a loja de produtos",
                "Prioridade na escolha de horários de pico",
                "Café gourmet e água mineral liberados"
            ),
            isPopular = false
        ),
        SubscriptionPlan(
            id = "plan_imperial",
            name = "Clube Imperial VIP",
            priceMonthly = 159.90,
            badge = "Mais Assinado",
            features = listOf(
                "CORTES ILIMITADOS no mês",
                "BARBA ILIMITADA no mês",
                "1 Cerveja Artesanal trincando a cada visita",
                "20% de desconto em pomadas e óleos",
                "Acesso a horários exclusivos e agendamento VIP",
                "Sobrancelha e pezinho sempre alinhados"
            ),
            isPopular = true
        )
    )

    // SaaS Metrics
    val saasMetrics: StateFlow<SaaSOverviewMetrics> = combine(appointments, orders) { appts, ords ->
        val todayStr = getTodayFormattedDate()
        val todayAppts = appts.filter { it.appointmentDate == todayStr }
        val completedToday = todayAppts.filter { it.status == "CONCLUIDO" }
        val pendingToday = todayAppts.filter { it.status == "CONFIRMADO" || it.status == "EM_ATENDIMENTO" }

        val todayRevenueFromAppts = completedToday.sumOf { it.totalPrice }
        val todayRevenueFromOrders = ords.sumOf { it.totalAmount }
        val totalDailyRevenue = todayRevenueFromAppts + todayRevenueFromOrders

        val monthlyApptsRevenue = appts.filter { it.status == "CONCLUIDO" }.sumOf { it.totalPrice }
        val totalMonthlyRevenue = monthlyApptsRevenue + ords.sumOf { it.totalAmount } + 2850.0

        val avgTicket = if (todayAppts.isNotEmpty()) {
            todayAppts.sumOf { it.totalPrice } / todayAppts.size
        } else {
            65.0
        }

        val occupancy = ((todayAppts.size.toDouble() / 24.0) * 100).toInt().coerceIn(10, 95)

        SaaSOverviewMetrics(
            dailyRevenue = totalDailyRevenue,
            monthlyRevenue = totalMonthlyRevenue,
            todayAppointmentsCount = todayAppts.size,
            averageTicket = avgTicket,
            occupancyRatePercentage = occupancy,
            pendingAppointmentsCount = pendingToday.size,
            completedAppointmentsCount = completedToday.size
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SaaSOverviewMetrics(385.0, 14200.0, 5, 77.0, 78, 3, 2)
    )

    // Barber Commission Summaries
    val barberCommissions: StateFlow<List<BarberCommissionSummary>> = combine(barbers, appointments) { barbs, appts ->
        barbs.map { barber ->
            val barberAppts = appts.filter { it.barberId == barber.id && it.status == "CONCLUIDO" }
            val billed = barberAppts.sumOf { it.totalPrice }
            val commission = billed * barber.commissionRate
            BarberCommissionSummary(
                barberId = barber.id,
                barberName = barber.name,
                role = barber.role,
                appointmentsCount = barberAppts.size,
                totalBilled = billed,
                commissionAmount = commission
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Master Global Platform Metrics
    val masterGlobalRevenue: StateFlow<Double> = allLicenses.combine(orders) { licenses, ords ->
        val licenseMRR = licenses.filter { it.status == "ATIVA" }.sumOf { it.monthlyFee }
        val platformGmv = ords.sumOf { it.totalAmount } * 0.05 // 5% SaaS take rate on products/services
        licenseMRR + platformGmv + 4800.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5580.0)

    // ------------------------------------------------------------------------
    // AUTHENTICATION LOGIC (Login, Register, Recover, Social, Switch Role)
    // ------------------------------------------------------------------------

    fun login(identifier: String, pass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            // Check direct Master credentials match requested in prompt: master / mater123
            if (identifier.trim().equals("master", ignoreCase = true) && pass.trim() == "mater123") {
                val master = repository.authenticate("master", "mater123") ?: UserEntity(
                    username = "master",
                    email = "master@barberpro.com",
                    password = "mater123",
                    name = "Administrador Master SaaS",
                    role = "MASTER",
                    businessName = "BarberPro Global",
                    licensePlan = "MASTER_UNLIMITED",
                    licenseStatus = "ATIVA"
                )
                _currentUser.value = master
                _currentScreen.value = AppScreen.MASTER_ADMIN
                onResult(true, "Bem-vindo, Administrador Master!")
                return@launch
            }

            val user = repository.authenticate(identifier, pass)
            if (user != null) {
                _currentUser.value = user
                when (user.role) {
                    "MASTER" -> _currentScreen.value = AppScreen.MASTER_ADMIN
                    "COMMERCIAL" -> _currentScreen.value = AppScreen.SAAS_DASHBOARD
                    else -> _currentScreen.value = AppScreen.HOME
                }
                onResult(true, "Login realizado com sucesso como ${user.name}!")
            } else {
                onResult(false, "Usuário ou senha inválidos. Tente novamente!")
            }
        }
    }

    fun register(
        username: String,
        email: String,
        pass: String,
        name: String,
        phone: String,
        role: String,
        businessName: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            if (email.isBlank() || pass.isBlank() || name.isBlank()) {
                onResult(false, "Preencha todos os campos obrigatórios.")
                return@launch
            }
            val newUser = repository.registerUser(
                username = username.ifBlank { email.substringBefore("@") },
                email = email,
                pass = pass,
                name = name,
                phone = phone,
                role = role,
                businessName = businessName
            )
            _currentUser.value = newUser
            when (newUser.role) {
                "MASTER" -> _currentScreen.value = AppScreen.MASTER_ADMIN
                "COMMERCIAL" -> _currentScreen.value = AppScreen.SAAS_DASHBOARD
                else -> _currentScreen.value = AppScreen.HOME
            }
            onResult(true, "Conta criada com sucesso!")
        }
    }

    fun socialConnect(provider: String, targetRole: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val email = when (provider) {
                "Google" -> "usuario.google@gmail.com"
                "Facebook" -> "usuario.fb@facebook.com"
                "Instagram" -> "barbeiro.insta@instagram.com"
                else -> "usuario.social@social.com"
            }
            val name = when (provider) {
                "Google" -> "Conectado via Google"
                "Facebook" -> "Conectado via Facebook"
                "Instagram" -> "Conectado via Instagram"
                else -> "Usuário Conectado"
            }

            val user = repository.socialLogin(provider, name, email, targetRole)
            _currentUser.value = user
            when (user.role) {
                "MASTER" -> _currentScreen.value = AppScreen.MASTER_ADMIN
                "COMMERCIAL" -> _currentScreen.value = AppScreen.SAAS_DASHBOARD
                else -> _currentScreen.value = AppScreen.HOME
            }
            onResult(true, "Autenticado com sucesso via $provider!")
        }
    }

    fun recoverPassword(email: String, newPass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val success = repository.resetPassword(email, newPass)
            if (success) {
                onResult(true, "Senha alterada com sucesso! Você já pode fazer login.")
            } else {
                onResult(false, "E-mail não encontrado no sistema.")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = AppScreen.LOGIN
    }

    // Easy Switcher to quickly demonstrate and test the 3 user personas
    fun switchUserRole(role: UserRole) {
        viewModelScope.launch {
            when (role) {
                UserRole.MASTER -> {
                    val master = repository.authenticate("master", "mater123") ?: UserEntity(
                        id = 1,
                        username = "master",
                        email = "master@barberpro.com",
                        password = "mater123",
                        name = "Administrador Master SaaS",
                        phone = "(11) 99999-8888",
                        role = "MASTER",
                        businessName = "BarberPro Global Platform",
                        licensePlan = "MASTER_UNLIMITED",
                        licenseStatus = "ATIVA"
                    )
                    _currentUser.value = master
                    _currentScreen.value = AppScreen.MASTER_ADMIN
                }
                UserRole.COMMERCIAL -> {
                    val commercial = repository.authenticate("comercial", "barber123") ?: UserEntity(
                        id = 2,
                        username = "comercial",
                        email = "comercial@domnavalha.com",
                        password = "barber123",
                        name = "Carlos 'Navalha' Silva",
                        phone = "(11) 98765-4321",
                        role = "COMMERCIAL",
                        businessName = "Barbearia Dom Navalha & Co.",
                        licensePlan = "COMMERCIAL_PRO",
                        licenseStatus = "ATIVA"
                    )
                    _currentUser.value = commercial
                    _currentScreen.value = AppScreen.SAAS_DASHBOARD
                }
                UserRole.CLIENT -> {
                    val client = repository.authenticate("cliente", "cliente123") ?: UserEntity(
                        id = 3,
                        username = "cliente",
                        email = "gabriel.cliente@gmail.com",
                        password = "cliente123",
                        name = "Gabriel Albuquerque",
                        phone = "(11) 98765-4321",
                        role = "CLIENT",
                        businessName = "",
                        licensePlan = "CLIENT_VIP",
                        licenseStatus = "ATIVA"
                    )
                    _currentUser.value = client
                    _currentScreen.value = AppScreen.HOME
                }
            }
        }
    }

    fun updateCommercialLicenseStatus(licenseId: Int, status: String) {
        viewModelScope.launch {
            repository.updateLicenseStatus(licenseId, status)
        }
    }

    // Navigation
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Filter mutations
    fun setServiceCategory(category: String) {
        _selectedServiceCategory.value = category
    }

    fun setProductCategory(category: String) {
        _selectedProductCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Booking actions
    fun toggleServiceInBooking(service: BarbershopServiceEntity) {
        val current = _bookingSelectedServices.value.toMutableSet()
        if (current.contains(service)) {
            current.remove(service)
        } else {
            current.add(service)
        }
        _bookingSelectedServices.value = current
    }

    fun selectBarber(barber: BarberEntity?) {
        _bookingSelectedBarber.value = barber
    }

    fun selectDate(date: String) {
        _bookingSelectedDate.value = date
    }

    fun selectTime(time: String) {
        _bookingSelectedTime.value = time
    }

    fun updateClientName(name: String) {
        _bookingClientName.value = name
    }

    fun updateClientPhone(phone: String) {
        _bookingClientPhone.value = phone
    }

    fun updateBookingNotes(notes: String) {
        _bookingNotes.value = notes
    }

    fun startBookingWithService(service: BarbershopServiceEntity) {
        _bookingSelectedServices.value = setOf(service)
        _currentScreen.value = AppScreen.BOOKING
    }

    fun confirmBooking() {
        val selected = _bookingSelectedServices.value
        if (selected.isEmpty()) return

        val barber = _bookingSelectedBarber.value
        val barberId = barber?.id ?: 1
        val barberName = barber?.nickname ?: "Carlos Navalha"
        val serviceNames = selected.joinToString(" + ") { it.name }
        val totalPrice = selected.sumOf { it.price }
        val totalDuration = selected.sumOf { it.durationMinutes }

        viewModelScope.launch {
            val id = repository.createAppointment(
                clientName = _bookingClientName.value.ifBlank { _currentUser.value?.name ?: "Cliente VIP" },
                clientPhone = _bookingClientPhone.value.ifBlank { _currentUser.value?.phone ?: "(11) 99999-0000" },
                barberId = barberId,
                barberName = barberName,
                serviceNames = serviceNames,
                totalPrice = totalPrice,
                totalDurationMinutes = totalDuration,
                appointmentDate = _bookingSelectedDate.value,
                appointmentTime = _bookingSelectedTime.value,
                notes = _bookingNotes.value
            )

            val created = AppointmentEntity(
                id = id.toInt(),
                clientName = _bookingClientName.value,
                clientPhone = _bookingClientPhone.value,
                barberId = barberId,
                barberName = barberName,
                serviceNames = serviceNames,
                totalPrice = totalPrice,
                totalDurationMinutes = totalDuration,
                appointmentDate = _bookingSelectedDate.value,
                appointmentTime = _bookingSelectedTime.value,
                status = "CONFIRMADO",
                notes = _bookingNotes.value
            )
            _lastBookedAppointment.value = created
            _loyaltyStamps.value = (_loyaltyStamps.value + 1).coerceAtMost(10)
        }
    }

    fun dismissBookingConfirmation() {
        _lastBookedAppointment.value = null
        _bookingSelectedServices.value = emptySet()
        _currentScreen.value = AppScreen.MY_APPOINTMENTS
    }

    // Appointment Status management (SaaS & Client)
    fun updateAppointmentStatus(appointmentId: Int, newStatus: String) {
        viewModelScope.launch {
            repository.updateAppointmentStatus(appointmentId, newStatus)
        }
    }

    fun cancelAppointment(appointmentId: Int) {
        viewModelScope.launch {
            repository.cancelAppointment(appointmentId)
        }
    }

    // Cart and E-Commerce actions
    fun addToCart(product: ProductEntity) {
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }

    fun incrementCartItem(item: CartItemEntity) {
        viewModelScope.launch {
            repository.incrementCartItem(item)
        }
    }

    fun decrementCartItem(item: CartItemEntity) {
        viewModelScope.launch {
            repository.decrementCartItem(item)
        }
    }

    fun removeCartItem(item: CartItemEntity) {
        viewModelScope.launch {
            repository.removeCartItem(item)
        }
    }

    fun openCart() {
        _isCartSheetOpen.value = true
    }

    fun closeCart() {
        _isCartSheetOpen.value = false
    }

    fun checkout(paymentMethod: String) {
        val items = cartItems.value
        if (items.isEmpty()) return

        viewModelScope.launch {
            val order = repository.checkout(items, paymentMethod)
            _lastCompletedOrder.value = order
            _isCartSheetOpen.value = false
        }
    }

    fun dismissOrderDialog() {
        _lastCompletedOrder.value = null
    }

    // SaaS Catalog actions
    fun toggleServiceStatus(serviceId: Int, isActive: Boolean) {
        viewModelScope.launch {
            repository.toggleServiceActive(serviceId, isActive)
        }
    }

    fun toggleBarberStatus(barberId: Int, isAvailable: Boolean) {
        viewModelScope.launch {
            repository.toggleBarberAvailability(barberId, isAvailable)
        }
    }

    fun addNewService(name: String, category: String, price: Double, duration: Int, description: String) {
        viewModelScope.launch {
            repository.addNewService(
                BarbershopServiceEntity(
                    name = name,
                    category = category,
                    description = description,
                    durationMinutes = duration,
                    price = price,
                    isPopular = false,
                    isActive = true
                )
            )
        }
    }

    companion object {
        fun getTodayFormattedDate(): String {
            return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        }

        fun getNextDays(daysAhead: Int = 10): List<Pair<String, String>> {
            val list = mutableListOf<Pair<String, String>>()
            val cal = Calendar.getInstance()
            val dayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val labelFormat = SimpleDateFormat("EEE, dd MMM", Locale("pt", "BR"))

            for (i in 0 until daysAhead) {
                val fullDate = dayFormat.format(cal.time)
                val label = if (i == 0) "Hoje" else if (i == 1) "Amanhã" else labelFormat.format(cal.time).replaceFirstChar { it.uppercase() }
                list.add(Pair(fullDate, label))
                cal.add(Calendar.DAY_OF_YEAR, 1)
            }
            return list
        }
    }
}
