package com.benoitlefevre.monbudget.database.repository

import com.benoitlefevre.monbudget.database.dao.CurrentAmountDao
import com.benoitlefevre.monbudget.database.models.CurrentAmount
import kotlinx.coroutines.flow.Flow
import java.util.*

class CurrentAmountRepository (private val currentAmountDao: CurrentAmountDao) {

    fun getAllCurrentAmounts(): Flow<List<CurrentAmount>>{
        return currentAmountDao.getAllCurrentAmount()
    }

    fun getCurrentAmountByPeriod(dateBegin : Date, dateEnd : Date) : Flow<List<CurrentAmount>>{
        return currentAmountDao.getCurrentAmountByPeriod(dateBegin, dateEnd)
    }

    fun getCurrentAmountByAccountId(accountId : Long) : Flow<List<CurrentAmount>>{
        return currentAmountDao.getAllCurrentAmountByAccountId(accountId)
    }

    fun getCurrentAmountById(id : Long) : Flow<CurrentAmount> {
        return currentAmountDao.getCurrentAmountById(id)
    }

    suspend fun insertCurrentAmount(currentAmount: CurrentAmount){
        currentAmountDao.insertCurrentAmount(currentAmount)
    }

    suspend fun deleteCurrentAmountByPeriod(dateBegin: Date, dateEnd: Date){
        currentAmountDao.deleteCurrentAmountByPeriod(dateBegin, dateEnd)
    }

    suspend fun deleteCurrentAmountById(id : Long) {
        currentAmountDao.deleteCurrentAmountById(id)
    }

    suspend fun deleteCurrentAmountByAccountId(accountId: Long){
        currentAmountDao.deleteCurrentAmountByAccountId(accountId)
    }
}