package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ProductEntity
import com.example.ui.BarberAppViewModel
import com.example.ui.components.RatingStarsRow
import com.example.ui.theme.BarberDarkBg
import com.example.ui.theme.BarberDarkSurface
import com.example.ui.theme.BarberDarkSurfaceCard
import com.example.ui.theme.BarberDarkSurfaceVariant
import com.example.ui.theme.BarberGold
import com.example.ui.theme.BarberGoldContainer
import com.example.ui.theme.BarberGreen
import com.example.ui.theme.BarberTextMuted
import com.example.ui.theme.BarberTextPrimary
import com.example.ui.theme.BarberTextSecondary
import java.util.Locale

@Composable
fun ShopScreen(
    viewModel: BarberAppViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedProductCategory.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val categories = listOf("Todos", "Pomadas", "Barba", "Cabelo", "Acessórios")

    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategory == "Todos" || product.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    val cartCount = cartItems.sumOf { it.quantity }
    val cartTotal = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BarberDarkBg,
        floatingActionButton = {
            if (cartCount > 0) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openCart() },
                    containerColor = BarberGold,
                    contentColor = BarberDarkBg,
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) },
                    text = {
                        Text(
                            text = "Ver Carrinho ($cartCount) • R$ ${String.format(Locale.getDefault(), "%.2f", cartTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("floating_cart_button")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header Search & Category Filter
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BarberDarkSurface)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar produtos, pomadas, óleos...", color = BarberTextMuted) },
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
                        .testTag("shop_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            onClick = { viewModel.setProductCategory(category) },
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

            // Products Grid
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhum produto encontrado",
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredProducts, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {
                                viewModel.addToCart(product)
                                Toast.makeText(
                                    context,
                                    "${product.name} adicionado ao carrinho!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ProductEntity,
    onAddToCart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BarberDarkSurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (product.isFeatured) BarberGold.copy(alpha = 0.5f) else BarberDarkSurfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Top Badge and Volume
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = BarberDarkBg,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = product.volumeOrSize,
                        fontSize = 10.sp,
                        color = BarberTextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                if (product.isFeatured) {
                    Surface(
                        color = BarberGoldContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "VIP",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BarberGold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Icon Placeholder Visual
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BarberDarkBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = BarberGold.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BarberTextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            RatingStarsRow(rating = product.rating)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "R$ ${String.format(Locale.getDefault(), "%.2f", product.price)}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = BarberGold
            )

            Text(
                text = "${product.stockQuantity} un em estoque",
                fontSize = 10.sp,
                color = if (product.stockQuantity > 5) BarberGreen else BarberTextMuted
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BarberGold,
                    contentColor = BarberDarkBg
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Comprar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
