package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.BarberAppViewModel
import com.example.ui.components.BarberBottomNavigation
import com.example.ui.components.BarberTopBar
import com.example.ui.components.BookingSuccessDialog
import com.example.ui.components.CartBottomSheet
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.screens.BookingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MasterAdminScreen
import com.example.ui.screens.MyAppointmentsScreen
import com.example.ui.screens.SaaSDashboardScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.theme.BarberProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BarberProTheme {
                BarberApp()
            }
        }
    }
}

@Composable
fun BarberApp(viewModel: BarberAppViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val isCartOpen by viewModel.isCartSheetOpen.collectAsStateWithLifecycle()
    val lastBookedAppointment by viewModel.lastBookedAppointment.collectAsStateWithLifecycle()
    val lastCompletedOrder by viewModel.lastCompletedOrder.collectAsStateWithLifecycle()

    val cartTotalCount = cartItems.sumOf { it.quantity }

    // Android Hardware Back button handling
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        if (currentScreen == AppScreen.LOGIN && currentUser != null) {
            viewModel.navigateTo(AppScreen.HOME)
        } else {
            viewModel.navigateTo(AppScreen.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != AppScreen.LOGIN) {
                BarberTopBar(
                    currentUser = currentUser,
                    onOpenLogin = { viewModel.navigateTo(AppScreen.LOGIN) },
                    onSwitchRole = { role -> viewModel.switchUserRole(role) },
                    onLogout = { viewModel.logout() },
                    cartItemsCount = cartTotalCount,
                    onOpenCart = { viewModel.openCart() },
                    currentScreen = currentScreen,
                    onBackToHome = { viewModel.navigateTo(AppScreen.HOME) },
                    onNavigateTo = { viewModel.navigateTo(it) }
                )
            }
        },
        bottomBar = {
            if (currentScreen != AppScreen.LOGIN) {
                BarberBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) },
                    currentUser = currentUser
                )
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            AppScreen.LOGIN -> {
                LoginScreen(
                    viewModel = viewModel,
                    modifier = screenModifier
                )
            }
            AppScreen.MASTER_ADMIN -> {
                MasterAdminScreen(
                    viewModel = viewModel,
                    modifier = screenModifier
                )
            }
            AppScreen.SAAS_DASHBOARD -> {
                SaaSDashboardScreen(
                    viewModel = viewModel,
                    modifier = screenModifier
                )
            }
            AppScreen.HOME -> {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = screenModifier
                )
            }
            AppScreen.BOOKING -> {
                BookingScreen(
                    viewModel = viewModel,
                    modifier = screenModifier
                )
            }
            AppScreen.SERVICES -> {
                ServicesScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = screenModifier
                )
            }
            AppScreen.SHOP -> {
                ShopScreen(
                    viewModel = viewModel,
                    modifier = screenModifier
                )
            }
            AppScreen.MY_APPOINTMENTS -> {
                MyAppointmentsScreen(
                    viewModel = viewModel,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = screenModifier
                )
            }
        }

        // Cart Bottom Sheet
        if (isCartOpen) {
            CartBottomSheet(
                cartItems = cartItems,
                onIncrement = { viewModel.incrementCartItem(it) },
                onDecrement = { viewModel.decrementCartItem(it) },
                onRemove = { viewModel.removeCartItem(it) },
                onCheckout = { paymentMethod -> viewModel.checkout(paymentMethod) },
                onDismiss = { viewModel.closeCart() }
            )
        }

        // Booking Confirmation Dialog
        lastBookedAppointment?.let { appointment ->
            BookingSuccessDialog(
                appointment = appointment,
                onDismiss = { viewModel.dismissBookingConfirmation() }
            )
        }

        // Order Confirmation Dialog (e-commerce shop with PIX)
        lastCompletedOrder?.let { order ->
            OrderSuccessDialog(
                order = order,
                onDismiss = { viewModel.dismissOrderDialog() }
            )
        }
    }
}
