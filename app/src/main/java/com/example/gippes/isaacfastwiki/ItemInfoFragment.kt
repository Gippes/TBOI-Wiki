package com.example.gippes.isaacfastwiki

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Igor Goryunov on 19.02.18.
 */
class ItemInfoFragment : Fragment() {
    companion object {
        const val TAG = "item_info_fragment"
    }

    var items : SparseArray<Item> = SparseArray()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewGroup = inflater?.inflate(R.layout.view_item_info, container, false) as ViewGroup
        val pos = arguments.getInt("position")
        items = (activity as MainActivity).items
        if(items.size() != 0) {
            (viewGroup.findViewById<TextView>(R.id.description_item))?.text = items.get(pos)?.description?.replace("•", "\n\n•")
            (viewGroup.findViewById<TextView>(R.id.title))?.text = items.get(pos)?.title
            (viewGroup.findViewById<TextView>(R.id.message))?.text = items.get(pos)?.message
            (viewGroup.findViewById<ImageView>(R.id.image_item))?.setImageDrawable(Drawable.createFromStream(activity.assets.open("images/${items[pos]?.imageName}"), null))
        }
        return viewGroup
    }
}