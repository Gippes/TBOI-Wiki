package com.example.gippes.isaacfastwiki

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import org.jetbrains.anko.intentFor
import java.lang.ref.WeakReference

/**
 * Created by Igor Goryunov on 13.03.18.
 */

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DBUpdater(WeakReference<SplashActivity>(this)).update()
    }
}

class DBUpdater<T : Activity>(val refActivity: WeakReference<T>) : Handler() {
    fun update() {
        refActivity.get()?.let {
            Thread({
                DBHelper(it.baseContext).readableDatabase.close()
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
