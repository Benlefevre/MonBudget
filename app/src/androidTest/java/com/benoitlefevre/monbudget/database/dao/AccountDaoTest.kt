package com.benoitlefevre.monbudget.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.benoitlefevre.monbudget.database.db.BudgetDatabase
import com.benoitlefevre.monbudget.database.models.Account
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountDaoTest {

    private lateinit var budgetDb: BudgetDatabase
    private lateinit var accountDao: AccountDao

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        budgetDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            BudgetDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        accountDao = budgetDb.accountDao()
    }

    @After
    fun tearDown() {
        budgetDb.close()
    }

    @Test
    fun insertAndGetAccount_success_correctDataReturned() = runBlocking {
        val account1 = Account(1, "PrimaryAccount")
        val account2 = Account(2, "SecondaryAccount")

        accountDao.insertAccount(account1)
        accountDao.insertAccount(account2)

        val accounts = budgetDb.accountDao().getAllAccounts().first().toList()
        assertThat(accounts).contains(account1)
        assertThat(accounts).contains(account2)
    }

    @Test
    fun getAccountById_success_correctDataReturned() = runBlocking {
        val account1 = Account(1, "PrimaryAccount")
        val account2 = Account(2, "SecondaryAccount")

        accountDao.insertAccount(account1)
        accountDao.insertAccount(account2)

        val account = budgetDb.accountDao().getAccountById(1).first()
        assertThat(account).isEqualTo(account1)
        assertThat(account).isNotEqualTo(account2)
    }

    @Test
    fun getAccountByName_success_correctDataReturned() = runBlocking {
        val account1 = Account(1, "test")
        val account2 = Account(2, "test2")

        accountDao.insertAccount(account1)

        val account = budgetDb.accountDao().getAccountByName("test").first()
        assertThat(account).isEqualTo(account1)
        assertThat(account).isNotEqualTo(account2)
    }

    @Test
    fun getCurrentAccount_success_correctDataReturned() = runBlocking {
        val currentAccount = Account(1, "CurrentAccount")
        val account1 = Account(2, "TestAccount")

        accountDao.insertAccount(account1)
        accountDao.insertAccount(currentAccount)

        val account = budgetDb.accountDao().getCurrentAccount().first()
        assertThat(account).isEqualTo(currentAccount)
        assertThat(account).isNotEqualTo(account1)
    }

    @Test
    fun deleteAccountById_success_correctDataDeleted() = runBlocking {
        val currentAccount = Account(1, "CurrentAccount")
        val account1 = Account(2, "TestAccount")

        accountDao.insertAccount(account1)
        accountDao.insertAccount(currentAccount)

        var accounts = budgetDb.accountDao().getAllAccounts().first().toList()
        assertThat(accounts.size).isEqualTo(2)
        assertThat(accounts).contains(currentAccount)
        assertThat(accounts).contains(account1)

        accountDao.deleteAccount(1)

        accounts = budgetDb.accountDao().getAllAccounts().first().toList()
        assertThat(accounts.size).isEqualTo(1)
        assertThat(accounts).doesNotContain(currentAccount)
    }
}