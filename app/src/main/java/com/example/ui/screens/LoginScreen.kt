package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.ui.components.QuickAccountItem
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.BarberAppViewModel
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

@Composable
fun LoginScreen(
    viewModel: BarberAppViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Entrar, 1: Criar Conta

    // Login Form State
    var loginIdentifier by remember { mutableStateOf("master") }
    var loginPassword by remember { mutableStateOf("mater123") }
    var showPassword by remember { mutableStateOf(false) }

    // Register Form State
    var regName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regRole by remember { mutableStateOf("CLIENT") } // "CLIENT" or "COMMERCIAL"
    var regBusinessName by remember { mutableStateOf("") }

    // Forgot Password Dialog
    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var forgotNewPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BarberDarkBg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Branding Logo
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(BarberGoldContainer)
                .border(2.dp, BarberGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ContentCut,
                contentDescription = null,
                tint = BarberGold,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "BarberPro",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = BarberTextPrimary
        )

        Text(
            text = "Plataforma SaaS Multi-tenant para Barbearias",
            fontSize = 12.sp,
            color = BarberGold,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tab Selector: Entrar vs Criar Conta
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = BarberDarkSurface,
            contentColor = BarberGold,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = BarberGold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Entrar", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Criar Conta", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // LOGIN FORM
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BarberDarkSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Acesse sua conta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BarberTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = loginIdentifier,
                        onValueChange = { loginIdentifier = it },
                        label = { Text("Usuário ou E-mail") },
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
                            .testTag("login_input_identifier")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it },
                        label = { Text("Senha") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = BarberGold)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = BarberTextSecondary
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_input_password")
                    )

                    // Forgot Password link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                forgotEmail = loginIdentifier
                                showForgotDialog = true
                            },
                            modifier = Modifier.testTag("forgot_password_button")
                        ) {
                            Text(
                                text = "Esqueci minha senha",
                                fontSize = 12.sp,
                                color = BarberGold,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            viewModel.login(loginIdentifier, loginPassword) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BarberGold,
                            contentColor = BarberDarkBg
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_submit_button")
                    ) {
                        Text(
                            text = "Entrar no Sistema",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        } else {
            // REGISTER FORM
            Card(
                colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BarberDarkSurfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Cadastre-se na Plataforma",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BarberTextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Qual o seu perfil de uso?",
                        fontSize = 12.sp,
                        color = BarberTextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Role Picker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            onClick = { regRole = "CLIENT" },
                            color = if (regRole == "CLIENT") BarberGoldContainer else BarberDarkSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (regRole == "CLIENT") BarberGold else Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (regRole == "CLIENT") BarberGold else BarberTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Cliente Final",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (regRole == "CLIENT") BarberGold else BarberTextPrimary
                                )
                                Text(
                                    text = "Agendar & Comprar",
                                    fontSize = 10.sp,
                                    color = BarberTextMuted
                                )
                            }
                        }

                        Surface(
                            onClick = { regRole = "COMMERCIAL" },
                            color = if (regRole == "COMMERCIAL") BarberGoldContainer else BarberDarkSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (regRole == "COMMERCIAL") BarberGold else Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = if (regRole == "COMMERCIAL") BarberGold else BarberTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Comercial / Barbearia",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (regRole == "COMMERCIAL") BarberGold else BarberTextPrimary
                                )
                                Text(
                                    text = "Licença de Uso SaaS",
                                    fontSize = 10.sp,
                                    color = BarberTextMuted
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = regName,
                        onValueChange = { regName = it },
                        label = { Text("Nome Completo") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (regRole == "COMMERCIAL") {
                        OutlinedTextField(
                            value = regBusinessName,
                            onValueChange = { regBusinessName = it },
                            label = { Text("Nome da sua Barbearia / Comércio") },
                            leadingIcon = {
                                Icon(Icons.Default.Business, contentDescription = null, tint = BarberGold)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BarberGold,
                                unfocusedBorderColor = BarberDarkSurfaceVariant,
                                focusedTextColor = BarberTextPrimary,
                                unfocusedTextColor = BarberTextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = { regEmail = it },
                        label = { Text("E-mail") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = BarberGold)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = regPhone,
                        onValueChange = { regPhone = it },
                        label = { Text("WhatsApp / Telefone") },
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
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = regPassword,
                        onValueChange = { regPassword = it },
                        label = { Text("Criar Senha") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = BarberGold)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            viewModel.register(
                                username = regEmail.substringBefore("@"),
                                email = regEmail,
                                pass = regPassword,
                                name = regName,
                                phone = regPhone,
                                role = regRole,
                                businessName = if (regRole == "COMMERCIAL") regBusinessName else ""
                            ) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BarberGold,
                            contentColor = BarberDarkBg
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Concluir Cadastro", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SOCIAL LOGIN / CONEXÃO SOCIAL (Gmail, Facebook, Instagram)
        Text(
            text = "Ou conecte-se rapidamente com:",
            fontSize = 12.sp,
            color = BarberTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialLoginButton(
                providerName = "Gmail",
                color = Color(0xFFEA4335),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.socialConnect("Google", "CLIENT") { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            )

            SocialLoginButton(
                providerName = "Facebook",
                color = Color(0xFF1877F2),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.socialConnect("Facebook", "CLIENT") { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            )

            SocialLoginButton(
                providerName = "Instagram",
                color = Color(0xFFE1306C),
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.socialConnect("Instagram", "CLIENT") { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(color = BarberDarkSurfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))

        // QUICK ROLE PREVIEW SHORTCUTS (Required by User for Master / Commercial / Client testing)
        Card(
            colors = CardDefaults.cardColors(containerColor = BarberDarkSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BarberGold.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Acesso Rápido aos 3 Tipos de Usuários:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BarberGold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 1. MASTER USER
                QuickAccountItem(
                    title = "1. Usuário Master (Acesso Total)",
                    credentials = "Login: master | Senha: mater123",
                    badgeText = "SUPER ADMIN",
                    badgeColor = BarberGold,
                    onClick = {
                        loginIdentifier = "master"
                        loginPassword = "mater123"
                        viewModel.switchUserRole(UserRole.MASTER)
                        Toast.makeText(context, "Conectado como USUÁRIO MASTER!", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 2. COMMERCIAL CLIENT
                QuickAccountItem(
                    title = "2. Cliente Comercial (Licença SaaS)",
                    credentials = "Dono de Barbearia com gestão e comissões",
                    badgeText = "COMERCIAL",
                    badgeColor = Color(0xFF42A5F5),
                    onClick = {
                        loginIdentifier = "comercial"
                        loginPassword = "barber123"
                        viewModel.switchUserRole(UserRole.COMMERCIAL)
                        Toast.makeText(context, "Conectado como CLIENTE COMERCIAL (Barbearia)!", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 3. REGULAR CLIENT
                QuickAccountItem(
                    title = "3. Cliente Final (Consumidor)",
                    credentials = "Compra produtos e agenda serviços na barbearia",
                    badgeText = "CLIENTE",
                    badgeColor = BarberGreen,
                    onClick = {
                        loginIdentifier = "cliente"
                        loginPassword = "cliente123"
                        viewModel.switchUserRole(UserRole.CLIENT)
                        Toast.makeText(context, "Conectado como CLIENTE FINAL!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // FORGOT PASSWORD MODAL (Recuperação de Senha)
    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            containerColor = BarberDarkSurfaceCard,
            title = {
                Text(
                    text = "Recuperação de Senha",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BarberTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Informe seu e-mail ou usuário para redefinir sua senha com segurança.",
                        fontSize = 13.sp,
                        color = BarberTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = { forgotEmail = it },
                        label = { Text("E-mail ou Usuário") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = forgotNewPassword,
                        onValueChange = { forgotNewPassword = it },
                        label = { Text("Nova Senha Desejada") },
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = BarberDarkSurfaceVariant,
                            focusedTextColor = BarberTextPrimary,
                            unfocusedTextColor = BarberTextPrimary
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (forgotEmail.isNotBlank() && forgotNewPassword.isNotBlank()) {
                            viewModel.recoverPassword(forgotEmail, forgotNewPassword) { success, msg ->
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                if (success) {
                                    showForgotDialog = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "Preencha todos os campos para recuperar a senha.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BarberGold,
                        contentColor = BarberDarkBg
                    )
                ) {
                    Text("Redefinir Senha", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotDialog = false }) {
                    Text("Cancelar", color = BarberTextSecondary)
                }
            }
        )
    }
}

@Composable
fun SocialLoginButton(
    providerName: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = BarberDarkSurfaceCard,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = providerName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BarberTextPrimary
            )
        }
    }
}
