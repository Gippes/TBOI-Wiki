package com.example.gippes.isaacfastwiki.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.example.gippes.isaacfastwiki.DaggerTestAppComponent
import com.example.gippes.isaacfastwiki.ui.MainActivity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 24.03.18.
 */
@RunWith(RobolectricTestRunner::class)
class AppDatabaseTest {

    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var itemDao: ItemDao
    @Inject
    lateinit var activity: MainActivity


    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
    }

    @After
    fun setDown() {
        db.close()
    }

    @Test
    fun get_elements_by_buffType() {
        val buffTypeSearchWord = "Пассивный%"
        var received = false
        itemDao.getElementsByBuffType(buffTypeSearchWord).observe(activity, Observer {
            println(it)
            assertEquals("The Sad Onion", it!![0].title)
            assertEquals("The Inner Eye", it[0].title)
            received = true
        })

        while (received)
            Thread.sleep(100)
    }

    @Test
    fun get_items_by_id() {
        val id = 346

        val item = itemDao.getItemById(id)

        assertEquals("Giga fart!", item.value!!.message)
    }
}