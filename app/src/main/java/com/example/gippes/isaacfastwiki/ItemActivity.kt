package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by Igor Goryunov on 19.02.18.
 */
class ItemActivity : AppCompatActivity() {

    lateinit var description: TextView
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        description = findViewById(R.id.description_item)
        image = findViewById(R.id.image_item)

        title = intent.getStringExtra("Title")
        description.text = intent.getStringExtra("Description").replace("•","\n•")
        Picasso.with(this).load(IMAGES_PATH + intent.getStringExtra("ImageName")).into(image)
    }
}