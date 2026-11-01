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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.CommercialLicenseEntity
import com.example.data.model.UserRole
import com.example.ui.AppScreen
import com.example.ui.BarberAppViewModel
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
fun MasterAdminScreen(
    viewModel: BarberAppViewModel,
    modifier: Modifier = Modifier
) {
    val licenses by viewModel.allLicenses.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val globalRevenue by viewModel.masterGlobalRevenue.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Master Header Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BarberGold),
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(BarberGoldContainer)
                                    .border(1.dp, BarberGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = BarberGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Painel Master SaaS",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BarberTextPrimary
                                )
                                Text(
                                    text = "Acesso Global Total • Controle de Licenças",
                                    fontSize = 12.sp,
                                    color = BarberGold
                                )
                            }
                        }

                        Surface(
                            color = BarberGold,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "SUPER ADMIN",
                                color = BarberDarkBg,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Você possui privilégios totais de gerenciamento sobre todos os comércios parceiros, contratos de licenças, regras de comissão e clientes do sistema.",
                        fontSize = 13.sp,
                        color = BarberTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Master Fast Tunneling to Commercial and Client Modes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.switchUserRole(UserRole.COMMERCIAL)
                                Toast.makeText(context, "Acessando como Cliente Comercial...", Toast.LENGTH_SHORT).show()
                            },
                            border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Store, contentDescription = null, tint = BarberGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ver Barbearia", fontSize = 11.sp, color = BarberGold, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.switchUserRole(UserRole.CLIENT)
                                Toast.makeText(context, "Acessando como Cliente Consumidor...", Toast.LENGTH_SHORT).show()
                            },
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF64B5F6)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ver Cliente", fontSize = 11.sp, color = Color(0xFF64B5F6), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Global Platform KPIs
        item {
            Column {
                Text(
                    text = "Métricas Globais da Plataforma",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    KpiCard(
                        title = "MRR Licenças SaaS",
                        value = "R$ ${String.format(Locale.getDefault(), "%.2f", globalRevenue)}",
                        icon = Icons.Default.MonetizationOn,
                        accentColor = BarberGold,
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Comércios Ativos",
                        value = "${licenses.filter { it.status == "ATIVA" }.size} Barbearias",
                        icon = Icons.Default.Business,
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
                        title = "Total de Usuários",
                        value = "${allUsers.size + 48} contas",
                        icon = Icons.Default.Group,
                        accentColor = Color(0xFF64B5F6),
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        title = "Status do Servidor",
                        value = "100% Operacional",
                        icon = Icons.Default.CheckCircle,
                        accentColor = BarberGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Commercial Licenses Management
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gestão de Licenças Comerciais",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberTextPrimary
                    )
                    Text(
                        text = "${licenses.size} cadastradas",
                        fontSize = 12.sp,
                        color = BarberGold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                licenses.forEach { lic ->
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
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = lic.businessName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = BarberTextPrimary
                                    )
                                    Text(
                                        text = "Responsável: ${lic.ownerName} (${lic.ownerEmail})",
                                        fontSize = 12.sp,
                                        color = BarberTextSecondary
                                    )
                                }

                                Surface(
                                    color = if (lic.status == "ATIVA") BarberGreen.copy(alpha = 0.2f) else BarberRed.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = lic.status,
                                        color = if (lic.status == "ATIVA") BarberGreen else BarberRed,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Plano: ${lic.planName} • R$ ${String.format(Locale.getDefault(), "%.2f", lic.monthlyFee)}/mês",
                                    fontSize = 12.sp,
                                    color = BarberGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${lic.currentBarbersCount}/${lic.maxBarbers} Barbeiros",
                                    fontSize = 12.sp,
                                    color = BarberTextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Master Action Buttons for this license
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (lic.status == "ATIVA") {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.updateCommercialLicenseStatus(lic.id, "SUSPENSA")
                                            Toast.makeText(context, "Licença suspensa pelo Master.", Toast.LENGTH_SHORT).show()
                                        },
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BarberRed.copy(alpha = 0.6f)),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("Suspender", fontSize = 11.sp, color = BarberRed, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            viewModel.updateCommercialLicenseStatus(lic.id, "ATIVA")
                                            Toast.makeText(context, "Licença ativada com sucesso!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BarberGreen, contentColor = Color.White),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(30.dp)
                                    ) {
                                        Text("Reativar Licença", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Fatura enviada para ${lic.ownerEmail}", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BarberDarkSurfaceVariant, contentColor = BarberTextPrimary),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Cobrança PIX", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
