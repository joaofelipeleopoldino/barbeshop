package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppointmentEntity
import com.example.ui.BarberAppViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BarberDarkBg
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

@Composable
fun SaaSDashboardScreen(
    viewModel: BarberAppViewModel,
    modifier: Modifier = Modifier
) {
    val metrics by viewModel.saasMetrics.collectAsStateWithLifecycle()
    val appointments by viewModel.appointments.collectAsStateWithLifecycle()
    val barbers by viewModel.barbers.collectAsStateWithLifecycle()
    val commissions by viewModel.barberCommissions.collectAsStateWithLifecycle()
    val services by viewModel.allServicesForManagement.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showNewServiceDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SaaS Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(BarberGoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = BarberGold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "BarberPro SaaS Admin",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BarberTextPrimary
                                )
                                Text(
                                    text = "Unidade Matriz • São Paulo",
                                    fontSize = 12.sp,
                                    color = BarberTextSecondary
                                )
                            }
                        }

                        Surface(
                            color = BarberGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "SaaS Ativo",
                                color = BarberGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Manager Bulk Action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Lembretes de horário disparados para todos os clientes de hoje!",
                                    Toast.LENGTH_LONG
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BarberGold,
                                contentColor = BarberDarkBg
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Disparar Lembretes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { showNewServiceDialog = true },
                            border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = BarberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Novo Serviço", fontSize = 11.sp, color = BarberGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Metrics Grid (KPIs)
        item {
            Column {
                Text(
                    text = "Métricas Financeiras & Operacionais",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "Faturamento Hoje",
                        value = "R$ ${String.format(Locale.getDefault(), "%.2f", metrics.dailyRevenue)}",
                        icon = Icons.Default.AttachMoney,
                        accentColor = BarberGold,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "MRR Mensal",
                        value = "R$ ${String.format(Locale.getDefault(), "%.0f", metrics.monthlyRevenue)}",
                        icon = Icons.Default.TrendingUp,
                        accentColor = BarberGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "Atendimentos Hoje",
                        value = "${metrics.todayAppointmentsCount} clientes",
                        icon = Icons.Default.CalendarMonth,
                        accentColor = BarberGold,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Taxa de Ocupação",
                        value = "${metrics.occupancyRatePercentage}% das cadeiras",
                        icon = Icons.Default.Group,
                        accentColor = BarberGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Today's Operational Queue & Appointment Management
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Fila de Atendimento de Hoje",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Text(
                        text = "${metrics.pendingAppointmentsCount} pendentes",
                        fontSize = 12.sp,
                        color = BarberGold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                if (appointments.isEmpty()) {
                    Text(
                        text = "Sem agendamentos registrados no momento.",
                        color = BarberTextMuted,
                        fontSize = 13.sp
                    )
                } else {
                    appointments.take(6).forEach { appointment ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${appointment.appointmentTime} • ${appointment.clientName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = BarberTextPrimary
                                        )
                                        Text(
                                            text = "Barbeiro: ${appointment.barberName} • Tel: ${appointment.clientPhone}",
                                            fontSize = 12.sp,
                                            color = BarberTextSecondary
                                        )
                                    }
                                    StatusBadge(status = appointment.status)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "${appointment.serviceNames} • R$ ${String.format(Locale.getDefault(), "%.2f", appointment.totalPrice)}",
                                    fontSize = 12.sp,
                                    color = BarberGold,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Quick Status Changer Buttons for Barber/Manager
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (appointment.status == "CONFIRMADO") {
                                        Button(
                                            onClick = {
                                                viewModel.updateAppointmentStatus(appointment.id, "EM_ATENDIMENTO")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = BarberGoldContainer,
                                                contentColor = BarberGold
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Iniciar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    if (appointment.status == "EM_ATENDIMENTO" || appointment.status == "CONFIRMADO") {
                                        Button(
                                            onClick = {
                                                viewModel.updateAppointmentStatus(appointment.id, "CONCLUIDO")
                                                Toast.makeText(context, "Atendimento concluído! Comissão creditada.", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = BarberGreen.copy(alpha = 0.2f),
                                                contentColor = BarberGreen
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Concluir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Barber Staff & Commission Management
        item {
            Column {
                Text(
                    text = "Gestão de Barbeiros & Comissões (SaaS)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Text(
                    text = "Cálculo de comissão por corte e status das cadeiras",
                    fontSize = 12.sp,
                    color = BarberTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                barbers.forEach { barber ->
                    val commissionInfo = commissions.find { it.barberId == barber.id }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
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
                                    text = barber.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = BarberTextPrimary
                                )
                                Text(
                                    text = "${barber.role} • ${(barber.commissionRate * 100).toInt()}% comissão",
                                    fontSize = 12.sp,
                                    color = BarberTextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Comissão a pagar: R$ ${String.format(Locale.getDefault(), "%.2f", commissionInfo?.commissionAmount ?: 0.0)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BarberGold
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (barber.isAvailable) "Livre" else "Ocupado",
                                    fontSize = 11.sp,
                                    color = if (barber.isAvailable) BarberGreen else BarberTextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                                Switch(
                                    checked = barber.isAvailable,
                                    onCheckedChange = { viewModel.toggleBarberStatus(barber.id, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = BarberGold,
                                        checkedTrackColor = BarberGoldContainer,
                                        uncheckedThumbColor = BarberTextMuted,
                                        uncheckedTrackColor = BarberDarkSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Catalog & Service Toggle for SaaS Manager
        item {
            Column {
                Text(
                    text = "Controle de Ativação do Catálogo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Text(
                    text = "Pause ou reative serviços visíveis aos clientes",
                    fontSize = 12.sp,
                    color = BarberTextSecondary,
                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                services.take(6).forEach { s ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
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
                                    text = s.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = if (s.isActive) BarberTextPrimary else BarberTextMuted
                                )
                                Text(
                                    text = "R$ ${String.format(Locale.getDefault(), "%.2f", s.price)} • ${s.durationMinutes} min",
                                    fontSize = 11.sp,
                                    color = BarberGold
                                )
                            }
                            Switch(
                                checked = s.isActive,
                                onCheckedChange = { viewModel.toggleServiceStatus(s.id, it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = BarberGold,
                                    checkedTrackColor = BarberGoldContainer,
                                    uncheckedThumbColor = BarberTextMuted,
                                    uncheckedTrackColor = BarberDarkSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal to Add New Service
    if (showNewServiceDialog) {
        var newServiceName by remember { mutableStateOf("") }
        var newServiceCategory by remember { mutableStateOf("Cabelo") }
        var newServicePrice by remember { mutableStateOf("50.00") }
        var newServiceDuration by remember { mutableStateOf("40") }
        var newServiceDesc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewServiceDialog = false },
            containerColor = BarberDarkSurfaceCard,
            title = {
                Text(
                    text = "Adicionar Novo Serviço",
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newServiceName,
                        onValueChange = { newServiceName = it },
                        label = { Text("Nome do Serviço") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newServiceCategory,
                        onValueChange = { newServiceCategory = it },
                        label = { Text("Categoria (Cabelo, Barba, Combos, etc)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newServicePrice,
                            onValueChange = { newServicePrice = it },
                            label = { Text("Preço R$") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BarberGold,
                                unfocusedBorderColor = BarberDarkSurfaceVariant,
                                focusedTextColor = BarberTextPrimary,
                                unfocusedTextColor = BarberTextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newServiceDuration,
                            onValueChange = { newServiceDuration = it },
                            label = { Text("Minutos") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BarberGold,
                                unfocusedBorderColor = BarberDarkSurfaceVariant,
                                focusedTextColor = BarberTextPrimary,
                                unfocusedTextColor = BarberTextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = newServiceDesc,
                        onValueChange = { newServiceDesc = it },
                        label = { Text("Descrição detalhada") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val price = newServicePrice.toDoubleOrNull() ?: 50.0
                        val duration = newServiceDuration.toIntOrNull() ?: 30
                        if (newServiceName.isNotBlank()) {
                            viewModel.addNewService(
                                name = newServiceName,
                                category = newServiceCategory,
                                price = price,
                                duration = duration,
                                description = newServiceDesc.ifBlank { "Serviço profissional de barbearia." }
                            )
                            Toast.makeText(context, "Novo serviço cadastrado no BarberPro!", Toast.LENGTH_SHORT).show()
                            showNewServiceDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BarberGold, contentColor = BarberDarkBg)
                ) {
                    Text("Salvar Serviço", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewServiceDialog = false }) {
                    Text("Cancelar", color = BarberTextSecondary)
                }
            }
        )
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BarberDarkSurfaceVariant),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = BarberTextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BarberTextPrimary
            )
        }
    }
}
