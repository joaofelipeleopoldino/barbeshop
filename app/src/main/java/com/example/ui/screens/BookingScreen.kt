package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BarberEntity
import com.example.data.model.BarbershopServiceEntity
import com.example.ui.BarberAppViewModel
import com.example.ui.theme.BarberDarkBg
import com.example.ui.theme.BarberDarkSurface
import com.example.ui.theme.BarberDarkSurfaceCard
import com.example.ui.theme.BarberDarkSurfaceVariant
import com.example.ui.theme.BarberGold
import com.example.ui.theme.BarberGoldContainer
import com.example.ui.theme.BarberTextMuted
import com.example.ui.theme.BarberTextPrimary
import com.example.ui.theme.BarberTextSecondary
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingScreen(
    viewModel: BarberAppViewModel,
    modifier: Modifier = Modifier
) {
    val services by viewModel.services.collectAsStateWithLifecycle()
    val barbers by viewModel.barbers.collectAsStateWithLifecycle()
    val selectedServices by viewModel.bookingSelectedServices.collectAsStateWithLifecycle()
    val selectedBarber by viewModel.bookingSelectedBarber.collectAsStateWithLifecycle()
    val selectedDate by viewModel.bookingSelectedDate.collectAsStateWithLifecycle()
    val selectedTime by viewModel.bookingSelectedTime.collectAsStateWithLifecycle()
    val clientName by viewModel.bookingClientName.collectAsStateWithLifecycle()
    val clientPhone by viewModel.bookingClientPhone.collectAsStateWithLifecycle()
    val bookingNotes by viewModel.bookingNotes.collectAsStateWithLifecycle()

    val availableDates = remember { BarberAppViewModel.getNextDays(10) }
    val availableTimeSlots = remember {
        listOf(
            "09:00", "09:45", "10:30", "11:15",
            "14:00", "14:45", "15:30", "16:15",
            "17:00", "18:00", "18:45", "19:30"
        )
    }

    val totalDuration = selectedServices.sumOf { it.durationMinutes }
    val totalPrice = selectedServices.sumOf { it.price }
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sticky/Top Summary Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberGoldContainer),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Agendamento Online",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BarberGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (selectedServices.isEmpty())
                                "Selecione pelo menos um serviço abaixo"
                            else
                                "${selectedServices.size} serviço(s) • $totalDuration min estimados",
                            fontSize = 13.sp,
                            color = BarberTextPrimary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total",
                            fontSize = 11.sp,
                            color = BarberTextSecondary
                        )
                        Text(
                            text = "R$ ${String.format(Locale.getDefault(), "%.2f", totalPrice)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BarberGold
                        )
                    }
                }
            }
        }

        // Step 1: Select Services
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "1. Escolha o(s) Serviço(s)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                services.forEach { service ->
                    val isChecked = selectedServices.contains(service)
                    Card(
                        onClick = { viewModel.toggleServiceInBooking(service) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChecked) BarberDarkSurfaceVariant else BarberDarkSurfaceCard
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isChecked) BarberGold else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { viewModel.toggleServiceInBooking(service) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = BarberGold,
                                        uncheckedColor = BarberTextSecondary,
                                        checkmarkColor = BarberDarkBg
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = service.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BarberTextPrimary
                                    )
                                    Text(
                                        text = "${service.durationMinutes} min • ${service.category}",
                                        fontSize = 12.sp,
                                        color = BarberTextSecondary
                                    )
                                }
                            }

                            Text(
                                text = "R$ ${String.format(Locale.getDefault(), "%.2f", service.price)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BarberGold
                            )
                        }
                    }
                }
            }
        }

        // Step 2: Select Barber
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "2. Escolha o Profissional",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Option: Any Barber
                    item {
                        val isSelected = selectedBarber == null
                        Surface(
                            onClick = { viewModel.selectBarber(null) },
                            color = if (isSelected) BarberGoldContainer else BarberDarkSurfaceCard,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) BarberGold else BarberDarkSurfaceVariant
                            ),
                            modifier = Modifier.width(130.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) BarberGold else BarberDarkBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = if (isSelected) BarberDarkBg else BarberGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Qualquer um",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BarberTextPrimary
                                )
                                Text(
                                    text = "Primeiro livre",
                                    fontSize = 11.sp,
                                    color = BarberTextSecondary
                                )
                            }
                        }
                    }

                    // Barbers
                    items(barbers, key = { it.id }) { barber ->
                        val isSelected = selectedBarber?.id == barber.id
                        Surface(
                            onClick = { viewModel.selectBarber(barber) },
                            color = if (isSelected) BarberGoldContainer else BarberDarkSurfaceCard,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) BarberGold else BarberDarkSurfaceVariant
                            ),
                            modifier = Modifier.width(140.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(BarberDarkBg)
                                        .border(1.dp, BarberGold, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = barber.name.take(1),
                                        fontWeight = FontWeight.Bold,
                                        color = BarberGold,
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = barber.nickname,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BarberTextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "★ ${barber.rating}",
                                    fontSize = 11.sp,
                                    color = BarberGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Step 3: Select Date
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "3. Escolha a Data",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(availableDates) { (dateStr, label) ->
                        val isSelected = selectedDate == dateStr
                        Surface(
                            onClick = { viewModel.selectDate(dateStr) },
                            color = if (isSelected) BarberGold else BarberDarkSurfaceCard,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) BarberGold else BarberDarkSurfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isSelected) BarberDarkBg else BarberTextPrimary
                                )
                                Text(
                                    text = dateStr.take(5), // "dd/MM"
                                    fontSize = 11.sp,
                                    color = if (isSelected) BarberDarkBg.copy(alpha = 0.8f) else BarberTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Step 4: Select Time Slot
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "4. Escolha o Horário",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableTimeSlots.forEach { slot ->
                        val isSelected = selectedTime == slot
                        Surface(
                            onClick = { viewModel.selectTime(slot) },
                            color = if (isSelected) BarberGold else BarberDarkSurfaceCard,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) BarberGold else BarberDarkSurfaceVariant
                            )
                        ) {
                            Text(
                                text = slot,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BarberDarkBg else BarberTextPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

        // Step 5: Client Information
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "5. Seus Dados para Contato",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { viewModel.updateClientName(it) },
                        label = { Text("Seu Nome") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = BarberGold)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_input_name")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { viewModel.updateClientPhone(it) },
                        label = { Text("WhatsApp para Confirmação") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = BarberGold)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_input_phone")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bookingNotes,
                        onValueChange = { viewModel.updateBookingNotes(it) },
                        label = { Text("Observação ou Preferência (Opcional)") },
                        placeholder = { Text("Ex: Cabelo fino, toalha bem quente...") },
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
            }
        }

        // Final Confirm Button
        item {
            Button(
                onClick = {
                    if (selectedServices.isEmpty()) {
                        Toast.makeText(context, "Por favor, selecione ao menos um serviço!", Toast.LENGTH_SHORT).show()
                    } else if (clientName.isBlank() || clientPhone.isBlank()) {
                        Toast.makeText(context, "Por favor, informe seu nome e telefone!", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.confirmBooking()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BarberGold,
                    contentColor = BarberDarkBg
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("booking_confirm_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedServices.isEmpty())
                            "Selecione um Serviço"
                        else
                            "Confirmar Agendamento • R$ ${String.format(Locale.getDefault(), "%.2f", totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
