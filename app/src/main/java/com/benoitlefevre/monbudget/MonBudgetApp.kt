package com.benoitlefevre.monbudget

import android.app.Application
import com.benoitlefevre.monbudget.injection.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MonBudgetApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@MonBudgetApp)
            modules(listOf(appModule))
        }
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}