package com.benoitlefevre.monbudget.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.benoitlefevre.monbudget.database.db.BudgetDatabase
import com.benoitlefevre.monbudget.database.models.Account
import com.benoitlefevre.monbudget.database.models.Expense
import com.benoitlefevre.monbudget.database.models.ExpenseType
import com.benoitlefevre.monbudget.database.models.Recurrence
import com.benoitlefevre.monbudget.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ExpenseDaoTest {

    private lateinit var budgetDb: BudgetDatabase
    private lateinit var accountDao: AccountDao
    private lateinit var expenseDao: ExpenseDao
    private lateinit var date1: Date
    private lateinit var date2: Date
    private lateinit var date3: Date
    private lateinit var expenseList: List<Expense>

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        date1 = Date()
        date2 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -7)
        }.time
        date3 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -15)
        }.time
        budgetDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            BudgetDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        accountDao = budgetDb.accountDao()
        expenseDao = budgetDb.expenseDao()

        runBlocking {
            accountDao.insertAccount(Account(1, "CurrentAccount"))
            accountDao.insertAccount(Account(2, "SecondaryAccount"))
        }
        expenseList = getAllExpenses()
    }

    @After
    fun tearDown() {
        budgetDb.close()
    }

    @Test
    fun insertAndGetExpense_success_correctDataReturned() = runBlocking {
        val expense1 = Expense(1, 1, date1, 30.0F)
        val expense2 = Expense(
            2,
            1,
            date2,
            40.0F,
            "Carrefour",
            "Me",
            Recurrence.WEEKLY,
            type = ExpenseType.FOOD
        )
        val expense3 = Expense(
            3,
            1,
            date2,
            40.0F,
            recurrence = Recurrence.MONTHLY,
            isIncome = true,
            type = ExpenseType.THRIFT
        )
        val expense4 =
            Expense(
                id = 4,
                accountId = 1,
                date = date3,
                amount = 50.0F,
                owner = "Me",
                isChecked = true,
                type = ExpenseType.GASOLINE
            )
        val expense5 = Expense(
            5,
            2,
            date3,
            100.0F,
            beneficiary = "Docteur X",
            owner = "Me",
            needPaidBack = true,
            type = ExpenseType.DOCTOR
        )

        expenseDao.insertExpense(expense1)
        expenseDao.insertExpense(expense2)
        expenseDao.insertExpense(expense3)
        expenseDao.insertExpense(expense4)
        expenseDao.insertExpense(expense5)

        val expenses = budgetDb.expenseDao().getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(5)
        assertThat(expenses).contains(expense1)
        assertThat(expenses).contains(expense2)
        assertThat(expenses).contains(expense3)
        assertThat(expenses).contains(expense4)
        assertThat(expenses).contains(expense5)
    }

    @Test
    fun getExpenseById_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expense = expenseDao.getExpenseById(1).getOrAwaitValue()
        assertThat(expense).isEqualTo(expenseList[0])
    }

    @Test
    fun getExpenseByPeriod_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getExpensesByPeriod(date3, date1).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(5)
        assertThat(expenses).contains(expenseList[0])
        assertThat(expenses).contains(expenseList[1])
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).contains(expenseList[3])

        expenses = expenseDao.getExpensesByPeriod(date2, date2).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[1])
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[0])
        assertThat(expenses).doesNotContain(expenseList[3])
    }

    @Test
    fun getExpenseByAccount_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpenseByAccount(1).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(3)
        assertThat(expenses).contains(expenseList[0])
        assertThat(expenses).contains(expenseList[1])
        assertThat(expenses).contains(expenseList[3])
        assertThat(expenses).doesNotContain(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[4])
    }

    @Test
    fun getExpenseByAccountAndPeriod_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpenseByAccountAndPeriod(1, date3, date2).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[1])
        assertThat(expenses).contains(expenseList[3])
        assertThat(expenses).doesNotContain(expenseList[0])
        assertThat(expenses).doesNotContain(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[4])
    }

    @Test
    fun getExpenseByAmount_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpenseByAmount(30F, 40F).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[0])
        assertThat(expenses).contains(expenseList[1])
    }

    @Test
    fun getExpenseByPeriodAndAmount_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses =
            expenseDao.getExpenseByPeriodAndAmount(90F, 100F, date1, date1).getOrAwaitValue()
        assertThat(expenses).isEmpty()

        expenses = expenseDao.getExpenseByPeriodAndAmount(30F, 50F, date3, date2).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[1])
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[0])
    }

    @Test
    fun getExpenseByType_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getExpenseByExpenseType(ExpenseType.NONE).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[0])

        expenses = expenseDao.getExpenseByExpenseType(ExpenseType.FOOD).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[1])
    }

    @Test
    fun getExpenseByPeriodAndType_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getExpenseByPeriodAndType(ExpenseType.GASOLINE, date2, date1)
            .getOrAwaitValue()
        assertThat(expenses).isEmpty()

        expenses =
            expenseDao.getExpenseByPeriodAndType(ExpenseType.DOCTOR, date2, date1).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[2])
    }

    @Test
    fun getExpenseByOwner_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getExpenseByOwner("MainUser").getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[0])
        assertThat(expenses).contains(expenseList[2])

        expenses = expenseDao.getExpenseByOwner("Me").getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(3)
        assertThat(expenses).contains(expenseList[1])
        assertThat(expenses).contains(expenseList[3])
        assertThat(expenses).contains(expenseList[4])
    }

    @Test
    fun getExpenseByRecurrence_success_correctDateReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpenseByRecurrence(Recurrence.MONTHLY).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[2])
    }

    @Test
    fun getExpenseWhenChecked_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpenseWhenIsChecked().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).contains(expenseList[3])
    }

    @Test
    fun getExpenseByPeriodWhenIsChecked_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getExpenseByPeriodWhenIsChecked(date3, date1).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).contains(expenseList[3])

        expenses = expenseDao.getExpenseByPeriodWhenIsChecked(date2, date1).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[2])
    }

    @Test
    fun getExpenseIfNeedPaidBack_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpenseIfNeedPaidBack().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[4])
    }

    @Test
    fun getExpensePaidBack_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getExpensePaidBack().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[4])
        assertThat(expenses).doesNotContain(expenseList[2])
    }

    @Test
    fun getAllIncomes_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getAllIncomes().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).contains(expenseList[4])
    }

    @Test
    fun getIncomeByPeriod_success_correctDataReturned() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        val expenses = expenseDao.getIncomeByPeriod(date2, date1).getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[4])
    }

    @Test
    fun deleteExpenseById_success_correctDataDeleted() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(5)

        expenseDao.deleteExpenseById(1)

        expenses = expenseDao.getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(4)
        assertThat(expenses).doesNotContain(expenseList[0])
    }

    @Test
    fun deleteExpenseByAccountId_success_correctDataDeleted() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(5)

        expenseDao.deleteExpenseByAccountId(1)

        expenses = expenseDao.getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(2)
        assertThat(expenses).doesNotContain(expenseList[0])
        assertThat(expenses).doesNotContain(expenseList[1])
        assertThat(expenses).doesNotContain(expenseList[3])
    }

    @Test
    fun deleteExpenseByPeriod_success_correctDataDeleted() = runBlocking {
        expenseDao.insertAllExpenses(expenseList)

        var expenses = expenseDao.getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(5)

        expenseDao.deleteExpenseByPeriod(date3, date2)

        expenses = expenseDao.getAllExpenses().getOrAwaitValue()
        assertThat(expenses.size).isEqualTo(1)
        assertThat(expenses).contains(expenseList[0])
        assertThat(expenses).doesNotContain(expenseList[1])
        assertThat(expenses).doesNotContain(expenseList[2])
        assertThat(expenses).doesNotContain(expenseList[3])
        assertThat(expenses).doesNotContain(expenseList[4])
    }

    private fun getAllExpenses(): List<Expense> {
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