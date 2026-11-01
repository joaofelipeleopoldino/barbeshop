package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BarberEntity
import com.example.data.model.BarbershopServiceEntity
import com.example.ui.AppScreen
import com.example.ui.BarberAppViewModel
import com.example.ui.components.RatingStarsRow
import com.example.ui.theme.BarberDarkBg
import com.example.ui.theme.BarberDarkSurface
import com.example.ui.theme.BarberDarkSurfaceCard
import com.example.ui.theme.BarberDarkSurfaceVariant
import com.example.ui.theme.BarberGold
import com.example.ui.theme.BarberGoldContainer
import com.example.ui.theme.BarberGoldLight
import com.example.ui.theme.BarberGreen
import com.example.ui.theme.BarberTextMuted
import com.example.ui.theme.BarberTextPrimary
import com.example.ui.theme.BarberTextSecondary
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: BarberAppViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val barbers by viewModel.barbers.collectAsStateWithLifecycle()
    val services by viewModel.services.collectAsStateWithLifecycle()
    val loyaltyStamps by viewModel.loyaltyStamps.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Showcase Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2C241B),
                                Color(0xFF1E1A16),
                                BarberDarkBg
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = BarberGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BarberGreen.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(BarberGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Aberto Agora • Até 20h",
                                    color = BarberGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        RatingStarsRow(rating = 4.9, reviewCount = 820)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Barbearia Dom Navalha",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BarberTextPrimary,
                        lineHeight = 34.sp
                    )

                    Text(
                        text = "Cortes modernos, barboterapia tradicional com toalha quente e ritual de cavalheiros em ambiente premium.",
                        fontSize = 14.sp,
                        color = BarberTextSecondary,
                        modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
                    )

                    // Primary CTA Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigate(AppScreen.BOOKING) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BarberGold,
                                contentColor = BarberDarkBg
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("home_hero_book_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Agendar Horário",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { onNavigate(AppScreen.SERVICES) },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BarberGold),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("home_hero_services_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Serviços", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amenities Badges Flow
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AmenityChip(icon = Icons.Default.Coffee, text = "Café Gourmet")
                        AmenityChip(icon = Icons.Default.SportsBar, text = "Chopp Artesanal")
                        AmenityChip(icon = Icons.Default.SportsEsports, text = "Sinuca & Games")
                        AmenityChip(icon = Icons.Default.Wifi, text = "Wi-Fi 5G")
                        AmenityChip(icon = Icons.Default.AcUnit, text = "Ar Climatizado")
                    }
                }
            }
        }

        // Quick Navigation Cards
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                Text(
                    text = "Acesso Rápido",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Agendamento",
                        subtitle = "Escolha barbeiro e horário",
                        icon = Icons.Default.CalendarMonth,
                        accentColor = BarberGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.BOOKING) }
                    )
                    QuickActionCard(
                        title = "Loja Grooming",
                        subtitle = "Pomadas e balms",
                        icon = Icons.Default.Storefront,
                        accentColor = BarberGoldLight,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(AppScreen.SHOP) }
                    )
                }
            }
        }

        // Digital Loyalty Card (Cartão Fidelidade)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Cartão Fidelidade VIP",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = BarberGold
                            )
                            Text(
                                text = "Complete 10 cortes e ganhe 1 corte grátis!",
                                fontSize = 12.sp,
                                color = BarberTextSecondary
                            )
                        }
                        Surface(
                            color = BarberGoldContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "$loyaltyStamps / 10",
                                color = BarberGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stamps Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (i in 1..10) {
                            val isStamped = i <= loyaltyStamps
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isStamped) BarberGold else BarberDarkBg)
                                    .border(
                                        1.dp,
                                        if (isStamped) BarberGoldLight else BarberTextMuted,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isStamped) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = BarberDarkBg,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = i.toString(),
                                        fontSize = 11.sp,
                                        color = BarberTextMuted,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Barbers Carousel Section
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nossos Mestres Barbeiros",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Text(
                        text = "${barbers.size} profissionais",
                        fontSize = 12.sp,
                        color = BarberGold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(barbers, key = { it.id }) { barber ->
                        BarberCard(
                            barber = barber,
                            onBookWithBarber = {
                                viewModel.selectBarber(barber)
                                onNavigate(AppScreen.BOOKING)
                            }
                        )
                    }
                }
            }
        }

        // Popular Services Spotlight
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Serviços Mais Pedidos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Text(
                        text = "Ver todos",
                        fontSize = 13.sp,
                        color = BarberGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigate(AppScreen.SERVICES) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                val popularList = services.filter { it.isPopular }.take(3)
                popularList.forEach { service ->
                    ServiceHighlightCard(
                        service = service,
                        onBookService = {
                            viewModel.startBookingWithService(service)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Lookbook & Haircut Trends
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = "Lookbook & Estilos em Alta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Text(
                    text = "Inspire-se para o seu próximo visual e mostre ao seu barbeiro",
                    fontSize = 12.sp,
                    color = BarberTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                viewModel.lookbookStyles.forEach { style ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = style.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BarberGold
                                )
                                Surface(
                                    color = BarberDarkBg,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = style.category,
                                        fontSize = 11.sp,
                                        color = BarberTextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = style.description,
                                fontSize = 13.sp,
                                color = BarberTextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Barba recomendada: ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BarberTextPrimary
                                )
                                Text(
                                    text = style.recommendedBeard,
                                    fontSize = 12.sp,
                                    color = BarberTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Subscription Club (SaaS Clubs)
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = "Clube da Barba • Assinaturas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Text(
                    text = "Economize até 40% com planos mensais e cortes à vontade",
                    fontSize = 12.sp,
                    color = BarberTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                viewModel.subscriptionPlans.forEach { plan ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (plan.isPopular) BarberGold else BarberDarkSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = plan.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BarberTextPrimary
                                    )
                                    Text(
                                        text = "R$ ${String.format(Locale.getDefault(), "%.2f", plan.priceMonthly)} / mês",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = BarberGold
                                    )
                                }
                                Surface(
                                    color = if (plan.isPopular) BarberGold else BarberDarkBg,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = plan.badge,
                                        color = if (plan.isPopular) BarberDarkBg else BarberTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            plan.features.forEach { feat ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = BarberGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = feat,
                                        fontSize = 12.sp,
                                        color = BarberTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Assinatura do ${plan.name} ativada no seu perfil!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (plan.isPopular) BarberGold else BarberDarkSurfaceVariant,
                                    contentColor = if (plan.isPopular) BarberDarkBg else BarberTextPrimary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (plan.isPopular) "Quero Ser Imperial VIP" else "Assinar Plano",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Location & Working Hours
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Endereço & Atendimento",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = BarberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Av. Paulista, 1420 - Cerqueira César, São Paulo - SP",
                            fontSize = 13.sp,
                            color = BarberTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = BarberGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Terça a Sábado: 09:00 às 20:00 | Domingo: 09:00 às 15:00",
                            fontSize = 12.sp,
                            color = BarberTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/5511987654321?text=Olá,%20gostaria%20de%20tirar%20uma%20dúvida%20sobre%20a%20Barbearia!")
                            }
                            context.startActivity(intent)
                        },
                        border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = BarberGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WhatsApp da Barbearia",
                            color = BarberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AmenityChip(icon: ImageVector, text: String) {
    Surface(
        color = BarberDarkSurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BarberDarkSurfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BarberGold,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                color = BarberTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = BarberTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = BarberTextSecondary
            )
        }
    }
}

@Composable
fun BarberCard(
    barber: BarberEntity,
    onBookWithBarber: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .width(200.dp)
            .border(1.dp, BarberDarkSurfaceVariant, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(BarberGoldContainer)
                        .border(1.dp, BarberGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = barber.name.take(1),
                        fontWeight = FontWeight.Bold,
                        color = BarberGold,
                        fontSize = 20.sp
                    )
                }

                Surface(
                    color = if (barber.isAvailable) BarberGreen.copy(alpha = 0.2f) else BarberTextMuted.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (barber.isAvailable) "Livre" else "Ocupado",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (barber.isAvailable) BarberGreen else BarberTextMuted,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = barber.nickname,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = BarberTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = barber.role,
                fontSize = 11.sp,
                color = BarberTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            RatingStarsRow(rating = barber.rating, reviewCount = barber.reviewCount)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = barber.specialties,
                fontSize = 11.sp,
                color = BarberTextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onBookWithBarber,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BarberGold,
                    contentColor = BarberDarkBg
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Text("Agendar com ele", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ServiceHighlightCard(
    service: BarbershopServiceEntity,
    onBookService: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = service.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BarberTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${service.durationMinutes} min • ${service.description}",
                    fontSize = 12.sp,
                    color = BarberTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "R$ ${String.format(Locale.getDefault(), "%.2f", service.price)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = BarberGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onBookService,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BarberGold,
                        contentColor = BarberDarkBg
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Agendar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
