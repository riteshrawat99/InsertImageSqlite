package com.pupup.insertimagesqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyHelper(context : Context) : SQLiteOpenHelper(context,"Student",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IMAGES(_ID INTEGER PRIMARY KEY AUTOINCREMENT,image blob)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}