package com.benoitlefevre.monbudget.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.benoitlefevre.monbudget.database.db.BudgetDatabase
import com.benoitlefevre.monbudget.database.models.Account
import com.benoitlefevre.monbudget.database.models.CurrentAmount
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
class CurrentAmountDaoTest {

    private lateinit var budgetDb: BudgetDatabase
    private lateinit var currentAmountDao: CurrentAmountDao
    private lateinit var date1: Date
    private lateinit var date2: Date
    private lateinit var date3: Date

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        date1 = Date()
        date2 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -8)
        }.time
        date3 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -10)
        }.time
        budgetDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            BudgetDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        currentAmountDao = budgetDb.currentAmountDao()
        val accountDao = budgetDb.accountDao()
        runBlocking {
            accountDao.insertAccount(Account(1, "CurrentAccount"))
            accountDao.insertAccount(Account(2, "SecondaryAccount"))
        }
    }

    @After
    fun tearDown() {
        budgetDb.close()
    }

    @Test
    fun insertAndGetCurrentAmount_success_correctDataReturned() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 1, 100.0F, date1)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)

        val currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(2)
        assertThat(currentAmounts).contains(currentAmount1)
        assertThat(currentAmounts).contains(currentAmount2)
    }

    @Test
    fun getCurrentAmountByPeriod_success_correctDataReturned() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 1, 100.0F, date2)
        val currentAmount3 = CurrentAmount(3, 1, 100.0F, date3)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)
        currentAmountDao.insertCurrentAmount(currentAmount3)

        var currentAmounts =
            currentAmountDao.getCurrentAmountByPeriod(date3, date1).getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(3)
        assertThat(currentAmounts).contains(currentAmount1)
        assertThat(currentAmounts).contains(currentAmount2)
        assertThat(currentAmounts).contains(currentAmount3)

        currentAmounts = currentAmountDao.getCurrentAmountByPeriod(date2, date1).getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(2)
        assertThat(currentAmounts).contains(currentAmount1)
        assertThat(currentAmounts).contains(currentAmount2)

        currentAmounts = currentAmountDao.getCurrentAmountByPeriod(date3, date2).getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(2)
        assertThat(currentAmounts).contains(currentAmount2)
        assertThat(currentAmounts).contains(currentAmount3)

        currentAmounts = currentAmountDao.getCurrentAmountByPeriod(date1, date1).getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(1)
        assertThat(currentAmounts).contains(currentAmount1)
    }

    @Test
    fun getCurrentAmountByAccountId() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 2, 100.0F, date2)
        val currentAmount3 = CurrentAmount(3, 2, 100.0F, date3)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)
        currentAmountDao.insertCurrentAmount(currentAmount3)

        var currentAmounts = currentAmountDao.getAllCurrentAmountByAccountId(1).getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(1)
        assertThat(currentAmounts).contains(currentAmount1)

        currentAmounts = currentAmountDao.getAllCurrentAmountByAccountId(2).getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(2)
        assertThat(currentAmounts).contains(currentAmount2)
        assertThat(currentAmounts).contains(currentAmount3)
    }

    @Test
    fun getCurrentAmountById_success_correctDataReturned() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 2, 100.0F, date2)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)

        val currentAmount = currentAmountDao.getCurrentAmountById(1).getOrAwaitValue()
        assertThat(currentAmount).isEqualTo(currentAmount1)
    }

    @Test
    fun deleteCurrentAccountByPeriod_success_correctDataDeleted() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 2, 100.0F, date2)
        val currentAmount3 = CurrentAmount(3, 2, 100.0F, date3)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)
        currentAmountDao.insertCurrentAmount(currentAmount3)

        currentAmountDao.deleteCurrentAmountByPeriod(date3,date2)
        var currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(1)
        assertThat(currentAmounts).contains(currentAmount1)

        currentAmountDao.deleteCurrentAmountByPeriod(date1,date1)
        currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts).isEmpty()
    }

    @Test
    fun deleteCurrentAmountById_success_correctDataDeleted() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 2, 100.0F, date2)
        val currentAmount3 = CurrentAmount(3, 2, 100.0F, date3)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)
        currentAmountDao.insertCurrentAmount(currentAmount3)

        var currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(3)

        currentAmountDao.deleteCurrentAmountById(1)

        currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(2)
        assertThat(currentAmounts).doesNotContain(currentAmount1)
    }

    @Test
    fun deleteCurrentAmountByAccountId_success_correctDataDeleted() = runBlocking {
        val currentAmount1 = CurrentAmount(1, 1, 100.0F, date1)
        val currentAmount2 = CurrentAmount(2, 2, 100.0F, date2)
        val currentAmount3 = CurrentAmount(3, 2, 100.0F, date3)

        currentAmountDao.insertCurrentAmount(currentAmount1)
        currentAmountDao.insertCurrentAmount(currentAmount2)
        currentAmountDao.insertCurrentAmount(currentAmount3)

        var currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(3)

        currentAmountDao.deleteCurrentAmountByAccountId(2)

        currentAmounts = currentAmountDao.getAllCurrentAmount().getOrAwaitValue()
        assertThat(currentAmounts.size).isEqualTo(1)
        assertThat(currentAmounts).doesNotContain(currentAmount2)
        assertThat(currentAmounts).doesNotContain(currentAmount3)
    }
}