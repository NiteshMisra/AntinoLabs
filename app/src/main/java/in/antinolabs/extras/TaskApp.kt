package `in`.antinolabs.extras

import android.annotation.SuppressLint
import android.app.Application

class TaskApp : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var instance: TaskApp? = null

        fun applicationContext(): TaskApp {
            return instance!!.applicationContext as TaskApp
        }

        fun getGoogleApiHelper() : GoogleApiHelper {
            return GoogleApiHelper(applicationContext())
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this@TaskApp
    }
}