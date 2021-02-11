package com.benoitlefevre.monbudget.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benoitlefevre.monbudget.database.models.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun getAllAccounts() : Flow<List<Account>>

    @Query("SELECT * FROM account WHERE name = :name")
    fun getAccountByName(name : String) : Flow<Account>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getAccountById(id : Long) : Flow<Account>

    @Query("SELECT * FROM account WHERE name = 'CurrentAccount'")
    fun getCurrentAccount() : Flow<Account>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account : Account) : Long

    @Query("DELETE FROM account where id = :accountId")
    suspend fun deleteAccount(accountId : Long)
}