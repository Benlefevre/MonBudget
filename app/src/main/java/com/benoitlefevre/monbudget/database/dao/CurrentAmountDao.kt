package com.benoitlefevre.monbudget.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benoitlefevre.monbudget.database.models.CurrentAmount
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CurrentAmountDao {

    @Query("SELECT * FROM CurrentAmount")
    fun getAllCurrentAmount() : Flow<List<CurrentAmount>>

    @Query("SELECT * FROM CurrentAmount WHERE date BETWEEN :dateBegin AND :dateEnd ")
    fun getCurrentAmountByPeriod(dateBegin : Date, dateEnd : Date) : Flow<List<CurrentAmount>>

    @Query("SELECT * FROM CurrentAmount WHERE accountId = :accountId")
    fun getAllCurrentAmountByAccountId(accountId : Long) : Flow<List<CurrentAmount>>

    @Query("SELECT * FROM CurrentAmount WHERE id = :id")
    fun getCurrentAmountById(id : Long) : Flow<CurrentAmount>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentAmount(currentAmount: CurrentAmount)

    @Query("DELETE FROM CurrentAmount WHERE date BETWEEN :dateBegin AND :dateEnd")
    suspend fun deleteCurrentAmountByPeriod(dateBegin: Date, dateEnd: Date)

    @Query("DELETE FROM CurrentAmount WHERE id = :id")
    suspend fun deleteCurrentAmountById(id: Long)

    @Query("DELETE FROM CurrentAmount WHERE accountId = :accountId")
    suspend fun deleteCurrentAmountByAccountId(accountId: Long)
}