package com.benoitlefevre.monbudget.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.benoitlefevre.monbudget.database.models.Expense
import com.benoitlefevre.monbudget.database.models.ExpenseType
import com.benoitlefevre.monbudget.database.models.Recurrence
import java.util.*

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM Expense")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE id = :id")
    fun getExpenseById(id: Long): LiveData<Expense>

    @Query("SELECT * FROM Expense WHERE date BETWEEN :dateBegin AND :dateEnd")
    fun getExpensesByPeriod(dateBegin: Date, dateEnd: Date): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE accountId = :accountId")
    fun getExpenseByAccount(accountId: Long): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE accountId = :accountId AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByAccountAndPeriod(
        accountId: Long,
        dateBegin: Date,
        dateEnd: Date
    ): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE amount BETWEEN :minAmount AND :maxAmount")
    fun getExpenseByAmount(minAmount: Float, maxAmount: Float): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE amount BETWEEN :minAmount AND :maxAmount AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByPeriodAndAmount(
        minAmount: Float,
        maxAmount: Float,
        dateBegin: Date,
        dateEnd: Date
    ): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE type = :expenseType")
    fun getExpenseByExpenseType(expenseType: ExpenseType): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE type = :expenseType AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByPeriodAndType(
        expenseType: ExpenseType,
        dateBegin: Date,
        dateEnd: Date
    ): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE owner = :owner")
    fun getExpenseByOwner(owner: String): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE recurrence = :recurrence")
    fun getExpenseByRecurrence(recurrence: Recurrence): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isChecked = 1")
    fun getExpenseWhenIsChecked(): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isChecked = 1 AND date BETWEEN :dateBegin AND :dateEnd")
    fun getExpenseByPeriodWhenIsChecked(dateBegin: Date, dateEnd: Date): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE needPaidBack = 1 AND isPaidBack = 0")
    fun getExpenseIfNeedPaidBack(): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE needPaidBack = 1 AND isPaidBack = 1")
    fun getExpensePaidBack(): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isIncome = 1")
    fun getAllIncomes(): LiveData<List<Expense>>

    @Query("SELECT * FROM Expense WHERE isIncome = 1 AND date BETWEEN :dateBegin AND :dateEnd")
    fun getIncomeByPeriod(dateBegin: Date, dateEnd: Date): LiveData<List<Expense>>

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