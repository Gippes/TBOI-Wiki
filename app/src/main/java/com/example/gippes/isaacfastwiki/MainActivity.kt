package com.example.gippes.isaacfastwiki

import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ListView
import android.widget.RelativeLayout
import com.example.gippes.isaacfastwiki.ViewType.GRID
import com.example.gippes.isaacfastwiki.ViewType.LIST

class MainActivity : FragmentActivity() {
    lateinit var mainLayout: RelativeLayout
    lateinit var config: Configuration
    lateinit var items: SparseArray<Item>
    lateinit var itemInfoFragment: ItemInfoFragment
    lateinit var itemsFragment: ItemsFragment

    var gridView: GridView? = null
    var listView: ListView? = null
    val onItemClickListener = OnItemClickListener({ _, _, pos, _ ->
        intent = Intent("android.intent.action.isaacItem")
        intent.putExtra("Title", items[pos].message)
        intent.putExtra("Description", items.get(pos).description)
        intent.putExtra("ImageName", items[pos].imageName)
        startActivity(intent)
    })

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        gridView?.let { outState?.putInt("grid_vertical_position", it.verticalScrollbarPosition) }
        listView?.let { outState?.putInt("list_vertical_position", it.verticalScrollbarPosition) }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        gridView?.let { it.verticalScrollbarPosition = savedInstanceState?.getInt("grid_vertical_position") ?: 0 }
        listView?.let { it.verticalScrollbarPosition = savedInstanceState?.getInt("list_vertical_position") ?: 0 }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemsFragment = ItemsFragment()
        itemInfoFragment = ItemInfoFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_activity_layout, itemsFragment).commit()

//        loaderManager.initLoader(1, Bundle.EMPTY, ItemsLoaderCallbacks())
        config = Configuration()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        when (config.viewType) {
            GRID -> menu.findItem(R.id.menuShowGrid).isChecked = true
            LIST -> menu.findItem(R.id.menuShowList).isChecked = true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuShowGrid -> {
                mainLayout.removeView(listView)
                addGridViewToViewGroup(mainLayout)
                config.viewType = GRID
                item.isChecked = true
            }
            R.id.menuShowList -> {
                mainLayout.removeView(gridView)
                addListViewToViewGroup(mainLayout)
                config.viewType = LIST
                item.isChecked = true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addListViewToViewGroup(viewGroup: ViewGroup) {
        if (listView == null) {
            val parent = layoutInflater.inflate(R.layout.view_list_items, viewGroup, false) as ViewGroup
            listView = parent.findViewById(R.id.list_items)
            listView?.adapter = ListAdapter(this, items)
            listView?.onItemClickListener = onItemClickListener
            parent.removeView(listView)
            viewGroup.addView(listView)
        } else if (listView?.parent != viewGroup) {
            viewGroup.addView(listView)
        }
    }

    private fun addGridViewToViewGroup(viewGroup: ViewGroup) {
        if (gridView == null) {
            val parent = layoutInflater.inflate(R.layout.view_grid_items, viewGroup, false) as ViewGroup
            gridView = parent.findViewById(R.id.grid_items)
            gridView?.adapter = GridAdapter(this, items)
            gridView?.onItemClickListener = onItemClickListener
            parent.removeView(gridView)
            viewGroup.addView(gridView)
        } else if (gridView?.parent != viewGroup) {
            viewGroup.addView(viewGroup)
        }
    }

    inner class ItemsLoaderCallbacks : LoaderManager.LoaderCallbacks<SparseArray<Item>> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<SparseArray<Item>> = ItemsLoader(this@MainActivity)

        override fun onLoadFinished(loader: Loader<SparseArray<Item>>?, data: SparseArray<Item>?) {
            items = data!!
            when (config.viewType) {
                GRID -> addGridViewToViewGroup(mainLayout)
                LIST -> addListViewToViewGroup(mainLayout)
            }
        }

        override fun onLoaderReset(loader: Loader<SparseArray<Item>>?) {}

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
