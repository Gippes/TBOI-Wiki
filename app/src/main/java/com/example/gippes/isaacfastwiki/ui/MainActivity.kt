package com.example.gippes.isaacfastwiki.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.View.VISIBLE
import com.example.gippes.isaacfastwiki.R
import com.example.gippes.isaacfastwiki.repository.MainViewModel
import com.mancj.materialsearchbar.MaterialSearchBar
import org.jetbrains.anko.intentFor

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity() {


    private val mOnClickListener = View.OnClickListener({ view ->
        startActivity(intentFor<ItemInfoActivity>("id" to view.tag))
    })


    private lateinit var mSearchBar: MaterialSearchBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataHolder = ViewModelProviders.of(this).get(MainViewModel::class.java).dataHolder

        val viewPager = findViewById<ViewPager>(R.id.viewPager)!!.setup(this,
                PageFragment.create(dataHolder.activeItems, mOnClickListener, getString(R.string.activ)),
                PageFragment.create(dataHolder.passiveItems, mOnClickListener, getString(R.string.passive))
        )

        val tabs = findViewById<TabLayout>(R.id.tabs)!!.apply {
            setupWithViewPager(viewPager)
            viewPager.offscreenPageLimit = 3
        }
        mSearchBar = findViewById<MaterialSearchBar>(R.id.searchBar)!!.apply {
            setPlaceHolder(getString(R.string.items))
            setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onButtonClicked(buttonCode: Int) {
                }

                override fun onSearchStateChanged(enabled: Boolean) {
                }

                override fun onSearchConfirmed(text: CharSequence?) {

                }

            })
            addTextChangeListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (keyword != null && keyword.isNotEmpty()) {
                        dataHolder.findElementsByKeyword("%$keyword%").observe(this@MainActivity, Observer {
                           if(it != null) {
                               val suggestions = mutableListOf<String>()
                               it.forEach({
                                   Log.d(LOG_TAG, it.title)
                                   suggestions.add(it.title)
                               })
                               updateLastSuggestions(suggestions)
                           }
                        })
                    }

                }
            })
        }


        findViewById<BottomNavigationView>(R.id.navigation)!!.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nvgt_items -> {
                    mSearchBar.setPlaceHolder(getString(R.string.items))
                    viewPager.update(
                            PageFragment.create(dataHolder.activeItems, mOnClickListener, getString(R.string.activ)),
                            PageFragment.create(dataHolder.passiveItems, mOnClickListener, getString(R.string.passive))
                    )
                    true
                }
                R.id.nvgt_trinkets -> {
                    mSearchBar.setPlaceHolder(getString(R.string.trinkets))
                    viewPager.update(
                            PageFragment.create(dataHolder.trinkets, mOnClickListener, getString(R.string.trinkets)),
                            PageFragment.create(dataHolder.cards, mOnClickListener, getString(R.string.cards))
                    )
                    tabs.animate().alpha(1.0f).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            super.onAnimationStart(animation)
                            viewPager.visibility = VISIBLE
                        }
                    })
                    viewPager.animate().alpha(1.0f).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            viewPager.visibility = VISIBLE
                        }
                    })
                    true
                }
                R.id.nvgt_monsters -> {
                    mSearchBar.setPlaceHolder(getString(R.string.monsters))
                    true
                }
                R.id.nvgt_characters -> {
                    mSearchBar.setPlaceHolder(getString(R.string.characters))
                    true
                }
                R.id.nvgt_objects -> {
                    mSearchBar.setPlaceHolder(getString(R.string.objects))
                    true
                }
                else -> false
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
}

