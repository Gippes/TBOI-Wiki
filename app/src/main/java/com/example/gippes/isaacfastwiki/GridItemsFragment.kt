package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView

/**
 * Created by Igor Goryunov on 27.02.18.
 */
class GridItemsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = inflater!!.inflate(R.layout.view_grid_items, container,false) as ViewGroup
        val gridView = parent.findViewById(R.id.grid_items) as GridView
        gridView.adapter = GridAdapter(activity, (activity as MainActivity).items)
        gridView.onItemClickListener = (activity as MainActivity).onItemClickListener
        return parent
    }
}