package com.benoitlefevre.monbudget.injection

import androidx.room.Room
import com.benoitlefevre.monbudget.database.db.BudgetDatabase
import com.benoitlefevre.monbudget.database.repository.AccountRepository
import com.benoitlefevre.monbudget.database.repository.CurrentAmountRepository
import com.benoitlefevre.monbudget.database.repository.ExpenseRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            BudgetDatabase::class.java,
            "budget_db"
        )
            .build()
    }

    single {
        AccountRepository(
            get<BudgetDatabase>().accountDao(),
        )
    }

    single {
        CurrentAmountRepository(
            get<BudgetDatabase>().currentAmountDao()
        )
    }

    single {
        ExpenseRepository(
            get<BudgetDatabase>().expenseDao()
        )
    }
}