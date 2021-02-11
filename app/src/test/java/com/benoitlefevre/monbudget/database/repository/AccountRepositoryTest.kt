package com.benoitlefevre.monbudget.database.repository

import com.benoitlefevre.monbudget.database.dao.AccountDao
import com.benoitlefevre.monbudget.database.models.Account
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class AccountRepositoryTest {

    @MockK
    private lateinit var accountDao: AccountDao
    private lateinit var accountRepository: AccountRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        accountRepository = AccountRepository(accountDao)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun getAllAccounts() = runBlocking {
        val accounts = listOf(Account(1), Account(2, "Secondary Account"))
        every { accountDao.getAllAccounts() } returns flowOf(accounts)
        val result = accountRepository.getAllAccounts().first().toList()
        assertThat(result.size).isEqualTo(2)
        assertThat(result).isEqualTo(accounts)
    }

    @Test
    fun getAccountByName() {
        every { accountDao.getAccountByName(any()) } returns flowOf(Account(1))
        accountRepository.getAccountByName("CurrentAccount")
        verify { accountDao.getAccountByName("CurrentAccount") }
    }

    @Test
    fun getAccountById() {
        every { accountDao.getAccountById(any()) } returns flowOf(Account(1))
        accountRepository.getAccountById(1)
        verify { accountDao.getAccountById(1) }
    }

    @Test
    fun getCurrentAccount() {
        every { accountDao.getCurrentAccount() } returns flowOf(Account(1))
        accountRepository.getCurrentAccount()
        verify { accountDao.getCurrentAccount() }
    }

    @Test
    fun insertAccount() = runBlocking {
        val account = Account(1, "CurrentAccount")
        coEvery { accountDao.insertAccount(any()) } returns 1
        val result = accountRepository.insertAccount(account)
        coVerify { accountDao.insertAccount(account) }
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun deleteAccount() = runBlocking {
        val account = Account(1)
        coEvery { accountDao.deleteAccount(any()) } returns Unit
        accountRepository.deleteAccount(account.id)
        coVerify { accountDao.deleteAccount(account.id) }
    }
}