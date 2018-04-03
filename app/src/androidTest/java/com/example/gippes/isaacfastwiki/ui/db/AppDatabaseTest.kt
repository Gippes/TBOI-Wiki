package com.example.gippes.isaacfastwiki.ui.db

import android.support.test.runner.AndroidJUnit4
import com.example.gippes.isaacfastwiki.db.AppDatabase
import com.example.gippes.isaacfastwiki.db.ItemDao
import com.example.gippes.isaacfastwiki.ui.DaggerAndroidTestAppComponent
import android.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 24.03.18.
 */
@RunWith(AndroidJUnit4::class)
class AndroidDatabaseTest {

    @Inject
    lateinit var db: AppDatabase
    @Inject
    lateinit var itemDao: ItemDao

    @get:Rule
    var instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        DaggerAndroidTestAppComponent.create().inject(this)
    }

    @After
    fun setDown() {
        db.close()
    }

    @Test
    fun get_elements_by_buffTypeTest() {
        val buffTypeSearchWord = "Пассивный%"
        val elements = itemDao.getElementsByBuffType(buffTypeSearchWord)

        assertEquals("The Sad Onion", elements.value!![0].title)
        assertEquals("The Inner Eye", elements.value!![0].title)
    }

    @Test
    fun get_items_by_id() {
        val id = 346

        val item = itemDao.getItemById(id)

        assertEquals("Giga fart!", item.value!!.message)
    }
}