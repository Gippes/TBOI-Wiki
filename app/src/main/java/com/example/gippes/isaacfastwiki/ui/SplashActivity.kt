package com.example.gippes.isaacfastwiki.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import com.example.gippes.isaacfastwiki.repository.MainViewModel
import org.jetbrains.anko.intentFor
import java.lang.ref.WeakReference

/**
 * Created by Igor Goryunov on 13.03.18.
 */

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProviders.of(this).get(MainViewModel::class.java)
        DBUpdater(WeakReference<SplashActivity>(this)).update()
    }
}

class DBUpdater<T : Activity>(val refActivity: WeakReference<T>) : Handler() {
    fun update() {
        refActivity.get()?.let {
            Thread({
                this.sendEmptyMessage(0)
            }).start()
        }
    }

    override fun handleMessage(msg: Message?) {
        refActivity.get()?.let {
            it.startActivity(it.intentFor<MainActivity>())
            postDelayed({ it.finish() }, 1000)
        }
    }
}
