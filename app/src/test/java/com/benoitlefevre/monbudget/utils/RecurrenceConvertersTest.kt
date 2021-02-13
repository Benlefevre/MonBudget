package com.benoitlefevre.monbudget.utils

import com.benoitlefevre.monbudget.database.models.Recurrence
import com.google.common.truth.Truth.assertThat
import org.junit.Before

import org.junit.Test

class RecurrenceConvertersTest {

    private lateinit var converters: RecurrenceConverters

    @Before
    fun setUp() {
        converters = RecurrenceConverters()
    }

    @Test
    fun toRecurrence_success_correctDataReturned() {
        for (index in 0..10) {
            val recurrence = converters.toRecurrence(index)
            assertThat(recurrence).isEqualTo(Recurrence.values()[index])
        }
    }

    @Test
    fun fromRecurrence_success_corectDataReturned() {
        for (index in 0..10) {
            val ordinal = converters.fromRecurrence(Recurrence.values()[index])
            assertThat(ordinal).isEqualTo(index)
        }
    }
}