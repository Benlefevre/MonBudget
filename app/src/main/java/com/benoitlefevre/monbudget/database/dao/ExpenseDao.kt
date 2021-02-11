package com.benoitlefevre.monbudget.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benoitlefevre.monbudget.database.models.Expense
import com.benoitlefevre.monbudget.database.models.ExpenseType
import com.benoitlefevre.monbudget.database.models.Recurrence
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM Expense")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE id = :id")
    fun getExpenseById(id: Long): Flow<Expense>

    @Query("SELECT * FROM Expense WHERE date BETWEEN :dateBegin AND :dateEnd")
    fun getExpensesByPeriod(dateBegin: Date, dateEnd: Date): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE accountId = :accountId")
    fun getExpenseByAccount(accountId: Long): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE accountId = :accountId AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByAccountAndPeriod(
        accountId: Long,
        dateBegin: Date,
        dateEnd: Date
    ): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE amount BETWEEN :minAmount AND :maxAmount")
    fun getExpenseByAmount(minAmount: Float, maxAmount: Float): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE amount BETWEEN :minAmount AND :maxAmount AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByPeriodAndAmount(
        minAmount: Float,
        maxAmount: Float,
        dateBegin: Date,
        dateEnd: Date
    ): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE type = :expenseType")
    fun getExpenseByExpenseType(expenseType: ExpenseType): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE type = :expenseType AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByPeriodAndType(
        expenseType: ExpenseType,
        dateBegin: Date,
        dateEnd: Date
    ): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE owner = :owner")
    fun getExpenseByOwner(owner: String): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE recurrence = :recurrence")
    fun getExpenseByRecurrence(recurrence: Recurrence): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isChecked = 1")
    fun getExpenseWhenIsChecked(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isChecked = 1 AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByPeriodWhenIsChecked(dateBegin: Date, dateEnd: Date): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE needPaidBack = 1 AND isPaidBack = 0")
    fun getExpenseIfNeedPaidBack(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE needPaidBack = 1 AND isPaidBack = 1")
    fun getExpensePaidBack(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isIncome = 1")
    fun getAllIncomes(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isIncome = 1 AND date BETWEEN :dateBegin AND :dateEnd")
    fun getIncomeByPeriod(dateBegin: Date, dateEnd: Date): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExpenses(expenses: List<Expense>)

    @Query("DELETE FROM Expense WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    @Query("DELETE FROM Expense WHERE accountId = :accountId")
    suspend fun deleteExpenseByAccountId(accountId: Long)

    @Query("DELETE FROM Expense WHERE date BETWEEN :dateBegin AND :dateEnd")
    suspend fun deleteExpenseByPeriod(dateBegin: Date, dateEnd: Date)
}