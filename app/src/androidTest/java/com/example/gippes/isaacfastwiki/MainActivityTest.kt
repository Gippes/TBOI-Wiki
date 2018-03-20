package com.example.gippes.isaacfastwiki

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Igor Goryunov on 19.03.18.
 */

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun `verify_to_view_pager_create_items_fragment`() {
        onView(withId(R.id.image)).perform(click())
        Thread.sleep(10_000)
    }

    @Test
    fun onCreate() {
    }

}