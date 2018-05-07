package com.example.gippes.isaacfastwiki.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.*
import com.example.gippes.isaacfastwiki.DaggerTestAppComponent
import com.example.gippes.isaacfastwiki.ui.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*
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

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
    }

    @Test
    fun `check data download`() {
//        dataHolder.activeItems.observe(activity, Observer {
//            assertEquals("Mega Bean", it!![0].title)
//            latch.countDown()
//        })
//        latch.await(2, SECONDS)
        assertEquals("Mega Bean", dataHolder.activeItems.blockingObserve()!![0].title)
//        assertEquals("Triple shot", dataHolder.passiveItems.value!![1].message)
//        assertEquals("items_530.png", dataHolder.trinkets.value!![0].imageName)
//        assertEquals("Pulse Worm", dataHolder.cards.value!![0].title)
    }

    fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val innerObserver = Observer<T> {
            value = it
            latch.countDown()
        }
        observeForever(innerObserver)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}