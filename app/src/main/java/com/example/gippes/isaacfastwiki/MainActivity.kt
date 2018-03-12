package com.example.gippes.isaacfastwiki

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.example.gippes.isaacfastwiki.ViewType.GRID
import com.example.gippes.isaacfastwiki.ViewType.LIST

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity() {
    val onClickListener = View.OnClickListener({ view ->
        var itemInfoFragment = supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG)
        if (itemInfoFragment == null) {
            val args = Bundle()
            args.putInt("position", view.verticalScrollbarPosition)
            itemInfoFragment = ItemInfoFragment()
            itemInfoFragment.arguments = args
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_activity_layout, itemInfoFragment, ItemInfoFragment.TAG)
                    .addToBackStack(null)
                    .commit()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.toolbar)?.let { setSupportActionBar(it) }
        val viewPager = findViewById<ViewPager>(R.id.viewPager)?.apply { setupViewPager(this) }
        findViewById<TabLayout>(R.id.tabs)?.apply {
            setupWithViewPager(viewPager)
            viewPager?.offscreenPageLimit = 3
        }

        supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
            supportFragmentManager.popBackStack()
        }
    }

    private fun setupViewPager(pager: ViewPager) {
        pager.adapter = ViewPagerAdapter(supportFragmentManager).apply {
            var fragment = GridPageFragment()
            fragment.onClickListener = onClickListener
            addFragment(fragment, getString(R.string.all))

            fragment = GridPageFragment()
            fragment.sqlQuery = "select image_name from items where buff_type like '%Активируемый%' order by _id asc"
            fragment.onClickListener = onClickListener
            addFragment(fragment, getString(R.string.activ))

            fragment = GridPageFragment()
            fragment.sqlQuery = "select image_name from items where buff_type like '%Пассивный%' order by _id asc"
            fragment.onClickListener = onClickListener
            addFragment(fragment, getString(R.string.passive))
        }
    }

    inner class Configuration {
        val preferences = this@MainActivity.getSharedPreferences("isaac_config", Context.MODE_PRIVATE)!!
        var viewType
            set(type) = preferences.edit().putInt("view_type", type.value).apply()
            get() = when (preferences.getInt("view_type", GRID.value)) {
                GRID.value -> GRID
                LIST.value -> LIST
                else -> GRID
            }
    }
}

enum class ViewType(val value: Int) { GRID(0), LIST(1) }
