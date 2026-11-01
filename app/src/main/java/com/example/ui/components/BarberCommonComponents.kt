package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppointmentEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.theme.BarberDarkBg
import com.example.ui.theme.BarberDarkSurface
import com.example.ui.theme.BarberDarkSurfaceCard
import com.example.ui.theme.BarberDarkSurfaceVariant
import com.example.ui.theme.BarberGold
import com.example.ui.theme.BarberGoldContainer
import com.example.ui.theme.BarberGreen
import com.example.ui.theme.BarberRed
import com.example.ui.theme.BarberTextMuted
import com.example.ui.theme.BarberTextPrimary
import com.example.ui.theme.BarberTextSecondary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarberTopBar(
    currentUser: UserEntity?,
    onOpenLogin: () -> Unit,
    onSwitchRole: (UserRole) -> Unit,
    onLogout: () -> Unit,
    cartItemsCount: Int,
    onOpenCart: () -> Unit,
    currentScreen: AppScreen,
    onBackToHome: () -> Unit,
    onNavigateTo: (AppScreen) -> Unit
) {
    var showUserMenu by remember { mutableStateOf(false) }
    var showRoleModal by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BarberDarkBg,
            titleContentColor = BarberTextPrimary
        ),
        navigationIcon = {
            if (currentScreen != AppScreen.HOME && currentScreen != AppScreen.SAAS_DASHBOARD && currentScreen != AppScreen.MASTER_ADMIN && currentScreen != AppScreen.LOGIN) {
                IconButton(
                    onClick = onBackToHome,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = BarberGold
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BarberGoldContainer)
                        .border(1.dp, BarberGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (currentUser?.role) {
                            "MASTER" -> Icons.Default.Security
                            "COMMERCIAL" -> Icons.Default.Store
                            else -> Icons.Default.ContentCut
                        },
                        contentDescription = "Logo",
                        tint = BarberGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        title = {
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (currentUser?.role) {
                            "MASTER" -> "BarberPro Master"
                            "COMMERCIAL" -> currentUser.businessName.ifBlank { "BarberPro SaaS" }
                            else -> "Dom Navalha & Co."
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = BarberTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    // Role Badge
                    val (badgeText, badgeBg, badgeFg) = when (currentUser?.role) {
                        "MASTER" -> Triple("MASTER", BarberGold, BarberDarkBg)
                        "COMMERCIAL" -> Triple("COMERCIAL", Color(0xFF1E3A5F), Color(0xFF64B5F6))
                        else -> Triple("CLIENTE", BarberDarkSurfaceCard, BarberGreen)
                    }

                    Surface(
                        color = badgeBg,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.clickable { showRoleModal = true }
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = badgeFg,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = currentUser?.name ?: "Visitante",
                    fontSize = 11.sp,
                    color = BarberTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        actions = {
            // Quick Role Switcher Button
            Surface(
                onClick = { showRoleModal = true },
                color = BarberDarkSurfaceCard,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold.copy(alpha = 0.5f)),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .minimumInteractiveComponentSize()
                    .testTag("switch_user_role_top_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Trocar Usuário",
                        tint = BarberGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Trocar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberGold
                    )
                }
            }

            // User Profile Menu Button
            Box {
                IconButton(onClick = { showUserMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        tint = BarberTextPrimary
                    )
                }

                DropdownMenu(
                    expanded = showUserMenu,
                    onDismissRequest = { showUserMenu = false },
                    modifier = Modifier.background(BarberDarkSurfaceCard)
                ) {
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = currentUser?.name ?: "Não conectado",
                                    fontWeight = FontWeight.Bold,
                                    color = BarberTextPrimary,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Perfil: ${currentUser?.role ?: "Visitante"}",
                                    color = BarberGold,
                                    fontSize = 11.sp
                                )
                            }
                        },
                        onClick = {}
                    )

                    HorizontalDivider(color = BarberDarkSurfaceVariant)

                    if (currentUser?.role == "MASTER") {
                        DropdownMenuItem(
                            text = { Text("Painel Master Global", color = BarberTextPrimary, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = BarberGold) },
                            onClick = {
                                showUserMenu = false
                                onNavigateTo(AppScreen.MASTER_ADMIN)
                            }
                        )
                    }

                    if (currentUser?.role == "COMMERCIAL" || currentUser?.role == "MASTER") {
                        DropdownMenuItem(
                            text = { Text("Gestão da Barbearia (SaaS)", color = BarberTextPrimary, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Store, contentDescription = null, tint = BarberGold) },
                            onClick = {
                                showUserMenu = false
                                onNavigateTo(AppScreen.SAAS_DASHBOARD)
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Visão do Cliente (Vitrine)", color = BarberTextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = BarberGold) },
                        onClick = {
                            showUserMenu = false
                            onNavigateTo(AppScreen.HOME)
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Trocar Tipo de Usuário", color = BarberGold, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = BarberGold) },
                        onClick = {
                            showUserMenu = false
                            showRoleModal = true
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Tela de Login / Cadastro", color = BarberTextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Login, contentDescription = null, tint = BarberTextSecondary) },
                        onClick = {
                            showUserMenu = false
                            onOpenLogin()
                        }
                    )

                    HorizontalDivider(color = BarberDarkSurfaceVariant)

                    DropdownMenuItem(
                        text = { Text("Sair da Conta", color = BarberRed, fontSize = 13.sp, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Logout, contentDescription = null, tint = BarberRed) },
                        onClick = {
                            showUserMenu = false
                            onLogout()
                        }
                    )
                }
            }

            // Cart icon
            if (currentUser?.role != "MASTER") {
                IconButton(
                    onClick = onOpenCart,
                    modifier = Modifier.testTag("open_cart_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartItemsCount > 0) {
                                Badge(
                                    containerColor = BarberGold,
                                    contentColor = BarberDarkBg
                                ) {
                                    Text(
                                        text = cartItemsCount.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Carrinho de compras",
                            tint = BarberTextPrimary
                        )
                    }
                }
            }
        }
    )

    // Modal to switch quickly between the 3 users specified by user
    if (showRoleModal) {
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { showRoleModal = false },
            containerColor = BarberDarkSurfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = BarberGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Alternar Tipo de Usuário",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Selecione um dos 3 tipos de usuários para experimentar todas as permissões do sistema SaaS:",
                        fontSize = 12.sp,
                        color = BarberTextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 1. MASTER
                    QuickAccountItem(
                        title = "1. Usuário Master (Acesso Total)",
                        credentials = "Login: master | Senha: mater123",
                        badgeText = "SUPER ADMIN",
                        badgeColor = BarberGold,
                        onClick = {
                            onSwitchRole(UserRole.MASTER)
                            Toast.makeText(context, "Conectado como USUÁRIO MASTER!", Toast.LENGTH_SHORT).show()
                            showRoleModal = false
                        }
                    )

                    // 2. COMMERCIAL
                    QuickAccountItem(
                        title = "2. Cliente Comercial (Licença SaaS)",
                        credentials = "Dono de Barbearia • Gestão, Agenda e Comissões",
                        badgeText = "COMERCIAL",
                        badgeColor = Color(0xFF42A5F5),
                        onClick = {
                            onSwitchRole(UserRole.COMMERCIAL)
                            Toast.makeText(context, "Conectado como CLIENTE COMERCIAL (Barbearia)!", Toast.LENGTH_SHORT).show()
                            showRoleModal = false
                        }
                    )

                    // 3. CLIENT
                    QuickAccountItem(
                        title = "3. Cliente Final (Consumidor)",
                        credentials = "Compra produtos e agenda serviços no comércio",
                        badgeText = "CLIENTE",
                        badgeColor = BarberGreen,
                        onClick = {
                            onSwitchRole(UserRole.CLIENT)
                            Toast.makeText(context, "Conectado como CLIENTE FINAL!", Toast.LENGTH_SHORT).show()
                            showRoleModal = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleModal = false }) {
                    Text("Fechar", color = BarberTextSecondary)
                }
            }
        )
    }
}

@Composable
fun BarberBottomNavigation(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    currentUser: UserEntity?
) {
    NavigationBar(
        containerColor = BarberDarkSurface,
        contentColor = BarberTextSecondary
    ) {
        when (currentUser?.role) {
            "MASTER" -> {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.MASTER_ADMIN,
                    onClick = { onNavigate(AppScreen.MASTER_ADMIN) },
                    icon = { Icon(Icons.Default.Security, contentDescription = "Master") },
                    label = { Text("Painel Master", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SAAS_DASHBOARD,
                    onClick = { onNavigate(AppScreen.SAAS_DASHBOARD) },
                    icon = { Icon(Icons.Default.Store, contentDescription = "Barbearia") },
                    label = { Text("Barbearias", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { onNavigate(AppScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Vitrine") },
                    label = { Text("Vitrine", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
            }
            "COMMERCIAL" -> {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SAAS_DASHBOARD,
                    onClick = { onNavigate(AppScreen.SAAS_DASHBOARD) },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Gestão Barbearia") },
                    label = { Text("Gestão SaaS", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SERVICES,
                    onClick = { onNavigate(AppScreen.SERVICES) },
                    icon = { Icon(Icons.Default.ContentCut, contentDescription = "Serviços") },
                    label = { Text("Serviços", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SHOP,
                    onClick = { onNavigate(AppScreen.SHOP) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Estoque") },
                    label = { Text("Estoque", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { onNavigate(AppScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Ver Loja") },
                    label = { Text("Ver Loja", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    )
                )
            }
            else -> {
                // CLIENT
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { onNavigate(AppScreen.HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                    label = { Text("Início", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_home_button")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.BOOKING,
                    onClick = { onNavigate(AppScreen.BOOKING) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Agendar") },
                    label = { Text("Agendar", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_booking_button")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SERVICES,
                    onClick = { onNavigate(AppScreen.SERVICES) },
                    icon = { Icon(Icons.Default.ContentCut, contentDescription = "Serviços") },
                    label = { Text("Serviços", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_services_button")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.SHOP,
                    onClick = { onNavigate(AppScreen.SHOP) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Produtos") },
                    label = { Text("Produtos", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_shop_button")
                )
                NavigationBarItem(
                    selected = currentScreen == AppScreen.MY_APPOINTMENTS,
                    onClick = { onNavigate(AppScreen.MY_APPOINTMENTS) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Meus Horários") },
                    label = { Text("Agenda", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BarberDarkBg,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = BarberTextSecondary,
                        unselectedTextColor = BarberTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_my_appointments_button")
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor, label) = when (status) {
        "CONFIRMADO" -> Triple(BarberGoldContainer, BarberGold, "Confirmado")
        "EM_ATENDIMENTO" -> Triple(Color(0xFF1E3A5F), Color(0xFF64B5F6), "Em Atendimento")
        "CONCLUIDO" -> Triple(Color(0xFF1B3B2B), BarberGreen, "Concluído")
        "CANCELADO" -> Triple(Color(0xFF3B1F20), BarberRed, "Cancelado")
        else -> Triple(BarberDarkSurfaceCard, BarberTextSecondary, status)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.4f))
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun RatingStarsRow(rating: Double, reviewCount: Int = 0) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Avaliação",
            tint = BarberGold,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = String.format(Locale.getDefault(), "%.1f", rating),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = BarberTextPrimary
        )
        if (reviewCount > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "($reviewCount)",
                fontSize = 11.sp,
                color = BarberTextMuted
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    cartItems: List<CartItemEntity>,
    onIncrement: (CartItemEntity) -> Unit,
    onDecrement: (CartItemEntity) -> Unit,
    onRemove: (CartItemEntity) -> Unit,
    onCheckout: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("PIX") }
    val subtotal = cartItems.sumOf { it.price * it.quantity }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BarberDarkSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Carrinho de Produtos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = BarberTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = BarberTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Seu carrinho está vazio",
                            color = BarberTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Adicione pomadas, óleos ou itens da barbearia",
                            color = BarberTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                ) {
                    items(cartItems, key = { it.id }) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.productName,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = BarberTextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "R$ ${String.format(Locale.getDefault(), "%.2f", item.price)} cada",
                                        fontSize = 12.sp,
                                        color = BarberGold
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        onClick = { onDecrement(item) },
                                        color = BarberDarkBg,
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                                                contentDescription = "Diminuir",
                                                tint = BarberTextSecondary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = item.quantity.toString(),
                                        fontWeight = FontWeight.Bold,
                                        color = BarberTextPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp)
                                    )
                                    Surface(
                                        onClick = { onIncrement(item) },
                                        color = BarberDarkBg,
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Aumentar",
                                                tint = BarberGold,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(
                    color = BarberDarkSurfaceCard,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Payment Method Selector
                Text(
                    text = "Forma de Pagamento:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BarberTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        onClick = { selectedPaymentMethod = "PIX" },
                        color = if (selectedPaymentMethod == "PIX") BarberGoldContainer else BarberDarkSurfaceCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedPaymentMethod == "PIX") BarberGold else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("payment_pix_option")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                tint = if (selectedPaymentMethod == "PIX") BarberGold else BarberTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PIX (Imediato)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedPaymentMethod == "PIX") BarberGold else BarberTextSecondary
                            )
                        }
                    }

                    Surface(
                        onClick = { selectedPaymentMethod = "CARTAO" },
                        color = if (selectedPaymentMethod == "CARTAO") BarberGoldContainer else BarberDarkSurfaceCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedPaymentMethod == "CARTAO") BarberGold else Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("payment_card_option")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Cartão / Balcão",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedPaymentMethod == "CARTAO") BarberGold else BarberTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total a Pagar",
                            fontSize = 12.sp,
                            color = BarberTextSecondary
                        )
                        Text(
                            text = "R$ ${String.format(Locale.getDefault(), "%.2f", subtotal)}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = BarberGold
                        )
                    }

                    Button(
                        onClick = { onCheckout(selectedPaymentMethod) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BarberGold,
                            contentColor = BarberDarkBg
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("cart_confirm_checkout_button")
                    ) {
                        Text(
                            text = if (selectedPaymentMethod == "PIX") "Pagar com PIX" else "Finalizar Pedido",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun OrderSuccessDialog(
    order: OrderEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BarberDarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BarberGreen.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = BarberGreen
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pedido Confirmado!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Número do pedido: #${order.orderNumber}",
                    fontWeight = FontWeight.Bold,
                    color = BarberGold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Itens: ${order.itemsSummary}",
                    color = BarberTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Total: R$ ${String.format(Locale.getDefault(), "%.2f", order.totalAmount)}",
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary,
                    fontSize = 15.sp
                )

                if (order.paymentMethod == "PIX" && order.pixCode.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Código PIX Copia e Cola:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BarberGold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = order.pixCode.take(45) + "...",
                                fontSize = 10.sp,
                                color = BarberTextMuted,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Código PIX", order.pixCode)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Chave PIX copiada para a área de transferência!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("copy_pix_code_button"),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    tint = BarberGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copiar Código PIX", fontSize = 12.sp, color = BarberGold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Seus produtos estão separados para retirada no balcão da barbearia!",
                    fontSize = 12.sp,
                    color = BarberTextMuted
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BarberGold, contentColor = BarberDarkBg),
                modifier = Modifier.testTag("order_success_ok_button")
            ) {
                Text("Entendido", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun BookingSuccessDialog(
    appointment: AppointmentEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BarberDarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BarberGoldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = BarberGold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Agendamento Realizado!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Horário confirmado com sucesso no BarberPro!",
                    color = BarberTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${appointment.appointmentDate} às ${appointment.appointmentTime}",
                                fontWeight = FontWeight.Bold,
                                color = BarberGold,
                                fontSize = 15.sp
                            )
                            StatusBadge(status = appointment.status)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Profissional: ${appointment.barberName}",
                            fontWeight = FontWeight.Medium,
                            color = BarberTextPrimary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Serviços: ${appointment.serviceNames}",
                            color = BarberTextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Duração: ${appointment.totalDurationMinutes} min • R$ ${String.format(Locale.getDefault(), "%.2f", appointment.totalPrice)}",
                            color = BarberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "+1 Selo adicionado ao seu Cartão Fidelidade VIP!",
                    color = BarberGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BarberGold, contentColor = BarberDarkBg),
                modifier = Modifier.testTag("booking_success_ok_button")
            ) {
                Text("Ver Meus Horários", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun QuickAccountItem(
    title: String,
    credentials: String,
    badgeText: String,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = BarberDarkSurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BarberDarkSurfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Text(
                    text = credentials,
                    fontSize = 11.sp,
                    color = BarberTextSecondary
                )
            }
            Surface(
                color = badgeColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeColor,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

