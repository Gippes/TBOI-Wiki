package com.example.gippes.isaacfastwiki

import android.database.Cursor
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Igor Goryunov on 12.03.18.
 */

class PageFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var itemsView: RecyclerView? = null
    private var images = arrayListOf<Drawable>()
    var sqlQuery = "select image_name from items order by _id asc"
    var onClickListener: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (itemsView == null) {
            itemsView = RecyclerView(activity)
            itemsView!!.layoutManager = GridLayoutManager(activity, 6)
            itemsView!!.setHasFixedSize(true)
            loaderManager.initLoader(0, Bundle.EMPTY, this)
            retainInstance = true
        }
        return itemsView!!
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = PageDataLoader(activity, sqlQuery)

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        if (cursor != null && itemsView!!.adapter == null) {
            if (cursor.moveToFirst()) {
                val begin = System.currentTimeMillis()
                val index = cursor.getColumnIndex(KEY_IMAGE_NAME)
                do {
                    images.add(Drawable.createFromStream(activity.assets.open("images/${cursor.getString(index)}"), null))
                } while (cursor.moveToNext())
                Log.i(LOG_TAG, "Loading time items from db - ${System.currentTimeMillis() - begin}ms")
                itemsView!!.adapter = ItemsListAdapter(activity, images, onClickListener)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {}
}