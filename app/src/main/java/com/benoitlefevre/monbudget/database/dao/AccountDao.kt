package com.benoitlefevre.monbudget.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benoitlefevre.monbudget.database.models.Account

@Dao
interface AccountDao {

    @Query("SELECT * FROM account")
    fun getAllAccounts() : LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE name = :name")
    fun getAccountByName(name : String) : LiveData<Account>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getAccountById(id : Long) : LiveData<Account>

    @Query("SELECT * FROM account WHERE name = 'CurrentAccount'")
    fun getCurrentAccount() : LiveData<Account>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account : Account) : Long

    @Query("DELETE FROM account where id = :accountId")
    suspend fun deleteAccount(accountId : Long)
}