package com.benoitlefevre.monbudget.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String = "CurrentAccount",
)