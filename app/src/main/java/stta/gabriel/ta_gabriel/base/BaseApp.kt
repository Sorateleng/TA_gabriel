package stta.gabriel.ta_gabriel.base

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class BaseApp :Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}