package com.benoitlefevre.monbudget.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.*

class DateConverterTest {

    private lateinit var converter: DateConverter
    private val longDate: Long = 1580490706

    @Before
    fun setUp() {
        converter = DateConverter()
    }

    @Test
    fun fromTimestamp_success_correctDateReturned() {
        val returnedDate = converter.fromTimestamp(longDate)
        val expectedDate = Date(longDate)
        assertEquals(expectedDate, returnedDate)
    }

    @Test
    fun fromTimestamp_failure_nullReturned() {
        val returnedDate = converter.fromTimestamp(null)
        assertNull(returnedDate)
    }

    @Test
    fun dateToTimestamp_success_correctLongReturned() {
        val expectedDate = Date(longDate)
        val returnedLong = converter.dateToTimestamp(expectedDate)
        assertEquals(longDate, returnedLong)
    }

    @Test
    fun dateToTimestamp_failure_nullReturned() {
        val returnedLong = converter.dateToTimestamp(null)
        assertNull(returnedLong)
    }
}