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
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import com.example.gippes.isaacfastwiki.R
import com.example.gippes.isaacfastwiki.db.Element
import com.example.gippes.isaacfastwiki.repository.MainViewModel
import com.mancj.materialsearchbar.MaterialSearchBar
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.intentFor

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity() {


    private val mOnClickListener = View.OnClickListener({ view ->
        startActivity(intentFor<ItemInfoActivity>("id" to view.tag))
    })

    private lateinit var mSearchBar: MaterialSearchBar
    private var suggestions: MutableList<Element>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_bar)

        val dataHolder = ViewModelProviders.of(this).get(MainViewModel::class.java).dataHolder

        val viewPager = findViewById<ViewPager>(R.id.viewPager)!!.setup(this,
                PageFragment.create(dataHolder.activeItems, mOnClickListener, getString(R.string.activ)),
                PageFragment.create(dataHolder.passiveItems, mOnClickListener, getString(R.string.passive))
        )


        val tabs = findViewById<TabLayout>(R.id.tabs)!!.apply {
            setupWithViewPager(viewPager)
            viewPager.offscreenPageLimit = 3
        }
        val transparentBackground = findViewById<FrameLayout>(R.id.transparent_bg)!!.apply {
            setOnTouchListener({ _, event ->
                if (event.actionMasked == KeyEvent.ACTION_DOWN) {
                    mSearchBar.disableSearch()
                    return@setOnTouchListener true
                }
                return@setOnTouchListener false
            })
        }
        mSearchBar = findViewById<MaterialSearchBar>(R.id.material_search_bar)!!
                .apply {
                    setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                        override fun onButtonClicked(buttonCode: Int) {
                        }

                        override fun onSearchStateChanged(enabled: Boolean) {
                            if (enabled) transparentBackground.apply {
                                visibility = VISIBLE
                                isClickable = true
                            } else transparentBackground.apply {
                                visibility = GONE
                                isClickable = false
                            }
                        }

                        override fun onSearchConfirmed(text: CharSequence?) {
                            suggestions?.let {
                                if (suggestions!!.isNotEmpty()) {
                                    startActivity(intentFor<ItemInfoActivity>("id" to suggestions!![0].id))
                                }
                            }
                        }

                    })

                    setCustomSuggestionAdapter(SuggestionsDetailAdapter(layoutInflater, mOnClickListener))

                    addTextChangeListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            if (keyword != null && keyword.isNotEmpty()) {
                                dataHolder.findElementsByKeyword("%$keyword%").observe(this@MainActivity, Observer {
                                    if (it != null) {
                                        suggestions = it.toMutableList()
                                        Log.d(LOG_TAG, "display - ${context.displayMetrics.let { "${it.heightPixels / it.density} dp density - ${it.density}" }}")
                                        updateLastSuggestions(suggestions)
                                    }
                                })
                            } else clearSuggestions()
                        }
                    })
                }


        findViewById<BottomNavigationView>(R.id.navigation)!!.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nvgt_items -> {
                    viewPager.update(
                            PageFragment.create(dataHolder.activeItems, mOnClickListener, getString(R.string.activ)),
                            PageFragment.create(dataHolder.passiveItems, mOnClickListener, getString(R.string.passive))
                    )
                    true
                }
                R.id.nvgt_trinkets -> {
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
                else -> false
            }
        }
    }

    private fun ViewPager.setup(context: MainActivity, vararg fragments: PageFragment): ViewPager {
        adapter = ViewPagerAdapter(context.supportFragmentManager).apply {
            fragments.forEach { addFragment(it) }
        }
        return this
    }

    private fun ViewPager.update(vararg fragments: PageFragment) {
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

