package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AppointmentEntity
import com.example.data.model.BarberEntity
import com.example.data.model.BarbershopServiceEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.CommercialLicenseEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        UserEntity::class,
        CommercialLicenseEntity::class,
        BarbershopServiceEntity::class,
        BarberEntity::class,
        AppointmentEntity::class,
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class BarberDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun commercialLicenseDao(): CommercialLicenseDao
    abstract fun serviceDao(): ServiceDao
    abstract fun barberDao(): BarberDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: BarberDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BarberDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarberDatabase::class.java,
                    "barber_pro_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: BarberDatabase) {
            // Initial Users: Master, Commercial & Client
            val initialUsers = listOf(
                UserEntity(
                    username = "master",
                    email = "master@barberpro.com",
                    password = "mater123", // Specified in the user brief: master / mater123
                    name = "Administrador Master SaaS",
                    phone = "(11) 99999-8888",
                    role = "MASTER",
                    businessName = "BarberPro Global Platform",
                    licensePlan = "MASTER_UNLIMITED",
                    licenseStatus = "ATIVA",
                    licenseExpiration = "VITALÍCIA",
                    authProvider = "EMAIL"
                ),
                UserEntity(
                    username = "comercial",
                    email = "comercial@domnavalha.com",
                    password = "barber123",
                    name = "Carlos 'Navalha' Silva",
                    phone = "(11) 98765-4321",
                    role = "COMMERCIAL",
                    businessName = "Barbearia Dom Navalha & Co.",
                    licensePlan = "COMMERCIAL_PRO",
                    licenseStatus = "ATIVA",
                    licenseExpiration = "31/12/2026",
                    authProvider = "EMAIL"
                ),
                UserEntity(
                    username = "cliente",
                    email = "gabriel.cliente@gmail.com",
                    password = "cliente123",
                    name = "Gabriel Albuquerque",
                    phone = "(11) 98765-4321",
                    role = "CLIENT",
                    businessName = "",
                    licensePlan = "CLIENT_VIP",
                    licenseStatus = "ATIVA",
                    licenseExpiration = "VITALÍCIA",
                    authProvider = "EMAIL"
                )
            )
            db.userDao().insertAll(initialUsers)

            // Initial Commercial Licenses for SaaS Master Management
            val initialLicenses = listOf(
                CommercialLicenseEntity(
                    businessName = "Barbearia Dom Navalha & Co.",
                    cnpjOrCpf = "34.567.890/0001-12",
                    ownerName = "Carlos 'Navalha' Silva",
                    ownerEmail = "comercial@domnavalha.com",
                    planName = "BarberPro Enterprise SaaS",
                    monthlyFee = 249.90,
                    status = "ATIVA",
                    maxBarbers = 6,
                    currentBarbersCount = 4,
                    validUntil = "31/12/2026",
                    financialStatus = "EM DIA"
                ),
                CommercialLicenseEntity(
                    businessName = "Navalha de Ouro Barber Club",
                    cnpjOrCpf = "45.123.987/0001-44",
                    ownerName = "Renato Vasconcelos",
                    ownerEmail = "contato@navalhaouro.com.br",
                    planName = "BarberPro Pro SaaS",
                    monthlyFee = 179.90,
                    status = "ATIVA",
                    maxBarbers = 4,
                    currentBarbersCount = 3,
                    validUntil = "15/11/2026",
                    financialStatus = "EM DIA"
                ),
                CommercialLicenseEntity(
                    businessName = "Viking Barbearia Tradicional",
                    cnpjOrCpf = "12.987.654/0001-88",
                    ownerName = "Igor 'Ragnar' Santos",
                    ownerEmail = "igor@vikingbarber.com",
                    planName = "BarberPro Start SaaS",
                    monthlyFee = 119.90,
                    status = "PENDENTE",
                    maxBarbers = 2,
                    currentBarbersCount = 2,
                    validUntil = "05/11/2026",
                    financialStatus = "AGUARDANDO PIX"
                ),
                CommercialLicenseEntity(
                    businessName = "Old School Barber Pub",
                    cnpjOrCpf = "23.456.789/0001-09",
                    ownerName = "Fernando Nogueira",
                    ownerEmail = "pub@oldschoolbarber.com",
                    planName = "BarberPro Enterprise SaaS",
                    monthlyFee = 249.90,
                    status = "ATIVA",
                    maxBarbers = 8,
                    currentBarbersCount = 6,
                    validUntil = "28/12/2026",
                    financialStatus = "EM DIA"
                )
            )
            db.commercialLicenseDao().insertAll(initialLicenses)

            // Initial Services
            val services = listOf(
                BarbershopServiceEntity(
                    name = "Combo Master: Cabelo + Barba + Cerveja",
                    category = "Combos",
                    description = "Corte personalizado completo com degradê ou tesoura + Barboterapia completa com toalha quente + 1 Cerveja artesanal da casa.",
                    durationMinutes = 60,
                    price = 85.0,
                    isPopular = true,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Degradê Navalhado (Fade)",
                    category = "Cabelo",
                    description = "Acabamento militar/fade preciso na navalha com shaver e lavagem com massagem capilar revigorante.",
                    durationMinutes = 45,
                    price = 55.0,
                    isPopular = true,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Corte Tradicional / Tesoura",
                    category = "Cabelo",
                    description = "Corte social ou clássico alinhado exclusivamente na tesoura e máquina com penteado finalizador.",
                    durationMinutes = 35,
                    price = 45.0,
                    isPopular = false,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Barboterapia com Toalha Quente",
                    category = "Barba",
                    description = "Modelagem completa da barba, óleo essencial, toalha vaporizada para abrir os poros, lâmina e loção pós-barba gelada.",
                    durationMinutes = 30,
                    price = 45.0,
                    isPopular = true,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Pigmentação & Camuflagem",
                    category = "Barba",
                    description = "Preenchimento de falhas na barba ou cabelo com tinta temporária de alta fixação que dura até 5 dias.",
                    durationMinutes = 25,
                    price = 35.0,
                    isPopular = false,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Selagem Térmica Orgânica",
                    category = "Tratamentos",
                    description = "Alinhamento e redução de volume sem formol, hidratação profunda e brilho natural.",
                    durationMinutes = 55,
                    price = 75.0,
                    isPopular = false,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Limpeza de Pele & Esfoliação Black",
                    category = "Tratamentos",
                    description = "Máscara removedora de cravos, esfoliação facial refrescante e hidratação com ácido hialurônico masculino.",
                    durationMinutes = 30,
                    price = 40.0,
                    isPopular = false,
                    isActive = true
                ),
                BarbershopServiceEntity(
                    name = "Pezinho & Sobrancelha na Navalha",
                    category = "Cabelo",
                    description = "Alinhamento das costeletas, nuca e desenho suave da sobrancelha na navalha esterilizada.",
                    durationMinutes = 20,
                    price = 25.0,
                    isPopular = false,
                    isActive = true
                )
            )
            db.serviceDao().insertAll(services)

            // Initial Barbers
            val barbers = listOf(
                BarberEntity(
                    name = "Carlos 'Navalha' Silva",
                    nickname = "Carlos Navalha",
                    role = "Master Barber & Fundador",
                    rating = 4.9,
                    reviewCount = 384,
                    specialties = "Fade Americano, Visagismo Masculino, Barboterapia",
                    bio = "Mais de 12 anos de estrada, formado nas melhores academias internacionais. Apaixonado pela precisão dos detalhes.",
                    commissionRate = 0.50,
                    isAvailable = true
                ),
                BarberEntity(
                    name = "Lucas Mendes",
                    nickname = "Lucas Fade",
                    role = "Especialista em Degradê",
                    rating = 4.8,
                    reviewCount = 240,
                    specialties = "Taper Fade, Freestyle, Riscos e Platinados",
                    bio = "Referência em cortes urbanos e degradês perfeitos. Sintonizado nas maiores tendências de estilo e moda.",
                    commissionRate = 0.45,
                    isAvailable = true
                ),
                BarberEntity(
                    name = "Matheus 'Old School' Rocha",
                    nickname = "Matheus Rocha",
                    role = "Mestre em Barba Clássica",
                    rating = 4.9,
                    reviewCount = 195,
                    specialties = "Barba Lenhador, Navalhete Tradicional, Pompadour",
                    bio = "Especialista no ritual clássico de barbearia com toalha quente e massagem relaxante.",
                    commissionRate = 0.45,
                    isAvailable = true
                ),
                BarberEntity(
                    name = "Gabriel Santos",
                    nickname = "Biel Barber",
                    role = "Visagista & Estética",
                    rating = 4.7,
                    reviewCount = 112,
                    specialties = "Corte Tesoura, Selagem e Tratamentos Faciais",
                    bio = "Foco em saúde capilar masculina, visagismo para cada tipo de rosto e cortes clássicos.",
                    commissionRate = 0.40,
                    isAvailable = true
                )
            )
            db.barberDao().insertAll(barbers)

            // Initial Products
            val products = listOf(
                ProductEntity(
                    name = "Pomada Efeito Matte Seco Dom Navalha",
                    category = "Pomadas",
                    description = "Fixação extra forte com acabamento 100% fosco sem brilho. Ideal para topetes e texturizados.",
                    price = 48.0,
                    stockQuantity = 22,
                    rating = 4.9,
                    volumeOrSize = "120g",
                    isFeatured = true
                ),
                ProductEntity(
                    name = "Óleo Nobre para Barba Wood & Amber",
                    category = "Barba",
                    description = "Mistura de óleos de argan e jojoba com essência amadeirada nobre. Hidrata fios e acalma a pele.",
                    price = 39.9,
                    stockQuantity = 18,
                    rating = 4.8,
                    volumeOrSize = "30ml",
                    isFeatured = true
                ),
                ProductEntity(
                    name = "Balm Hidratante para Barba Refrescante",
                    category = "Barba",
                    description = "Elimina o frizz, amacia os fios e confere maciez instantânea com sensação refrescante de menta.",
                    price = 42.0,
                    stockQuantity = 15,
                    rating = 4.9,
                    volumeOrSize = "100g",
                    isFeatured = false
                ),
                ProductEntity(
                    name = "Shampoo Ice Menthol 2 em 1",
                    category = "Cabelo",
                    description = "Limpeza profunda do couro cabeludo e barba com sensação ultra gelada de frescor anticaspa.",
                    price = 36.0,
                    stockQuantity = 25,
                    rating = 4.7,
                    volumeOrSize = "250ml",
                    isFeatured = false
                ),
                ProductEntity(
                    name = "Pente de Madeira Anti-Estático Artesanal",
                    category = "Acessórios",
                    description = "Feito à mão com madeira nobre de lei, não gera eletricidade estática e desembaraça suavemente.",
                    price = 29.9,
                    stockQuantity = 14,
                    rating = 4.8,
                    volumeOrSize = "1 unidade",
                    isFeatured = false
                ),
                ProductEntity(
                    name = "Kit VIP Grooming Master",
                    category = "Acessórios",
                    description = "Contém 1 Pomada Matte, 1 Óleo 30ml, 1 Balm 100g e 1 Pente artesanal em caixa personalizada de madeira.",
                    price = 139.0,
                    stockQuantity = 8,
                    rating = 5.0,
                    volumeOrSize = "Kit 4 itens",
                    isFeatured = true
                )
            )
            db.productDao().insertAll(products)

            // Initial Sample Appointments
            val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val sampleAppointments = listOf(
                AppointmentEntity(
                    clientName = "Bruno Albuquerque",
                    clientPhone = "(11) 98765-4321",
                    barberId = 1,
                    barberName = "Carlos Navalha",
                    serviceNames = "Combo Master: Cabelo + Barba + Cerveja",
                    totalPrice = 85.0,
                    totalDurationMinutes = 60,
                    appointmentDate = todayStr,
                    appointmentTime = "09:30",
                    status = "CONCLUIDO",
                    notes = "Gosta de toalha bem quente e navalha afiada."
                ),
                AppointmentEntity(
                    clientName = "Rodrigo Ferreira",
                    clientPhone = "(11) 97654-3210",
                    barberId = 2,
                    barberName = "Lucas Fade",
                    serviceNames = "Degradê Navalhado (Fade)",
                    totalPrice = 55.0,
                    totalDurationMinutes = 45,
                    appointmentDate = todayStr,
                    appointmentTime = "11:00",
                    status = "CONCLUIDO",
                    notes = "Fade baixo bem marcado."
                ),
                AppointmentEntity(
                    clientName = "Gabriel Albuquerque",
                    clientPhone = "(11) 98765-4321",
                    barberId = 1,
                    barberName = "Carlos Navalha",
                    serviceNames = "Degradê Navalhado (Fade) + Barboterapia",
                    totalPrice = 100.0,
                    totalDurationMinutes = 75,
                    appointmentDate = todayStr,
                    appointmentTime = "14:00",
                    status = "EM_ATENDIMENTO",
                    notes = "Cliente VIP Clube Imperial."
                ),
                AppointmentEntity(
                    clientName = "Thiago Silveira",
                    clientPhone = "(11) 98112-3344",
                    barberId = 3,
                    barberName = "Matheus Rocha",
                    serviceNames = "Barboterapia com Toalha Quente",
                    totalPrice = 45.0,
                    totalDurationMinutes = 30,
                    appointmentDate = todayStr,
                    appointmentTime = "16:30",
                    status = "CONFIRMADO",
                    notes = "Primeira vez na barbearia."
                ),
                AppointmentEntity(
                    clientName = "Eduardo Camargo",
                    clientPhone = "(11) 97444-5566",
                    barberId = 2,
                    barberName = "Lucas Fade",
                    serviceNames = "Corte Tradicional / Tesoura",
                    totalPrice = 45.0,
                    totalDurationMinutes = 35,
                    appointmentDate = todayStr,
                    appointmentTime = "18:00",
                    status = "CONFIRMADO",
                    notes = "Prefere corte rápido."
                )
            )
            sampleAppointments.forEach { db.appointmentDao().insert(it) }
        }
    }
}
