package com.example.gippes.isaacfastwiki

import android.content.Context
import android.database.Cursor
import android.support.v4.content.CursorLoader

/**
 *
 * Created by Igor Goryunov on 12.03.18.
 */

class PageDataLoader(ctx: Context, val sqlQuery: String) : CursorLoader(ctx) {
//    val db: SQLiteDatabase = DBHelper(context).writableDatabase
    init {
        forceLoad()
    }

    override fun loadInBackground(): Cursor? {
//        db.beginTransaction()
//        val cursor: Cursor?
//        try {
//            cursor = db.rawQuery(sqlQuery, arrayOf())
//            db.setTransactionSuccessful()
//        }finally {
//            db.endTransaction()
//        }
//        return cursor
        return null
    }
}
