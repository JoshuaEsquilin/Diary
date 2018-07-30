package com.a5.joshe.locationassign


import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteQueryBuilder

import java.util.ArrayList

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  DbHandler handles the production of the SQL database and the operations on it such
//               as reading, saving, updating, and deleting data entries

class DbHandler : SQLiteOpenHelper {

    companion object {

        val DATABASE_NAME = "EntryDB"
        val DATABASE_VERSION = 2
        val TABLE_NAME = "phoneTable"

        val idOfDiaryEntry = "dId"
        val subject = "dSubject"
        val diaryEntry = "dEntry"
        val diaryLat = "dLat"
        val diaryLon = "dLon"
        val diarySource = "dSource"
        val dateOfCreation = "dDate"
    }

    var sqlObject: SQLiteDatabase
    var context: Context? = null


    constructor(context: Context) : super(context, DATABASE_NAME, null, DATABASE_VERSION) {

        this.context = context
        sqlObject = this.writableDatabase
    }

    override fun onCreate(dataB: SQLiteDatabase?) {

        // Establishes the database
        var sql1: String = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" + idOfDiaryEntry + " INTEGER PRIMARY KEY," +
                subject + " VARCHAR(60), " + diaryEntry + " VARCHAR(420), " + diaryLat + " VARCHAR(60), " +
                diaryLon + " VARCHAR(60), " + diarySource + " TEXT, " +  dateOfCreation + " TEXT );"

        dataB!!.execSQL(sql1)
    }

    override fun onUpgrade(dataB: SQLiteDatabase?, old: Int, new: Int) {

        dataB!!.execSQL("Drop table IF EXISTS " + TABLE_NAME)
        onCreate(dataB)

    }

    fun saveData(values: ContentValues): String {

        // Saves the data and checks to see if there was an error between the method exchange
        var report = "error"
        val ID = sqlObject!!.insert(TABLE_NAME, "", values)

        if (ID > 0) {
            report = "ok"
        }
        return report
    }

    fun readData(keyword: String): ArrayList<Entry> {

        var list = ArrayList<Entry>()
        val sqlb = SQLiteQueryBuilder()
        sqlb.tables = TABLE_NAME
        val columnsForSql = arrayOf("dId", "dSubject", "dEntry", "dLat", "dLon", "dSource", "dDate")
        val selectionOfRow = arrayOf(keyword)

        val cursor = sqlb.query(sqlObject, columnsForSql, "dSubject like ?", selectionOfRow, null, null, "dSubject")

        // Goes though the data and assigns it to the respective entry data slot
        if (cursor.moveToFirst()) {

            do {
                val dId = cursor.getInt(cursor.getColumnIndex("dId"))
                val dSubject = cursor.getString(cursor.getColumnIndex("dSubject"))
                val dEntry = cursor.getString(cursor.getColumnIndex("dEntry"))
                val dLat = cursor.getString(cursor.getColumnIndex("dLat"))
                val dLon = cursor.getString(cursor.getColumnIndex("dLon"))
                val dSource = cursor.getString(cursor.getColumnIndex("dSource"))
                val dDate = cursor.getString(cursor.getColumnIndex("dDate"))

                list.add(Entry(dId, dSubject, dEntry, dLat, dLon, dSource, dDate))

            } while (cursor.moveToNext())
        }
        return list
    }

    // Updates the database while checking to make sure there was no error in the method exchange
    fun updateData(values: ContentValues, id: Int): String {

        var wantedArgs = arrayOf(id.toString())

        val i = sqlObject!!.update(TABLE_NAME, values, "dId=?", wantedArgs)

        if (i > 0) {
            return "ok"
        } else {

            return "error"
        }
    }

    // Deletes the chosen data while checking to make sure there was no error in the method exchange
    fun deleteData(id: Int): String {

        var wantedArgs = arrayOf(id.toString())

        val i = sqlObject!!.delete(TABLE_NAME, "dId=?", wantedArgs)

        if (i > 0) {
            return "ok"
        } else {

            return "error"
        }
    }
}