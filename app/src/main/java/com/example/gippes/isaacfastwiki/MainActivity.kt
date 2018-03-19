package com.example.gippes.isaacfastwiki

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity() {

    val onClickListener = View.OnClickListener({ view ->
        var itemInfoFragment = supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG)
        if (itemInfoFragment == null) {
            val args = Bundle()
            args.putInt("id", view.tag as Int)
            itemInfoFragment = ItemInfoFragment()
            itemInfoFragment.arguments = args
            if (supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG) == null) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.main_activity_layout, itemInfoFragment, ItemInfoFragment.TAG)
                        .addToBackStack(null)
                        .commit()
            }
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.toolbar)!!.let {
            setTitle(R.string.items)
            setSupportActionBar(it)
        }
        val viewPager = findViewById<ViewPager>(R.id.viewPager)!!.apply { setupViewPager(this) }

        findViewById<TabLayout>(R.id.tabs)!!.apply {
            setupWithViewPager(viewPager)
            viewPager.offscreenPageLimit = 3
        }

        findViewById<BottomNavigationView>(R.id.navigation)!!.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nvgt_items->{
                    toolbar.setTitle(R.string.items)
                    true
                }
                R.id.nvgt_trinkets->{
                    toolbar.setTitle(R.string.trinkets)
                true
                }
                R.id.nvgt_monsters->{
                    toolbar.setTitle(R.string.monsters)
                    true
                }
                R.id.nvgt_characters -> {
                    toolbar.setTitle(R.string.characters)
                    true
                }
                R.id.nvgt_objects->{
                    toolbar.setTitle(R.string.objects)
                    true
                }
                else -> false
            }
        }

        supportFragmentManager.updateFragments(viewPager.getPageTags()) {
            (this as PageFragment).update(onClickListener)
        }

        supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
            supportFragmentManager.popBackStack()
        }
    }

    private fun setupViewPager(pager: ViewPager) {
        pager.adapter = ViewPagerAdapter(supportFragmentManager).apply {
            //            addFragment(PageFragment.create("select _id,image_name from items order by _id asc",onClickListener), getString(R.string.all))
            addFragment(PageFragment.create("select _id,image_name from items where buff_type like '%Активируемый%' order by _id asc", onClickListener), getString(R.string.activ))
            addFragment(PageFragment.create("select _id,image_name from items where buff_type like '%Пассивный%' order by _id asc", onClickListener), getString(R.string.passive))
        }
    }
}

fun <B> FragmentManager.updateFragments(tags: List<String>, block: Fragment.() -> B) {
    tags.forEach {
        findFragmentByTag(it)?.apply {
            block()
        }
    }
}

fun ViewPager.getPageTags() = List(adapter.count) { "android:switcher:$id:$it" }

enum class ViewType(val value: Int) { GRID(0), LIST(1) }
