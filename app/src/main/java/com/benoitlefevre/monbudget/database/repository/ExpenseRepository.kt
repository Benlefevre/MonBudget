package com.benoitlefevre.monbudget.database.repository

import com.benoitlefevre.monbudget.database.dao.ExpenseDao
import com.benoitlefevre.monbudget.database.models.Expense
import com.benoitlefevre.monbudget.database.models.ExpenseType
import com.benoitlefevre.monbudget.database.models.Recurrence
import kotlinx.coroutines.flow.Flow
import java.util.*

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    fun getExpenseById(id: Long): Flow<Expense> {
        return expenseDao.getExpenseById(id)
    }

    fun getExpenseByPeriod(dateBegin: Date, dateEnd: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesByPeriod(dateBegin, dateEnd)
    }

    fun getExpenseByAccount(accountId : Long) : Flow<List<Expense>>{
        return expenseDao.getExpenseByAccount(accountId)
    }

    fun getExpenseByPeriodAndAccount(accountId: Long,dateBegin: Date,dateEnd: Date) : Flow<List<Expense>>{
        return expenseDao.getExpenseByAccountAndPeriod(accountId,dateBegin, dateEnd)
    }

    fun getExpenseByType(expenseType: ExpenseType) : Flow<List<Expense>>{
        return expenseDao.getExpenseByExpenseType(expenseType)
    }

    fun getExpenseByPeriodAndType(expenseType: ExpenseType,dateBegin: Date,dateEnd: Date) : Flow<List<Expense>>{
        return expenseDao.getExpenseByPeriodAndType(expenseType, dateBegin, dateEnd)
    }

    fun getExpenseByOwner(owner : String) : Flow<List<Expense>>{
        return expenseDao.getExpenseByOwner(owner)
    }

    fun getExpenseByRecurrence(recurrence: Recurrence) : Flow<List<Expense>> {
        return expenseDao.getExpenseByRecurrence(recurrence)
    }

    fun getExpenseWhenIsChecked() : Flow<List<Expense>>{
        return expenseDao.getExpenseWhenIsChecked()
    }

    fun getExpenseByPeriodWhenIsChecked(dateBegin: Date, dateEnd: Date) : Flow<List<Expense>> {
        return expenseDao.getExpenseByPeriodWhenIsChecked(dateBegin, dateEnd)
    }

    fun getExpenseIfNeedPaidBack() : Flow<List<Expense>>{
        return expenseDao.getExpenseIfNeedPaidBack()
    }

    fun getExpensePaidBack() : Flow<List<Expense>> {
        return expenseDao.getExpensePaidBack()
    }

    fun getAllIncomes() : Flow<List<Expense>>{
        return expenseDao.getAllIncomes()
    }

    fun getIncomeByPeriod(dateBegin: Date,dateEnd: Date) : Flow<List<Expense>>{
        return expenseDao.getIncomeByPeriod(dateBegin, dateEnd)
    }

    suspend fun insertExpense(expense: Expense){
        expenseDao.insertExpense(expense)
    }

    suspend fun insertAllExpenses(expenses : List<Expense>){
        expenseDao.insertAllExpenses(expenses)
    }

    suspend fun deleteExpenseById(id: Long){
        expenseDao.deleteExpenseById(id)
    }

    suspend fun deleteExpenseByAccountId(accountId: Long){
        expenseDao.deleteExpenseByAccountId(accountId)
    }

    suspend fun deleteExpenseByPeriod(dateBegin: Date, dateEnd: Date){
        expenseDao.deleteExpenseByPeriod(dateBegin, dateEnd)
    }
}