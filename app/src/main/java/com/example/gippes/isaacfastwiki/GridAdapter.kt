package com.example.gippes.isaacfastwiki

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP

/**
 * Created by Igor Goryunov on 19.02.18.
 */
class GridAdapter(private val context: Context, private val items: SparseArray<Item>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val imageView : ImageView?
        if (convertView == null) {
            imageView = ImageView(context)
            imageView.layoutParams = ViewGroup.LayoutParams(150, 150)
            imageView.scaleType = CENTER_CROP
            imageView.setPadding(2, 2, 2, 2)
        }else{
           imageView = convertView as ImageView
        }
//        imageView.setImageURI(Uri.parse(IMAGES_PATH + items[position].imageName))
        imageView.setImageDrawable(Drawable.createFromStream(context.assets.open("images/${items[position].imageName}"),null))
//        Picasso.with(context).load(IMAGES_PATH + items[position].imageName).config(Bitmap.Config.RGB_565).noFade().resize(150,150).into(imageView)
        return imageView
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