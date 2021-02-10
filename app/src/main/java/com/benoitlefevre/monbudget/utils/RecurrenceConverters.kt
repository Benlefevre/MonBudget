package com.benoitlefevre.monbudget.utils

import androidx.room.TypeConverter
import com.benoitlefevre.monbudget.database.models.Recurrence

class RecurrenceConverters {

    @TypeConverter
    fun toRecurrence(value: Int) = enumValues<Recurrence>()[value]

    @TypeConverter
    fun fromRecurrence(value: Recurrence) = value.ordinal
}