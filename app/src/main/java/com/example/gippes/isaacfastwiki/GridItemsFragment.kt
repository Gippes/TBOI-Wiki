package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
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

    lateinit var gridView: GridView

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("key", gridView.onSaveInstanceState())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = inflater!!.inflate(R.layout.view_grid_items, container, false)
        gridView = parent.findViewById(R.id.grid_items) as GridView
        gridView.adapter = GridAdapter(activity, (activity as MainActivity).items)
        savedInstanceState?.let { gridView.onRestoreInstanceState(savedInstanceState.getParcelable("key")) }
        gridView.onItemClickListener = (activity as MainActivity).onItemClickListener
        return parent
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_TAG, "fragment destroyed")
    }
}