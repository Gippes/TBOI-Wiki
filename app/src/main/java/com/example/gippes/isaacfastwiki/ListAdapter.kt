package com.example.gippes.isaacfastwiki

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 *
 * Created by Igor Goryunov on 21.02.18.
 */

class ListAdapter(private val context: Context, val items: SparseArray<Item>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.view_item, parent,false)
        }
        view?.findViewById<ImageView>(R.id.image)?.setImageDrawable(Drawable.createFromStream(context.assets.open("images/${items[position].imageName}"), items[position].imageName))
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
