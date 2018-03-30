package com.example.gippes.isaacfastwiki.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.example.gippes.isaacfastwiki.R
import com.example.gippes.isaacfastwiki.repository.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity() {

    val onClickListener = View.OnClickListener({ view ->
        startActivity(intentFor<ItemInfoActivity>("id" to view.tag))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Toolbar>(R.id.toolbar)!!.let {
            setTitle(R.string.items)
            setSupportActionBar(it)
        }

        val dataHolder = ViewModelProviders.of(this).get(MainViewModel::class.java).dataHolder

        val viewPager = findViewById<ViewPager>(R.id.viewPager)!!.setup(this,
                PageFragment.create(dataHolder.activeItems, onClickListener, getString(R.string.activ)),
                PageFragment.create(dataHolder.passiveItems, onClickListener, getString(R.string.passive)),
                PageFragment.create(dataHolder.trinkets, onClickListener, getString(R.string.trinkets)),
                PageFragment.create(dataHolder.cards, onClickListener, getString(R.string.cards))
        )

        val tabs = findViewById<TabLayout>(R.id.tabs)!!.apply {
            setupWithViewPager(viewPager)
            viewPager.offscreenPageLimit = 3
        }

        findViewById<BottomNavigationView>(R.id.navigation)!!.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nvgt_items -> {
                    toolbar.setTitle(R.string.items)
                    viewPager.update(
                            PageFragment.create(dataHolder.activeItems, onClickListener, getString(R.string.activ)),
                            PageFragment.create(dataHolder.passiveItems, onClickListener, getString(R.string.passive))
                    )
                    tabs.animate().alpha(1.0f).setListener(object:AnimatorListenerAdapter(){
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            viewPager.visibility = VISIBLE
                        }
                    })
                        viewPager.animate().alpha(1.0f).setListener(object: AnimatorListenerAdapter(){
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            viewPager.visibility = VISIBLE
                        }
                    })
                    true
                }
                R.id.nvgt_trinkets -> {
                    toolbar.setTitle(R.string.trinkets)
                    viewPager.update(
                            PageFragment.create(dataHolder.trinkets, onClickListener, getString(R.string.trinkets)),
                            PageFragment.create(dataHolder.cards, onClickListener, getString(R.string.cards))
                    )
                    tabs.animate().alpha(0.0f).setListener(object:AnimatorListenerAdapter(){
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            viewPager.visibility = GONE
                        }
                    })
                    viewPager.animate().alpha(0.0f).setListener(object:AnimatorListenerAdapter(){
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            viewPager.visibility = GONE
                        }
                    })
                    true
                }
                R.id.nvgt_monsters -> {
                    toolbar.setTitle(R.string.monsters)
                    true
                }
                R.id.nvgt_characters -> {
                    toolbar.setTitle(R.string.characters)
                    true
                }
                R.id.nvgt_objects -> {
                    toolbar.setTitle(R.string.objects)
                    true
                }
                else -> false
            }
        }

//        supportFragmentManager.updateFragments(viewPager.getPageTags()) {
//            (this as PageFragment).update(onClickListener)
//        }

        supportFragmentManager.findFragmentByTag(ItemInfoActivity.TAG)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
            supportFragmentManager.popBackStack()
        }
    }
}

fun ViewPager.setup(context: MainActivity, vararg fragments: PageFragment): ViewPager {
    adapter = ViewPagerAdapter(context.supportFragmentManager).apply {
        fragments.forEach { addFragment(it) }
    }
    return this
}

fun ViewPager.update(vararg fragments: PageFragment) {
    (adapter as ViewPagerAdapter).apply {
        clear()
        fragments.forEach {
            addFragment(it)
        }
        notifyDataSetChanged()
    }
}

fun <B> FragmentManager.updateFragments(tags: List<String>?, block: Fragment.() -> B) {
    tags?.let {
        it.forEach {
            findFragmentByTag(it)?.apply {
                block()
            }
        }
    }
}

fun ViewPager.getPageTags() = adapter?.let { List(it.count) { "android:switcher:$id:$it" } }

enum class ViewType(val value: Int) { GRID(0), LIST(1) }
