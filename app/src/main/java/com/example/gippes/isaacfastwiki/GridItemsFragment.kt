package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView

/**
 * Created by Igor Goryunov on 27.02.18.
 */
class GridItemsFragment : Fragment() {
    companion object {
        const val TAG = "GridFragment"
    }

    var mainView: View? = null
    lateinit var gridView: GridView
    lateinit var items: SparseArray<Item>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (mainView == null) {
            mainView = inflater!!.inflate(R.layout.view_grid_items, container, false)
            gridView = mainView!!.findViewById(R.id.grid_items)
            items = (activity as MainActivity).items
            retainInstance = true
        }
        gridView.onItemClickListener = (activity as MainActivity).onItemClickListener
        gridView.adapter = GridAdapter(activity, items)
        return mainView!!
    }

    override fun onDestroy() {
        mainView = null
        super.onDestroy()
    }
}