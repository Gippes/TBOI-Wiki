package com.example.gippes.isaacfastwiki

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Igor Goryunov on 01.03.18.
 */

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    val fragments = arrayListOf<Fragment>()
    val titles = arrayListOf<String>()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence = titles[position]

    override fun getCount(): Int = fragments.size

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
}

class ItemsListAdapter(private val context: Context,
                       val data: ArrayList<IdAndImage>,
                       var clickListener: View.OnClickListener? = null)
    : RecyclerView.Adapter<ItemsListAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        Log.d(LOG_TAG, "create view holder")
        return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.view_grid_element, parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        Log.d(LOG_TAG, "bind view holder - $position")
        holder?.apply {
            image.setImageDrawable(data[position].image)
            itemView.tag = data[position].id
            itemView.setOnClickListener(clickListener)
        }
    }

    class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView!!.findViewById(R.id.image)!!
    }
}

class IdAndImage(val id: Int, val image: Drawable) {
    operator fun component1(): Int = id
    operator fun component2(): Drawable = image
}

class ListAdapter(private val context: Context, val items: SparseArray<Item>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.view_list_element, parent, false)
        }
        view?.findViewById<ImageView>(R.id.image)?.setImageDrawable(Drawable.createFromStream(context.assets.open("data/${items[position].imageName}"), items[position].imageName))
        view?.findViewById<TextView>(R.id.title)?.text = items[position].title
        view?.findViewById<TextView>(R.id.message)?.text = items[position].message
        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getCount(): Int {
        return items.size()
    }
}