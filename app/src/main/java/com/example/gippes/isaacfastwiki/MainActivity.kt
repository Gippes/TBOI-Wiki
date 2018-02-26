package com.example.gippes.isaacfastwiki


import android.app.LoaderManager

import android.content.Context
import android.content.Intent
import android.content.Loader
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
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

class MainActivity : AppCompatActivity() {
    lateinit var mainLayout: RelativeLayout
    lateinit var config: Configuration
    lateinit var items: SparseArray<Item>

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

        mainLayout = findViewById(R.id.main_activity_layout)
        loaderManager.initLoader(1, Bundle.EMPTY, ItemsLoaderCallbacks())
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
                createGridView()
                config.viewType = GRID
                item.isChecked = true
            }
            R.id.menuShowList -> {
                mainLayout.removeView(gridView)
                createListView()
                config.viewType = LIST
                item.isChecked = true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createListView() {
        if (listView == null) {
            val viwGroup = layoutInflater.inflate(R.layout.view_list_items, mainLayout, false) as ViewGroup
            listView = viwGroup.findViewById(R.id.list_items)
            listView?.adapter = ListAdapter(this, items)
            listView?.onItemClickListener = onItemClickListener
            viwGroup.removeView(listView)
            mainLayout.addView(listView)
        } else if (listView?.parent != mainLayout) {
            mainLayout.addView(listView)
        }
    }

    private fun createGridView() {
        if (gridView == null) {
            val viewGroup = layoutInflater.inflate(R.layout.view_grid_items, mainLayout, false) as ViewGroup
            gridView = viewGroup.findViewById(R.id.grid_items)
            gridView?.adapter = GridAdapter(this, items)
            gridView?.onItemClickListener = onItemClickListener
            viewGroup.removeView(gridView)
            mainLayout.addView(gridView)
        } else if (gridView?.parent != mainLayout) {
            mainLayout.addView(gridView)
        }
    }

    inner class ItemsLoaderCallbacks : LoaderManager.LoaderCallbacks<SparseArray<Item>> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<SparseArray<Item>> = ItemsLoader(this@MainActivity)

        override fun onLoadFinished(loader: Loader<SparseArray<Item>>?, data: SparseArray<Item>?) {
            items = data!!
            when (config.viewType) {
                GRID -> createGridView()
                LIST -> createListView()
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
