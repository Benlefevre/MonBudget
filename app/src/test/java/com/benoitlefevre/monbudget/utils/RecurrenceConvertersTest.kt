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
        var recurrence = converters.toRecurrence(0)
        assertThat(recurrence).isEqualTo(Recurrence.DAILY)
        recurrence = converters.toRecurrence(1)
        assertThat(recurrence).isEqualTo(Recurrence.WEEKLY)
        recurrence = converters.toRecurrence(2)
        assertThat(recurrence).isEqualTo(Recurrence.BI_WEEKLY)
        recurrence = converters.toRecurrence(3)
        assertThat(recurrence).isEqualTo(Recurrence.TER_WEEKLY)
        recurrence = converters.toRecurrence(4)
        assertThat(recurrence).isEqualTo(Recurrence.FOUR_WEEKLY)
        recurrence = converters.toRecurrence(5)
        assertThat(recurrence).isEqualTo(Recurrence.MONTHLY)
        recurrence = converters.toRecurrence(6)
        assertThat(recurrence).isEqualTo(Recurrence.BI_MONTHLY)
        recurrence = converters.toRecurrence(7)
        assertThat(recurrence).isEqualTo(Recurrence.TERMLY)
        recurrence = converters.toRecurrence(8)
        assertThat(recurrence).isEqualTo(Recurrence.BIANNUAL)
        recurrence = converters.toRecurrence(9)
        assertThat(recurrence).isEqualTo(Recurrence.ANNUAL)
        recurrence = converters.toRecurrence(10)
        assertThat(recurrence).isEqualTo(Recurrence.NONE)
    }

    @Test
    fun fromRecurrence_success_corectDataReturned() {
        var ordinal = converters.fromRecurrence(Recurrence.DAILY)
        assertThat(ordinal).isEqualTo(0)
        ordinal = converters.fromRecurrence(Recurrence.WEEKLY)
        assertThat(ordinal).isEqualTo(1)
        ordinal = converters.fromRecurrence(Recurrence.BI_WEEKLY)
        assertThat(ordinal).isEqualTo(2)
        ordinal = converters.fromRecurrence(Recurrence.TER_WEEKLY)
        assertThat(ordinal).isEqualTo(3)
        ordinal = converters.fromRecurrence(Recurrence.FOUR_WEEKLY)
        assertThat(ordinal).isEqualTo(4)
        ordinal = converters.fromRecurrence(Recurrence.MONTHLY)
        assertThat(ordinal).isEqualTo(5)
        ordinal = converters.fromRecurrence(Recurrence.BI_MONTHLY)
        assertThat(ordinal).isEqualTo(6)
        ordinal = converters.fromRecurrence(Recurrence.TERMLY)
        assertThat(ordinal).isEqualTo(7)
        ordinal = converters.fromRecurrence(Recurrence.BIANNUAL)
        assertThat(ordinal).isEqualTo(8)
        ordinal = converters.fromRecurrence(Recurrence.ANNUAL)
        assertThat(ordinal).isEqualTo(9)
        ordinal = converters.fromRecurrence(Recurrence.NONE)
        assertThat(ordinal).isEqualTo(10)
    }
}