package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
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

@Composable
fun ServicesScreen(
    viewModel: BarberAppViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val services by viewModel.services.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedServiceCategory.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("Todos", "Combos", "Cabelo", "Barba", "Tratamentos")

    val filteredServices = services.filter { service ->
        val matchesCategory = selectedCategory == "Todos" || service.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                service.name.contains(searchQuery, ignoreCase = true) ||
                service.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg)
    ) {
        // Search & Filters Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BarberDarkSurface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar serviço...", color = BarberTextMuted) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = BarberGold)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = BarberTextSecondary)
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BarberGold,
                    unfocusedBorderColor = BarberDarkSurfaceVariant,
                    focusedTextColor = BarberTextPrimary,
                    unfocusedTextColor = BarberTextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("services_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        onClick = { viewModel.setServiceCategory(category) },
                        color = if (isSelected) BarberGold else BarberDarkSurfaceCard,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) BarberGold else BarberDarkSurfaceVariant
                        )
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) BarberDarkBg else BarberTextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Services List
        if (filteredServices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Nenhum serviço encontrado",
                        fontWeight = FontWeight.Bold,
                        color = BarberTextSecondary,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tente outra busca ou categoria.",
                        color = BarberTextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${filteredServices.size} serviços disponíveis",
                        fontSize = 13.sp,
                        color = BarberTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                items(filteredServices, key = { it.id }) { service ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (service.isPopular) BarberGold.copy(alpha = 0.5f) else BarberDarkSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = service.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BarberTextPrimary
                                    )
                                    if (service.isPopular) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = BarberGoldContainer,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null,
                                                    tint = BarberGold,
                                                    modifier = Modifier.size(10.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Destaque",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = BarberGold
                                                )
                                            }
                                        }
                                    }
                                }

                                Text(
                                    text = "R$ ${String.format(Locale.getDefault(), "%.2f", service.price)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BarberGold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = service.description,
                                fontSize = 13.sp,
                                color = BarberTextSecondary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = BarberDarkBg,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = BarberTextMuted,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${service.durationMinutes} min",
                                            fontSize = 11.sp,
                                            color = BarberTextSecondary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "•  ${service.category}",
                                            fontSize = 11.sp,
                                            color = BarberTextMuted
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        viewModel.startBookingWithService(service)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = BarberGold,
                                        contentColor = BarberDarkBg
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Agendar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
