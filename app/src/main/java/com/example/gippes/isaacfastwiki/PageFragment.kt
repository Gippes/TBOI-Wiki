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
    private var mItemsView: RecyclerView? = null
    private var mData = arrayListOf<IdAndImage>()
    lateinit var title: String
    private lateinit var mSqlQuery: String
    private lateinit var mOnClickListener: View.OnClickListener

    companion object {
        fun create(sqlQuery: String, clickListener: View.OnClickListener, title: String = ""): PageFragment {
            val instance = PageFragment()
            instance.mSqlQuery = sqlQuery
            instance.mOnClickListener = clickListener
            instance.title = title
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (mItemsView == null) {
            mItemsView = RecyclerView(activity)
            mItemsView!!.layoutManager = GridLayoutManager(activity, 6)
            mItemsView!!.setHasFixedSize(true)
            loaderManager.initLoader(0, Bundle.EMPTY, this)
            retainInstance = true
        }
        return mItemsView!!
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = PageDataLoader(activity, mSqlQuery)

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        if (cursor != null && mItemsView!!.adapter == null) {
            if (cursor.moveToFirst()) {
                val begin = System.currentTimeMillis()
                do {
                    mData.add(IdAndImage(cursor.getInt(0),
                            Drawable.createFromStream(activity.assets.open("images/${cursor.getString(1)}"), null)))
                } while (cursor.moveToNext())
                Log.i(LOG_TAG, "Loading time items from db - ${System.currentTimeMillis() - begin}ms")
                mItemsView!!.adapter = ItemsListAdapter(activity, mData, mOnClickListener)
            }
        }
    }

    fun update(clickListener: View.OnClickListener) {
        (mItemsView!!.adapter as ItemsListAdapter).apply {
            this.clickListener = clickListener
            notifyDataSetChanged()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {}
}