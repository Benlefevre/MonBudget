package com.benoitlefevre.monbudget.utils

import androidx.room.TypeConverter
import com.benoitlefevre.monbudget.database.models.ExpenseType

class ExpenseTypeConverter {

    @TypeConverter
    fun toExpenseType(value: Int) = enumValues<ExpenseType>()[value]

    @TypeConverter
    fun fromExpenseType(value: ExpenseType) = value.ordinal
}