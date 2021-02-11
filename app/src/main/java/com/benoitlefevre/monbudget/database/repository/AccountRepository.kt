package com.benoitlefevre.monbudget.database.repository

import com.benoitlefevre.monbudget.database.dao.AccountDao
import com.benoitlefevre.monbudget.database.models.Account
import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountDao: AccountDao) {

    fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts()
    }

    fun getAccountByName(accountName: String): Flow<Account> {
        return accountDao.getAccountByName(accountName)
    }

    fun getAccountById(accountId: Long): Flow<Account> {
        return accountDao.getAccountById(accountId)
    }

    fun getCurrentAccount(): Flow<Account> {
        return accountDao.getCurrentAccount()
    }

    suspend fun insertAccount(account: Account): Long {
        return accountDao.insertAccount(account)
    }

    suspend fun deleteAccount(accountId: Long) {
        accountDao.deleteAccount(accountId)
    }
}