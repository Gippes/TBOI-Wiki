package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ListView

/**
 * Created by Igor Goryunov on 02.03.18.
 */
fun <T> buildFragment(@LayoutRes viewGroupResId: Int, @IdRes viewResId: Int): ItemsFragment<T> where T : AbsListView =
        ItemsFragment<T>().apply {
            parentResId = viewGroupResId
            resId = viewResId
        }

class ItemsFragment<T : AbsListView> : Fragment() {
    @IdRes
    var resId = 0
    @LayoutRes
    var parentResId = 0
    private var parent: View? = null
    private var fragmentView: T? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (parent == null) {
            parent = inflater!!.inflate(parentResId, container, false)
            fragmentView = parent!!.findViewById(resId) as T
            fragmentView!!.onItemClickListener = (activity as MainActivity).onItemClickListener
            fragmentView!!.adapter = when (fragmentView) {
                is GridView -> GridAdapter(activity, (activity as MainActivity).items)
                is ListView -> ListAdapter(activity, (activity as MainActivity).items)
                else -> null
            }
            retainInstance = true
        }
        return parent!!
    }

    fun updateView(clickListener: OnItemClickListener, initAdapter: () -> BaseAdapter) {
        fragmentView?.onItemClickListener = clickListener
        fragmentView?.adapter = initAdapter()
    }

}
