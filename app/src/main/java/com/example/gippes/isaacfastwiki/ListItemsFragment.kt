package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
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
    var mainView: View? = null
    lateinit var listView: ListView
    lateinit var items: SparseArray<Item>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (mainView == null) {
            mainView = inflater!!.inflate(R.layout.view_list_items, container, false)
            listView = mainView?.findViewById(R.id.list_items) as ListView
            items = (activity as MainActivity).items
            retainInstance = true
        }
        listView.adapter = ListAdapter(activity, items)
        listView.onItemClickListener = (activity as MainActivity).onItemClickListener
        return mainView!!
    }

    override fun onDestroy() {
        mainView = null
        super.onDestroy()
    }
}

