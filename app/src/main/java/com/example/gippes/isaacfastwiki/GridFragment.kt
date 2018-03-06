package com.example.gippes.isaacfastwiki

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Igor Goryunov on 05.03.18.
 */
class GridFragment : Fragment() {
    var view: RecyclerView? = null
    val images = arrayListOf<Drawable>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (view == null) {
            view = inflater!!.inflate(R.layout.view_grid_items, container, false).findViewById(R.id.grid_items)
            view!!.layoutManager = GridLayoutManager(activity, 6)
            val items = (activity as MainActivity).items
            if (items.size() != 0) {
                view!!.adapter = loadImagesToAdapter(items)
            }
            retainInstance = true
        }
        return view!!
    }

    fun updateView(items: SparseArray<Item>) {
        view?.adapter = loadImagesToAdapter(items)
    }

    private fun loadImagesToAdapter(items: SparseArray<Item>): GridAdapter {
        for (i in 0 until items.size()) {
            images.add(Drawable.createFromStream(activity.assets.open("images/${items[i].imageName}"), null))
            Log.i(LOG_TAG, "${items[i].imageName} loaded")
        }
        return GridAdapter(activity, View.OnClickListener { }, images)
    }
}
