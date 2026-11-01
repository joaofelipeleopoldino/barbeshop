package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AppointmentEntity
import com.example.ui.AppScreen
import com.example.ui.BarberAppViewModel
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BarberDarkBg
import com.example.ui.theme.BarberDarkSurfaceCard
import com.example.ui.theme.BarberDarkSurfaceVariant
import com.example.ui.theme.BarberGold
import com.example.ui.theme.BarberRed
import com.example.ui.theme.BarberTextMuted
import com.example.ui.theme.BarberTextPrimary
import com.example.ui.theme.BarberTextSecondary
import java.util.Locale

@Composable
fun MyAppointmentsScreen(
    viewModel: BarberAppViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val appointments by viewModel.appointments.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Meus Agendamentos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BarberTextPrimary
            )
            Text(
                text = "Acompanhe seus horários marcados na Barbearia Dom Navalha",
                fontSize = 13.sp,
                color = BarberTextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (appointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = BarberTextMuted,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Você não possui agendamentos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Escolha seus serviços e garanta seu horário com nossos mestres barbeiros.",
                        color = BarberTextSecondary,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigate(AppScreen.BOOKING) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BarberGold,
                            contentColor = BarberDarkBg
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Agendar Agora", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments, key = { it.id }) { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        onCancel = {
                            viewModel.cancelAppointment(appointment.id)
                            Toast.makeText(context, "Agendamento cancelado.", Toast.LENGTH_SHORT).show()
                        },
                        onOpenWhatsApp = {
                            val text = "Olá! Gostaria de confirmar meu agendamento do dia ${appointment.appointmentDate} às ${appointment.appointmentTime} com ${appointment.barberName}."
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/5511987654321?text=${Uri.encode(text)}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentEntity,
    onCancel: () -> Unit,
    onOpenWhatsApp: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BarberDarkSurfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Date, Time & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${appointment.appointmentDate} • ${appointment.appointmentTime}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }

                StatusBadge(status = appointment.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Professional
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BarberGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Barbeiro: ${appointment.barberName}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = BarberTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Services
            Text(
                text = appointment.serviceNames,
                fontSize = 13.sp,
                color = BarberTextSecondary
            )

            if (appointment.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Obs: ${appointment.notes}",
                    fontSize = 11.sp,
                    color = BarberTextMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Price & Duration
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${appointment.totalDurationMinutes} min estimados",
                    fontSize = 12.sp,
                    color = BarberTextMuted
                )

                Text(
                    text = "R$ ${String.format(Locale.getDefault(), "%.2f", appointment.totalPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BarberGold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenWhatsApp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", fontSize = 11.sp, color = BarberGold, fontWeight = FontWeight.Bold)
                }

                if (appointment.status == "CONFIRMADO") {
                    OutlinedButton(
                        onClick = onCancel,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BarberRed.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = BarberRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Cancelar", fontSize = 11.sp, color = BarberRed, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
