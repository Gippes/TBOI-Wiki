package com.example.gippes.isaacfastwiki.repository

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.SparseArray
import com.example.gippes.isaacfastwiki.db.Item
import com.example.gippes.isaacfastwiki.ui.LOG_TAG
import com.google.gson.GsonBuilder

/**
 * Created by Igor Goryunov on 26.03.18.
 */
class AssetUtils(val assets: AssetManager) {
    fun makeItemsFromFile(jsonObjectFilePath: String): Array<Item> {
        var result: Array<Item>? = null
        assets.open(jsonObjectFilePath).use {
            val buffer = ByteArray(it.available())
            if (it.read(buffer) > 0) {
                result = GsonBuilder().create().fromJson(String(buffer), Array<Item>::class.java).apply {
                    val images = getImageNames()
                    for (item in this) {
                        if (images.size() > item.id) {
                            item.imageName = images[item.id]
                        } else break
                    }
                }
            }
        }
        return result!!
    }

    fun createDrawableByName(name: String): Drawable =
            Drawable.createFromStream(assets.open("images/$name"), null)

    internal fun getImageNames(): SparseArray<String> {
        val res = SparseArray<String>()
        assets.list("images")
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

fun AssetManager.createDrawableByName(name: String): Drawable =
            Drawable.createFromStream(this.open("images/$name"), null)
