package com.example.gippes.isaacfastwiki

import android.support.test.InstrumentationRegistry
import android.support.test.filters.MediumTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.example.gippes.isaacfastwiki.ui.MainActivity
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@MediumTest
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.example.gippes.isaacfastwiki", appContext.packageName)
    }
}
