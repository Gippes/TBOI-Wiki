package com.example.gippes.isaacfastwiki

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Igor Goryunov on 19.02.18.
 */
class ItemInfoFragment : DialogFragment() {
    companion object {
        const val TAG = "item_info_fragment"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewGroup = inflater?.inflate(R.layout.view_item_info, container, false) as ViewGroup
        val pos = arguments.getInt("position")
        val items = (activity as MainActivity).items
        (viewGroup.findViewById<TextView>(R.id.description_item))?.text = items.get(pos).description
        (viewGroup.findViewById<TextView>(R.id.title))?.text = items.get(pos).title
        (viewGroup.findViewById<ImageView>(R.id.image_item))?.setImageDrawable(Drawable.createFromStream(activity.assets.open("images/${items[pos].imageName}"), null))
        return viewGroup
    }
}