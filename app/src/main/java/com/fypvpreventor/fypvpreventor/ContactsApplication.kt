package com.fypvpreventor.VpreventorFYP

import android.app.Application
import com.fypvpreventor.VpreventorFYP.database.AppDatabase

class ContactsApplication : Application() {
    val database : AppDatabase by lazy { AppDatabase.getDatabase(this) }
}