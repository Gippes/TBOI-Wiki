package com.example.gippes.isaacfastwiki.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.gippes.isaacfastwiki.R
import org.jetbrains.anko.AnkoLogger

/**
 * Created by Igor Goryunov on 19.02.18.
 */
class ItemInfoActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        const val TAG = "item_info_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_info)

        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        intent.getIntExtra("id", 0).let {
            val id = it
//            database.readableDatabase.use {
//                debug { "database found - $it" }
//                it.beginTransaction()
//                it.getValueById<String>(KEY_TITLE, id)?.let {
////                    findViewById<TextView>(R.id.title)?.text = it
//                    supportActionBar?.setTitle(it)
//                }
//                it.getValueById<String>(KEY_DESCRIPTION, id)?.let { (findViewById<TextView>(R.id.description_item))?.text = it.replace("•", "\n\n•") }
//                it.getValueById<String>(KEY_MESSAGE, id)?.let { (findViewById<TextView>(R.id.message))?.text = it }
//                it.getValueById<String>(KEY_IMAGE_NAME, id)?.let { (findViewById<ImageView>(R.id.image_item))?.setImageDrawable(Drawable.createFromStream(assets.open("images/$it"), null)) }
//                it.setTransactionSuccessful()
//                it.endTransaction()
//            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


