package com.benoitlefevre.monbudget.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.benoitlefevre.monbudget.database.dao.AccountDao
import com.benoitlefevre.monbudget.database.dao.CurrentAmountDao
import com.benoitlefevre.monbudget.database.dao.ExpenseDao
import com.benoitlefevre.monbudget.database.models.Account
import com.benoitlefevre.monbudget.database.models.CurrentAmount
import com.benoitlefevre.monbudget.database.models.Expense
import com.benoitlefevre.monbudget.utils.DateConverter
import com.benoitlefevre.monbudget.utils.ExpenseTypeConverter
import com.benoitlefevre.monbudget.utils.RecurrenceConverters

@Database(
    entities = [Account::class, CurrentAmount::class, Expense::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, RecurrenceConverters::class, ExpenseTypeConverter::class)
abstract class BudgetDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun accountDao(): AccountDao
    abstract fun currentAmountDao(): CurrentAmountDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetDatabase? = null

        fun getDatabase(context: Context): BudgetDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetDatabase::class.java,
                    "Budget_db"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}