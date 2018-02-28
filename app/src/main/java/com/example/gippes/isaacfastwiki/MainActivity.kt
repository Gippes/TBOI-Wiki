package com.example.gippes.isaacfastwiki

import android.app.LoaderManager
import android.content.Context
import android.content.Loader
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.RelativeLayout
import com.example.gippes.isaacfastwiki.ViewType.GRID
import com.example.gippes.isaacfastwiki.ViewType.LIST

const val LOG_TAG = "gipTag"

class MainActivity : AppCompatActivity() {
    lateinit var mainLayout: RelativeLayout
    lateinit var config: Configuration
    lateinit var itemInfoFragment: ItemInfoFragment
    lateinit var gridItemsFragment: GridItemsFragment
    lateinit var listItemsFragment: ListItemsFragment

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
        gridItemsFragment = GridItemsFragment()
        listItemsFragment = ListItemsFragment()
        itemInfoFragment = ItemInfoFragment()

        loaderManager.initLoader(1, Bundle.EMPTY, ItemsLoaderCallbacks())
        config = Configuration()
        supportFragmentManager.findFragmentByTag(ItemInfoFragment.TAG)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
            supportFragmentManager.popBackStack()
        }
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
                displayGridFragment()
                config.viewType = GRID
                item.isChecked = true
            }
            R.id.menuShowList -> {
                displayListFragment()
                config.viewType = LIST
                item.isChecked = true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayListFragment() {
        if (supportFragmentManager.findFragmentByTag(ListItemsFragment.TAG) == null) {
            if (supportFragmentManager.findFragmentByTag(GridItemsFragment.TAG) == null) {
                supportFragmentManager.beginTransaction().add(R.id.main_activity_layout, listItemsFragment, ListItemsFragment.TAG).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.main_activity_layout, listItemsFragment, ListItemsFragment.TAG).commit()
            }
        }
    }

    private fun displayGridFragment() {
        if (supportFragmentManager.findFragmentByTag(GridItemsFragment.TAG) == null) {
            if (supportFragmentManager.findFragmentByTag(ListItemsFragment.TAG) == null) {
                supportFragmentManager.beginTransaction().add(R.id.main_activity_layout, gridItemsFragment, GridItemsFragment.TAG).commit()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.main_activity_layout, gridItemsFragment, GridItemsFragment.TAG).commit()
            }
        }
    }

    inner class ItemsLoaderCallbacks : LoaderManager.LoaderCallbacks<SparseArray<Item>> {

        override fun onCreateLoader(id: Int, args: Bundle?): Loader<SparseArray<Item>> = ItemsLoader(this@MainActivity)

        override fun onLoadFinished(loader: Loader<SparseArray<Item>>?, data: SparseArray<Item>?) {
            items = data!!
            when (config.viewType) {
                GRID -> displayGridFragment()
                LIST -> displayListFragment()
            }
            Log.i(LOG_TAG, "load finished")
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
