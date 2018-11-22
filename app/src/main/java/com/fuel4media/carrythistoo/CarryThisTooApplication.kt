package com.fuel4media.carrythistoo

import android.app.Application
import android.content.Context

/**
 * Created by shweta on 22/5/18.
 */
class CarryThisTooApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: CarryThisTooApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = CarryThisTooApplication.applicationContext()
    }


}
