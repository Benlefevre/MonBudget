package com.benoitlefevre.monbudget.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["id"],
        childColumns = ["accountId"],
        onDelete = CASCADE
    )]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var accountId: Long,
    var date: Date,
    var amount: Float,
    var beneficiary: String = "",
    var owner: String = "MainUser",
    var recurrence: Recurrence = Recurrence.NONE,
    var isChecked: Boolean = false,
    var isIncome: Boolean = false,
    var needPaidBack: Boolean = false,
    var isPaidBack: Boolean = false,
    var type: ExpenseType = ExpenseType.NONE
)