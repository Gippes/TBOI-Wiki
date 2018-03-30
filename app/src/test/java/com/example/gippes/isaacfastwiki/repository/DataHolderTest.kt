package com.example.gippes.isaacfastwiki.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.example.gippes.isaacfastwiki.DaggerTestAppComponent
import com.example.gippes.isaacfastwiki.ui.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

/**
 * Created by Igor Goryunov on 26.03.18.
 */
@RunWith(RobolectricTestRunner::class)
class DataHolderTest {

    @Inject
    lateinit var dataHolder: DataHolder
    @Inject
    lateinit var activity: MainActivity

    @get:Rule
    var instantExeRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun `check data download`() {
        assertEquals("Mega Bean", dataHolder.activeItems.value!![0].title)
        assertEquals("Triple shot", dataHolder.passiveItems.value!![1].message)
        assertEquals("items_530.png", dataHolder.trinkets.value!![0].imageName)
        assertEquals("Pulse Worm", dataHolder.cards.value!![0].title)
    }

}