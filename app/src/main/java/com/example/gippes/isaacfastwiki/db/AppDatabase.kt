package com.example.gippes.isaacfastwiki.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.example.gippes.isaacfastwiki.db.Item.Column.BUFF_TYPE
import com.example.gippes.isaacfastwiki.db.Item.Column.ID
import com.example.gippes.isaacfastwiki.db.Item.Column.IMAGE_NAME
import com.example.gippes.isaacfastwiki.db.Item.Column.ITEM_TYPE
import com.example.gippes.isaacfastwiki.db.Item.Column.MESSAGE
import com.example.gippes.isaacfastwiki.db.Item.Column.TITLE
import java.io.Serializable

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

    @Query("SELECT * FROM item WHERE $ID = 0")
    fun checkTableCreate(): Item?

    @Query("SELECT _id, title, message, image_name FROM item " +
            "WHERE tags LIKE :keyword " +
            "OR title LIKE :keyword " +
            "ORDER BY _id ASC " +
            "LIMIT 10")
    fun findElementsByKeyword(keyword: String): LiveData<List<Element>>

    @Insert
    fun insert(vararg items: Item)
}

@Entity
class Element(@ColumnInfo(name = ID) var id: Int = 0,
              @ColumnInfo(name = TITLE) var title: String = "",
              @ColumnInfo(name = MESSAGE) var message: String = "",
              @ColumnInfo(name = IMAGE_NAME) var imageName: String = "",
              @Ignore var image: Drawable? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            BitmapDrawable(Resources.getSystem(), parcel.readParcelable<Bitmap>(ClassLoader.getSystemClassLoader())))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(imageName)
        image?.let { parcel.writeParcelable((it as BitmapDrawable).bitmap, flags) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Element> {
        override fun createFromParcel(parcel: Parcel): Element {
            return Element(parcel)
        }

        override fun newArray(size: Int): Array<Element?> {
            return arrayOfNulls(size)
        }
    }

}

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
