package com.example.gippes.isaacfastwiki

import android.content.AsyncTaskLoader
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.util.SparseArray
import com.google.gson.GsonBuilder
import java.io.IOException
import java.io.InputStream

/**
 *
 * Created by gippes on 18.02.18.
 */


class ItemsLoader(context: Context) : AsyncTaskLoader<SparseArray<Item>>(context) {
    private val assetManager: AssetManager = context.assets
    private var items: SparseArray<Item> = SparseArray()
    val database: SQLiteDatabase = DBHelper(context).writableDatabase

    companion object {
        const val IMAGES_PATH = "file:///android_asset/images/"
    }

    init {
        forceLoad()
    }

    override fun loadInBackground(): SparseArray<Item> {
        database.query(TABLE_ITEMS, null, null, null, null, null, null).use {
            loadItemsFromDatabase(it)
        }
        if (items.size() == 0) saveItemsToDataBase()
        return items
    }

    private fun loadItemsFromDatabase(cursor: Cursor) {
        items = SparseArray(cursor.count)
        if (cursor.moveToFirst()) {
            val indexId = cursor.getColumnIndex(KEY_ID)
            val indexTitle = cursor.getColumnIndex(KEY_TITLE)
            val indexGameId = cursor.getColumnIndex(KEY_GAME_ID)
            val indexMessage = cursor.getColumnIndex(KEY_MESSAGE)
            val indexItemType = cursor.getColumnIndex(KEY_ITEM_TYPE)
            val indexBuffType = cursor.getColumnIndex(KEY_BUFF_TYPE)
            val indexFInd = cursor.getColumnIndex(KEY_FIND)
            val indexDescription = cursor.getColumnIndex(KEY_DESCRIPTION)
            val indexUnlockCond = cursor.getColumnIndex(KEY_UNLOCK_CONDITION)
            val indexTags = cursor.getColumnIndex(KEY_TAGS)
            val indexImageName = cursor.getColumnIndex(KEY_IMAGE_NAME)
            val begin = System.currentTimeMillis()
            do {
                val id = cursor.getInt(indexId)
                items.put(id, Item(
                        id = cursor.getInt(indexId),
                        title = cursor.getString(indexTitle),
                        gameItemId = cursor.getString(indexGameId),
                        message = cursor.getString(indexMessage),
                        itemType = cursor.getString(indexItemType),
                        buffType = cursor.getString(indexBuffType),
                        whereToFind = cursor.getString(indexFInd),
                        description = cursor.getString(indexDescription),
                        unlockCond = cursor.getString(indexUnlockCond),
                        tags = cursor.getString(indexTags),
                        imageName = cursor.getString(indexImageName)
                ))
            } while (cursor.moveToNext())
            Log.i(LOG_TAG, "Loading time items from db - ${System.currentTimeMillis() - begin}ms")
        }
    }

    private fun saveItemsToDataBase() {
        items = buildItems()
        val begin = System.currentTimeMillis()
//        val statement = database.compileStatement("insert into $TABLE_ITEMS values(?,?,?,?,?,?,?,?,?,?,?);")
        database.beginTransaction()
        try {
            for (i in 0 until items.size()) {
                val item = items.valueAt(i)
//                statement.clearBindings()
//                statement.bindLong(1, item.value.toLong())
//                statement.bindString(2, item.title)
//                statement.bindString(3, item.gameItemId)
//                statement.bindString(4, item.message)
//                statement.bindString(5, item.itemType)
//                statement.bindString(6, item.buffType)
//                statement.bindString(7, item.whereToFind)
//                statement.bindString(8, item.description)
//                statement.bindString(9, item.unlockCond)
//                statement.bindString(10, item.tags)
//                statement.bindString(11, item.imageName)
//                statement.execute()
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

data class Item(var id: Int, var title: String, var gameItemId: String, var message: String, var itemType: String,
                var buffType: String, var whereToFind: String, var description: String, var unlockCond: String?, var tags: String, var imageName: String)
