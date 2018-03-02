package com.example.gippes.isaacfastwiki

import android.app.LoaderManager.LoaderCallbacks
import android.content.Context
import android.content.Loader
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.SparseArray
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ListView
import com.example.gippes.isaacfastwiki.ViewType.GRID
import com.example.gippes.isaacfastwiki.ViewType.LIST

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity(), LoaderCallbacks<SparseArray<Item>> {
    lateinit var mainLayout: CoordinatorLayout
    lateinit var config: Configuration
    lateinit var itemInfoFragment: ItemInfoFragment
    lateinit var gridItemsFragment: ItemsFragment<GridView>
    lateinit var listItemsFragment: ItemsFragment<ListView>

    var items: SparseArray<Item> = SparseArray()

    val onItemClickListener = OnItemClickListener({ _, _, pos, _ ->
        if (supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG) == null) {
            val args = Bundle()
            args.putInt("position", pos)
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

        mainLayout = findViewById(R.id.main_activity_layout)
        gridItemsFragment = buildFragment(R.layout.view_grid_items, R.id.grid_items)
        listItemsFragment = buildFragment(R.layout.view_list_items, R.id.list_items)
        itemInfoFragment = ItemInfoFragment()

        findViewById<Toolbar>(R.id.toolbar)?.let { setSupportActionBar(it) }
        val viewPager = findViewById<ViewPager>(R.id.viewPager)?.apply { setupViewPager(this) }
        findViewById<TabLayout>(R.id.tabs)?.apply {
            setupWithViewPager(viewPager)
            getTabAt(0)?.setIcon(R.drawable.grid_icon)
            getTabAt(1)?.setIcon(R.drawable.list_4)
        }

        loaderManager.initLoader(1, Bundle.EMPTY, this)
        config = Configuration()
        supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
            supportFragmentManager.popBackStack()
        }
    }

    private fun setupViewPager(pager: ViewPager) {
        pager.adapter = ViewPagerAdapter(supportFragmentManager).apply {
            addFragment(gridItemsFragment, getString(R.string.grid))
            addFragment(listItemsFragment, getString(R.string.list))
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<SparseArray<Item>> = ItemsLoader(this)

    override fun onLoadFinished(loader: Loader<SparseArray<Item>>?, data: SparseArray<Item>?) {
        items = data!!
        gridItemsFragment.updateView(onItemClickListener, { GridAdapter(this, items) })
        listItemsFragment.updateView(onItemClickListener, { ListAdapter(this, items) })
    }

    override fun onLoaderReset(loader: Loader<SparseArray<Item>>?) {}

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
