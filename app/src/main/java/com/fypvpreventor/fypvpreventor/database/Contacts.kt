package com.fypvpreventor.VpreventorFYP.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contacts(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    @NonNull @ColumnInfo(name = "name")
    val name : String,
    @NonNull @ColumnInfo(name ="phone_number")
    val phoneNumber : String,
    @ColumnInfo(name = "message")
    val message : String = "Pre-set warning: Suspicious activities are suspected, contact me as soon as possible. kindly remove all of the ~ " +
            "and (#deleteme) to see the whole link."
)
//Typically, SQL column names will have words separated by an underscore,
// as opposed to the lowerCamelCase used by Kotlin properties