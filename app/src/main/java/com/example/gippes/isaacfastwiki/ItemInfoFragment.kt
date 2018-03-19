package com.example.gippes.isaacfastwiki

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewGroup = inflater?.inflate(R.layout.view_item_info, container, false) as ViewGroup
        val id = arguments.getInt("id")
        activity.database.readableDatabase.use {
            it.beginTransaction()
            it.getValueById<String>(KEY_DESCRIPTION, id)?.let { (viewGroup.findViewById<TextView>(R.id.description_item))?.text = it.replace("•", "\n\n•") }
            it.getValueById<String>(KEY_TITLE, id)?.let { (viewGroup.findViewById<TextView>(R.id.text))?.text = it}
            it.getValueById<String>(KEY_MESSAGE, id)?.let { (viewGroup.findViewById<TextView>(R.id.message))?.text = it}
            it.getValueById<String>(KEY_IMAGE_NAME, id)?.let { (viewGroup.findViewById<ImageView>(R.id.image_item))?.setImageDrawable(Drawable.createFromStream(activity.assets.open("images/$it"), null))}
            it.setTransactionSuccessful()
            it.endTransaction()
        }
        return viewGroup
    }
}

