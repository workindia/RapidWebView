package `in`.workindia.rapidwebviewandroidsample

import `in`.workindia.rapidwebview.RapidClient
import android.app.Application

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!RapidClient.isInstanceInitialised()) {
            RapidClient.createInstance(this)
        }
    }
}