package com.example.gippes.isaacfastwiki

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by gippes on 18.02.18.
 */

const val DATABASE_NAME = "isaac_db"
const val DATABASE_VERSION = 13
const val TABLE_ITEMS = "items"

const val KEY_ID = "_id"
const val KEY_TITLE = "name"
const val KEY_GAME_ID = "gameItemId"
const val KEY_IMAGE_NAME = "image_name"
const val KEY_MESSAGE = "message"
const val KEY_ITEM_TYPE = "item_type"
const val KEY_BUFF_TYPE = "buff_type"
const val KEY_FIND = "where_to_find"
const val KEY_DESCRIPTION = "description"
const val KEY_UNLOCK_CONDITION = "unlockCond"
const val KEY_TAGS = "tags"

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table " + TABLE_ITEMS + " ("
                + KEY_ID + " integer primary key,"
                + KEY_TITLE + " text,"
                + KEY_GAME_ID + " text,"
                + KEY_IMAGE_NAME + " text,"
                + KEY_MESSAGE + " text,"
                + KEY_ITEM_TYPE + " text,"
                + KEY_BUFF_TYPE + " text,"
                + KEY_FIND + " text,"
                + KEY_DESCRIPTION + " text,"
                + KEY_UNLOCK_CONDITION + " text,"
                + KEY_TAGS + " text);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists " + TABLE_ITEMS)
        onCreate(db)
    }
}



