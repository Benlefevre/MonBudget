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
data class CurrentAmount(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var accountId: Long,
    var amount: Float,
    var date: Date
)
