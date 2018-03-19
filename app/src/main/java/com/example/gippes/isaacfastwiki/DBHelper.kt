package com.example.gippes.isaacfastwiki

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.util.SparseArray
import com.google.gson.GsonBuilder
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.select
import java.io.IOException
import java.io.InputStream

/**
 * Created by gippes on 18.02.18.
 */
const val DATABASE_NAME = "isaac_db"
const val DATABASE_VERSION = 18
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

class DBHelper(context: Context) : ManagedSQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val assetManager: AssetManager = context.assets

    companion object {
        private var instance: DBHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DBHelper {
            if (instance == null) {
                instance = DBHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table " + TABLE_ITEMS + " ("
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
        db.rawQuery("select count(*) from $TABLE_ITEMS", arrayOf()).use {
            if (it.moveToFirst() && it.getInt(0) == 0) {
                saveItemsToDataBase(db)
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_ITEMS")
        onCreate(db)
    }

    private fun saveItemsToDataBase(database: SQLiteDatabase) {
        val items = buildItems()
        val begin = System.currentTimeMillis()
        database.beginTransaction()
        try {
            for (i in 0 until items.size()) {
                val item = items.valueAt(i)
                val cv = ContentValues()
                cv.put(KEY_ID, item.id)
                cv.put(KEY_TITLE, item.title)
                cv.put(KEY_GAME_ID, item.gameItemId)
                cv.put(KEY_MESSAGE, item.message)
                cv.put(KEY_ITEM_TYPE, item.itemType)
                cv.put(KEY_BUFF_TYPE, item.buffType)
                cv.put(KEY_FIND, item.whereToFind)
                cv.put(KEY_DESCRIPTION, item.description)
                cv.put(KEY_UNLOCK_CONDITION, item.unlockCond)
                cv.put(KEY_TAGS, item.tags)
                cv.put(KEY_IMAGE_NAME, item.imageName)
                database.insert(TABLE_ITEMS, null, cv)
                Log.i(LOG_TAG, "item(value - " + item.id + ") inserted to table \"" + TABLE_ITEMS + "\"")
            }
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            Log.i(LOG_TAG, "Saving time items to db - ${System.currentTimeMillis() - begin}ms")
        }
    }

    private fun buildItems(): SparseArray<Item> {
        val itemsMap = SparseArray<Item>()
        var inputStream: InputStream? = null
        try {
            inputStream = assetManager.open("items.json")
            val buffer = ByteArray(inputStream.available())
            if (inputStream.read(buffer) > 0) {
                val images = getImageNames()
                for (item in GsonBuilder().create().fromJson(String(buffer), Array<Item>::class.java)) {
                    if (images.size() > item.id) {
                        item.imageName = images[item.id]
                        itemsMap.put(item.id, item)
                    } else break
                }
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "can't build Items - ", e)
        } finally {
            inputStream?.close()
        }
        return itemsMap
    }

    private fun getImageNames(): SparseArray<String> {
        val res = SparseArray<String>()
        assetManager.list("images")
                .filter { it.contains("items_") }
                .forEach {
                    try {
                        val key = it.split("[_.]".toRegex(), 3)[1].toInt()
                        res.put(key, it)
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "wrong image name - $it Exception - ${e.localizedMessage}")
                    }
                }
        return res
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T> SQLiteDatabase.getValueById(column: String, id: Int): T? = select(TABLE_ITEMS).column(column).whereArgs("_id = {id}", "id" to id).exec {
    moveToFirst()
    when (T::class) {
        String::class -> getString(0)
        Int::class -> getInt(0)
        else -> null
    } as T
}

val Context.database: DBHelper
    get() = DBHelper.getInstance(applicationContext)

data class Item(var id: Int, var title: String, var gameItemId: String, var message: String, var itemType: String,
                var buffType: String, var whereToFind: String, var description: String, var unlockCond: String?, var tags: String, var imageName: String)

