package com.benoitlefevre.monbudget.database.repository

import com.benoitlefevre.monbudget.database.dao.CurrentAmountDao
import com.benoitlefevre.monbudget.database.models.CurrentAmount
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class CurrentAmountRepositoryTest {

    @MockK
    private lateinit var currentAmountDao: CurrentAmountDao
    private lateinit var currentAmountRepository: CurrentAmountRepository
    private lateinit var date1: Date
    private lateinit var date2: Date
    private lateinit var date3: Date

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        currentAmountRepository = CurrentAmountRepository(currentAmountDao)
        date1 = Date()
        date2 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -7)
        }.time
        date3 = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -15)
        }.time
    }

    @Test
    fun getAllCurrentAmounts() = runBlocking {
        val currentAmounts = listOf(
            CurrentAmount(1, 1, 10.0F, date1),
            CurrentAmount(2, 1, 20.0F, date2),
            CurrentAmount(3, 1, 30.0F, date3),
        )

        every { currentAmountDao.getAllCurrentAmount() } returns flowOf(currentAmounts)
        val result = currentAmountRepository.getAllCurrentAmounts().first().toList()
        verify { currentAmountDao.getAllCurrentAmount() }
        assertThat(result).isEqualTo(currentAmounts)
    }

    @Test
    fun getCurrentAmountByPeriod() = runBlocking {
        val currentAmounts = listOf(
            CurrentAmount(1, 1, 10.0F, date1),
            CurrentAmount(2, 1, 20.0F, date2),
            CurrentAmount(3, 1, 30.0F, date3),
        )

        every { currentAmountDao.getCurrentAmountByPeriod(any(), any()) } returns flowOf(
            currentAmounts
        )
        val result = currentAmountRepository.getCurrentAmountByPeriod(date3, date1).first().toList()
        verify { currentAmountDao.getCurrentAmountByPeriod(date3, date1) }
        assertThat(result).isEqualTo(currentAmounts)
    }

    @Test
    fun getCurrentAmountByAccountId() = runBlocking {
        val currentAmounts = listOf(
            CurrentAmount(1, 1, 10.0F, date1),
            CurrentAmount(2, 1, 20.0F, date2),
            CurrentAmount(3, 1, 30.0F, date3),
        )

        every { currentAmountDao.getAllCurrentAmountByAccountId(any()) } returns flowOf(
            currentAmounts
        )
        val result = currentAmountRepository.getCurrentAmountByAccountId(1).first().toList()
        verify { currentAmountDao.getAllCurrentAmountByAccountId(1) }
        assertThat(result).isEqualTo(currentAmounts)
    }

    @Test
    fun getCurrentAmountById() = runBlocking {
        val currentAmount = CurrentAmount(1, 1, 10.0F, date1)
        every { currentAmountDao.getCurrentAmountById(any()) } returns flowOf(
            CurrentAmount(
                1,
                1,
                10.0F,
                date1
            )
        )
        val result = currentAmountRepository.getCurrentAmountById(1).first()
        verify { currentAmountDao.getCurrentAmountById(1) }
        assertThat(result).isEqualTo(currentAmount)
    }

    @Test
    fun insertCurrentAmount() = runBlocking {
        val currentAmount = CurrentAmount(1, 1, 10.0F, date1)
        coEvery { currentAmountDao.insertCurrentAmount(any()) } returns Unit
        currentAmountRepository.insertCurrentAmount(currentAmount)
        coVerify { currentAmountDao.insertCurrentAmount(currentAmount) }
    }

    @Test
    fun deleteCurrentAmountByPeriod() = runBlocking {
        coEvery { currentAmountDao.deleteCurrentAmountByPeriod(any(), any()) } returns Unit
        currentAmountRepository.deleteCurrentAmountByPeriod(date3, date1)
        coVerify { currentAmountDao.deleteCurrentAmountByPeriod(date3, date1) }
    }

    @Test
    fun deleteCurrentAmountById() = runBlocking {
        coEvery { currentAmountDao.deleteCurrentAmountById(any()) } returns Unit
        currentAmountRepository.deleteCurrentAmountById(1)
        coVerify { currentAmountDao.deleteCurrentAmountById(1) }
    }

    @Test
    fun deleteCurrentAmountByAccountId() = runBlocking {
        coEvery { currentAmountDao.deleteCurrentAmountByAccountId(any()) } returns Unit
        currentAmountRepository.deleteCurrentAmountByAccountId(1)
        coVerify { currentAmountDao.deleteCurrentAmountByAccountId(1) }
    }
}