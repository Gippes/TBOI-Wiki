package com.example.gippes.isaacfastwiki.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.example.gippes.isaacfastwiki.App
import com.example.gippes.isaacfastwiki.AppModule
import com.example.gippes.isaacfastwiki.DaggerAppComponent
import com.example.gippes.isaacfastwiki.R
import com.example.gippes.isaacfastwiki.R.drawable.item
import com.example.gippes.isaacfastwiki.db.ItemDao
import com.example.gippes.isaacfastwiki.repository.AssetUtils
import com.example.gippes.isaacfastwiki.repository.MainViewModel
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 19.02.18.
 */
class ItemInfoActivity : AppCompatActivity() {

    @Inject
    lateinit var assets: AssetUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_info)
        App.appComponent.inject(this)

        findViewById<Toolbar>(R.id.item_info_activity_toolbar)?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        intent.getIntExtra("id", 0).let {
            val itemLifeData = ViewModelProviders.of(this).get(MainViewModel::class.java).dataHolder.getItemById(it)
                    itemLifeData.observe(this, Observer {
                        supportActionBar?.title = it?.title
                        (findViewById<TextView>(R.id.description_item)).text = it?.description?.replace("•", "\n\n•")
                        (findViewById<TextView>(R.id.message))?.text = it?.message
                        it?.imageName?.let { (findViewById<ImageView>(R.id.image_item))?.setImageDrawable(assets.createDrawableByName(it)) }
                    })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


