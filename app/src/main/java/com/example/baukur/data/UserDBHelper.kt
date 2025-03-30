package com.example.baukur.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "userLocal.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "usersSpending"
        private const val COLUMN_USER_NAME = "email"
        private const val COLUMN_SPENDING = "spending"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_USER_NAME STRING PRIMARY KEY,
                $COLUMN_SPENDING INTEGER
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertOrUpdateUserData(email: String?, spending: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USER_NAME, email)
        contentValues.put(COLUMN_SPENDING, spending)

        val result = db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        return result != -1L
    }

    fun getUserSpending(userId: String): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_SPENDING FROM $TABLE_NAME WHERE $COLUMN_USER_NAME = ?", arrayOf(userId))
        return if (cursor.moveToFirst()) {
            val data = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SPENDING))
            cursor.close()
            data
        } else {
            cursor.close()
            null
        }
    }
}
