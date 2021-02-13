package com.benoitlefevre.monbudget.utils

import com.benoitlefevre.monbudget.database.models.ExpenseType
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ExpenseTypeConverterTest {

    private lateinit var converter: ExpenseTypeConverter

    @Before
    fun setUp() {
        converter = ExpenseTypeConverter()
    }

    @Test
    fun toExpenseType_success_correctExpenseTypeReturned() {
        for (index in 0..50) {
            val expenseType = converter.toExpenseType(index)
            assertThat(expenseType).isEqualTo(ExpenseType.values()[index])
        }
    }

    @Test
    fun fromExpenseType_success_correctIntReturned() {
        for (index in 0..50) {
            val ordinal = converter.fromExpenseType(ExpenseType.values()[index])
            assertThat(ordinal).isEqualTo(index)
        }
    }
}