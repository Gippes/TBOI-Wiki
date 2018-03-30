package com.example.gippes.isaacfastwiki.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.graphics.drawable.Drawable
import com.example.gippes.isaacfastwiki.db.Item.Column.BUFF_TYPE
import com.example.gippes.isaacfastwiki.db.Item.Column.ID
import com.example.gippes.isaacfastwiki.db.Item.Column.IMAGE_NAME
import com.example.gippes.isaacfastwiki.db.Item.Column.ITEM_TYPE
import com.example.gippes.isaacfastwiki.db.Item.Column.MESSAGE
import com.example.gippes.isaacfastwiki.db.Item.Column.TITLE

/**
 * Created by Igor Goryunov on 23.03.18.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<Item>?

    @Query("SELECT $ID,$TITLE,$MESSAGE,$IMAGE_NAME FROM item WHERE $BUFF_TYPE LIKE :buffType ORDER by $ID ASC")
    fun getElementsByBuffType(buffType: String): LiveData<List<Element>>

    @Query("SELECT $ID,$TITLE,$MESSAGE,$IMAGE_NAME FROM item WHERE $ITEM_TYPE LIKE :itemType ORDER by $ID ASC")
    fun getElementsByItemType(itemType: String): LiveData<List<Element>>

    @Query("SELECT * FROM item WHERE $ID = :id")
    fun getItemById(id: Int): LiveData<Item>

    @Insert
    fun insert(vararg items: Item)
}

@Entity
data class Element(@ColumnInfo(name = ID) var id: Int = 0,
                   @ColumnInfo(name = TITLE) var title: String = "",
                   @ColumnInfo(name = MESSAGE) var message: String = "",
                   @ColumnInfo(name = IMAGE_NAME) var imageName: String = "",
                   @Ignore var image: Drawable? = null)

@Entity
data class Item(@PrimaryKey @ColumnInfo(name = ID) var id: Int,
                @ColumnInfo(name = TITLE) var title: String = "",
                @ColumnInfo(name = GAME_ITEM_ID) var gameItemId: Int = 0,
                @ColumnInfo(name = MESSAGE) var message: String = "",
                @ColumnInfo(name = ITEM_TYPE) var itemType: String = "",
                @ColumnInfo(name = BUFF_TYPE) var buffType: String = "",
                @ColumnInfo(name = WHERE_TO_FIND) var whereToFind: String = "",
                @ColumnInfo(name = DESCRIPTION) var description: String = "",
                @ColumnInfo(name = UNLOCK_COND) var unlockCond: String? = null,
                @ColumnInfo(name = TAGS) var tags: String = "",
                @ColumnInfo(name = IMAGE_NAME) var imageName: String = "") {
    companion object Column {
        const val ID = "_id"
        const val TITLE = "title"
        const val GAME_ITEM_ID = "game_item_id"
        const val MESSAGE = "message"
        const val ITEM_TYPE = "item_type"
        const val BUFF_TYPE = "buff_type"
        const val WHERE_TO_FIND = "where_to_find"
        const val DESCRIPTION = "description"
        const val UNLOCK_COND = "unlock_cond"
        const val TAGS = "tags"
        const val IMAGE_NAME = "image_name"
    }
}
