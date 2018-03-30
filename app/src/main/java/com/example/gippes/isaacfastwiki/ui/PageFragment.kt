package com.example.gippes.isaacfastwiki.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gippes.isaacfastwiki.db.Element
import com.example.gippes.isaacfastwiki.repository.AssetUtils
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 12.03.18.
 */

class PageFragment : Fragment() {
    private var mElementsView: RecyclerView? = null
    private lateinit var elementsLiveData: LiveData<ArrayList<Element>>
    private lateinit var mOnClickListener: View.OnClickListener
    lateinit var title: String

    companion object {
        fun create(elementsLiveData: LiveData<ArrayList<Element>>, clickListener: View.OnClickListener, title: String = ""): PageFragment {
            val instance = PageFragment()
            instance.elementsLiveData = elementsLiveData
            instance.mOnClickListener = clickListener
            instance.title = title
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mElementsView == null) {
            mElementsView = RecyclerView(activity).apply {
                layoutManager = GridLayoutManager(activity, 6)
                setHasFixedSize(true)
                elementsLiveData.observe(this@PageFragment, Observer {
                    it?.let {
                        adapter = ElementsAdapter(activity!!, it, mOnClickListener)
                        adapter.notifyDataSetChanged()

                    }
                })
            }
            retainInstance = true
        }
        return mElementsView!!
    }

//    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = PageDataLoader(activity!!, mSqlQuery)
//
//    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
//        if (cursor != null && mElementsView!!.adapter == null) {
//            if (cursor.moveToFirst()) {
//                val begin = System.currentTimeMillis()
//                do {
//                    mData.add(IdAndImage(cursor.getInt(0),
//                            Drawable.createFromStream(activity!!.assets.open("images/${cursor.getString(1)}"), null)))
//                } while (cursor.moveToNext())
//                Log.i(LOG_TAG, "Loading time items from db - ${System.currentTimeMillis() - begin}ms")
//                activity?.let { mElementsView!!.adapter = ElementsAdapter(it, mData, mOnClickListener) }
//            }
//        }
//    }
//
//    override fun onLoaderReset(loader: Loader<Cursor>) {}

//    fun update(clickListener: View.OnClickListener) {
//        (mElementsView!!.adapter as ElementsAdapter).apply {
//            this.clickListener = clickListener
//            notifyDataSetChanged()
//        }
//    }
}