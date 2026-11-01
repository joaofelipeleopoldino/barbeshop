package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AppointmentEntity
import com.example.data.model.BarberEntity
import com.example.data.model.BarbershopServiceEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.CommercialLicenseEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :identifier OR email = :identifier LIMIT 1")
    suspend fun getUserByUsernameOrEmail(identifier: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun update(user: UserEntity)

    @Query("UPDATE users SET password = :newPassword WHERE email = :email OR username = :email")
    suspend fun updatePassword(email: String, newPassword: String): Int

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)
}

@Dao
interface CommercialLicenseDao {
    @Query("SELECT * FROM commercial_licenses ORDER BY id ASC")
    fun getAllLicenses(): Flow<List<CommercialLicenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(licenses: List<CommercialLicenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(license: CommercialLicenseEntity): Long

    @Update
    suspend fun update(license: CommercialLicenseEntity)

    @Query("UPDATE commercial_licenses SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services WHERE isActive = 1 ORDER BY isPopular DESC, name ASC")
    fun getAllActiveServices(): Flow<List<BarbershopServiceEntity>>

    @Query("SELECT * FROM services ORDER BY category ASC, name ASC")
    fun getAllServicesForManagement(): Flow<List<BarbershopServiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(services: List<BarbershopServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(service: BarbershopServiceEntity)

    @Update
    suspend fun update(service: BarbershopServiceEntity)

    @Query("UPDATE services SET isActive = :isActive WHERE id = :id")
    suspend fun setServiceActive(id: Int, isActive: Boolean)
}

@Dao
interface BarberDao {
    @Query("SELECT * FROM barbers ORDER BY rating DESC")
    fun getAllBarbers(): Flow<List<BarberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(barbers: List<BarberEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(barber: BarberEntity)

    @Update
    suspend fun update(barber: BarberEntity)

    @Query("UPDATE barbers SET isAvailable = :isAvailable WHERE id = :id")
    suspend fun setBarberAvailability(id: Int, isAvailable: Boolean)
}

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY id DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE appointmentDate = :date ORDER BY appointmentTime ASC")
    fun getAppointmentsForDate(date: String): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: AppointmentEntity): Long

    @Update
    suspend fun update(appointment: AppointmentEntity)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY isFeatured DESC, name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Update
    suspend fun update(product: ProductEntity)

    @Query("UPDATE products SET stockQuantity = stockQuantity - :quantity WHERE id = :productId AND stockQuantity >= :quantity")
    suspend fun deductStock(productId: Int, quantity: Int)
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun getItemByProductId(productId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItemEntity)

    @Update
    suspend fun update(cartItem: CartItemEntity)

    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY createdAtTimestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long
}
