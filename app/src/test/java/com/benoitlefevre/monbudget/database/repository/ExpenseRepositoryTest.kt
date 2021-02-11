package com.benoitlefevre.monbudget.database.repository

import com.benoitlefevre.monbudget.database.dao.ExpenseDao
import com.benoitlefevre.monbudget.database.models.Expense
import com.benoitlefevre.monbudget.database.models.ExpenseType
import com.benoitlefevre.monbudget.database.models.Recurrence
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class ExpenseRepositoryTest {

    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var date1: Date
    private lateinit var date2: Date
    private lateinit var date3: Date
    private lateinit var expenses: List<Expense>

    @MockK
    private lateinit var expenseDao: ExpenseDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        expenseRepository = ExpenseRepository(expenseDao)
        date1 = Date()
        date2 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -7)
        }.time
        date3 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -15)
        }.time
        expenses = getAllExpensesDemo()
    }

    @Test
    fun getAllExpenses() = runBlocking {
        every { expenseDao.getAllExpenses() } returns flowOf(expenses)
        val result = expenseRepository.getAllExpenses().first().toList()
        verify { expenseDao.getAllExpenses() }
        assertThat(result.size).isEqualTo(5)
        assertThat(result).contains(expenses[0])
        assertThat(result).contains(expenses[1])
        assertThat(result).contains(expenses[2])
        assertThat(result).contains(expenses[3])
        assertThat(result).contains(expenses[4])
    }

    @Test
    fun getExpenseById() = runBlocking {
        every { expenseDao.getExpenseById(any()) } returns flowOf(expenses[0])
        val result = expenseDao.getExpenseById(1).first()
        verify { expenseDao.getExpenseById(1) }
        assertThat(result).isEqualTo(expenses[0])
    }

    @Test
    fun getExpenseByPeriod() = runBlocking {
        every { expenseDao.getExpensesByPeriod(any(), any()) } returns flowOf(expenses)
        val result = expenseRepository.getExpenseByPeriod(date3, date1).first().toList()
        verify { expenseDao.getExpensesByPeriod(date3, date1) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByAccount() = runBlocking {
        every { expenseDao.getExpenseByAccount(any()) } returns flowOf(expenses)
        val result = expenseRepository.getExpenseByAccount(1).first().toList()
        verify { expenseDao.getExpenseByAccount(1) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByPeriodAndAccount() = runBlocking {
        every { expenseDao.getExpenseByAccountAndPeriod(any(), any(), any()) } returns flowOf(
            expenses
        )
        val result =
            expenseRepository.getExpenseByPeriodAndAccount(1, date3, date2).first().toList()
        verify { expenseDao.getExpenseByAccountAndPeriod(1, date3, date2) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByType() = runBlocking {
        every { expenseDao.getExpenseByExpenseType(any()) } returns flowOf(expenses)
        val result = expenseRepository.getExpenseByType(ExpenseType.OTHER_EXPENSE).first().toList()
        verify { expenseDao.getExpenseByExpenseType(ExpenseType.OTHER_EXPENSE) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByPeriodAndType() = runBlocking {
        every { expenseDao.getExpenseByPeriodAndType(any(), any(), any()) } returns flowOf(expenses)
        val result =
            expenseRepository.getExpenseByPeriodAndType(ExpenseType.OTHER_EXPENSE, date3, date1)
                .first().toList()
        verify { expenseDao.getExpenseByPeriodAndType(ExpenseType.OTHER_EXPENSE, date3, date1) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByOwner() = runBlocking {
        every { expenseDao.getExpenseByOwner(any()) } returns flowOf(expenses)
        val result = expenseRepository.getExpenseByOwner("MainUser").first().toList()
        verify { expenseDao.getExpenseByOwner("MainUser") }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByRecurrence() = runBlocking {
        every { expenseDao.getExpenseByRecurrence(any()) } returns flowOf(expenses)
        val result = expenseRepository.getExpenseByRecurrence(Recurrence.WEEKLY).first().toList()
        verify { expenseDao.getExpenseByRecurrence(Recurrence.WEEKLY) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseWhenIsChecked() = runBlocking {
        every { expenseDao.getExpenseWhenIsChecked() } returns flowOf(expenses)
        val result = expenseRepository.getExpenseWhenIsChecked().first().toList()
        verify { expenseDao.getExpenseWhenIsChecked() }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseByPeriodWhenIsChecked() = runBlocking {
        every { expenseDao.getExpenseByPeriodWhenIsChecked(any(), any()) } returns flowOf(expenses)
        val result =
            expenseRepository.getExpenseByPeriodWhenIsChecked(date3, date1).first().toList()
        verify { expenseDao.getExpenseByPeriodWhenIsChecked(date3, date1) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpenseIfNeedPaidBack() = runBlocking {
        every { expenseDao.getExpenseIfNeedPaidBack() } returns flowOf(expenses)
        val result = expenseRepository.getExpenseIfNeedPaidBack().first().toList()
        verify { expenseDao.getExpenseIfNeedPaidBack() }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getExpensePaidBack() = runBlocking {
        every { expenseDao.getExpensePaidBack() } returns flowOf(expenses)
        val result = expenseRepository.getExpensePaidBack().first().toList()
        verify { expenseDao.getExpensePaidBack() }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getAllIncomes() = runBlocking {
        every { expenseDao.getAllIncomes() } returns flowOf(expenses)
        val result = expenseRepository.getAllIncomes().first().toList()
        verify { expenseDao.getAllIncomes() }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun getIncomeByPeriod() = runBlocking {
        every { expenseDao.getIncomeByPeriod(any(), any()) } returns flowOf(expenses)
        val result = expenseRepository.getIncomeByPeriod(date3, date1).first().toList()
        verify { expenseDao.getIncomeByPeriod(date3, date1) }
        assertThat(result).isEqualTo(expenses)
    }

    @Test
    fun insertExpense() = runBlocking {
        coEvery { expenseDao.insertExpense(any()) } returns Unit
        val expense = Expense(
            1, 1, date1, 10.0F
        )
        expenseRepository.insertExpense(expense)
        coVerify { expenseDao.insertExpense(expense) }
    }

    @Test
    fun insertAllExpenses() = runBlocking {
        coEvery { expenseDao.insertAllExpenses(any()) } returns Unit
        expenseRepository.insertAllExpenses(expenses)
        coVerify { expenseDao.insertAllExpenses(expenses) }
    }

    @Test
    fun deleteExpenseById() = runBlocking {
        coEvery { expenseDao.deleteExpenseById(any()) } returns Unit
        expenseRepository.deleteExpenseById(1)
        coVerify { expenseDao.deleteExpenseById(1) }
    }

    @Test
    fun deleteExpenseByAccountId() = runBlocking {
        coEvery { expenseDao.deleteExpenseByAccountId(any()) } returns Unit
        expenseRepository.deleteExpenseByAccountId(1)
        coVerify { expenseDao.deleteExpenseByAccountId(1) }
    }

    @Test
    fun deleteExpenseByPeriod() = runBlocking {
        coEvery { expenseDao.deleteExpenseByPeriod(any(), any()) } returns Unit
        expenseRepository.deleteExpenseByPeriod(date3, date1)
        coVerify { expenseDao.deleteExpenseByPeriod(date3, date1) }
    }

    private fun getAllExpensesDemo(): List<Expense> {
        return listOf(
            Expense(
                1,
                1,
                date1,
                30.0F
            ),
            Expense(
                2,
                1,
                date2,
                40.0F,
                "Carrefour",
                "Me",
                Recurrence.WEEKLY,
                type = ExpenseType.FOOD
            ),
            Expense(
                3,
                2,
                date2,
                50.0F,
                recurrence = Recurrence.MONTHLY,
                isIncome = true,
                isChecked = true,
                needPaidBack = true,
                type = ExpenseType.DOCTOR
            ),
            Expense(
                4,
                1,
                date3,
                60.0F,
                owner = "Me",
                isChecked = true,
                type = ExpenseType.GASOLINE
            ),
            Expense(
                5,
                2,
                date3,
                100.0F,
                beneficiary = "Docteur X",
                owner = "Me",
                isIncome = true,
                needPaidBack = true,
                isPaidBack = true,
                type = ExpenseType.DOCTOR
            )
        )
    }
}