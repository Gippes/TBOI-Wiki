package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView


/**
 * Created by Igor Goryunov on 27.02.18.
 */
class ListItemsFragment : Fragment() {
    companion object {
        const val TAG: String = "ListFragment"
    }
    lateinit var listView: ListView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = inflater!!.inflate(R.layout.view_list_items, container, false) as ViewGroup
        listView = parent.findViewById(R.id.list_items) as ListView
        listView.adapter = ListAdapter(activity, (activity as MainActivity).items)
        listView.onItemClickListener = (activity as MainActivity).onItemClickListener
        return parent
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        listView.let { outState?.putInt("list_vertical_position", it.verticalScrollbarPosition) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.let { it.verticalScrollbarPosition = savedInstanceState?.getInt("list_vertical_position") ?: 0 }
    }
}

