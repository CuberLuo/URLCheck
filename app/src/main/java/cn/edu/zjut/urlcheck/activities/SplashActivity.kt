package cn.edu.zjut.urlcheck.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference
import java.util.*


@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private val mHandler = MyHandler(WeakReference(this))
    //使用WeakReference避免内存泄漏
    private class MyHandler(val wrActivity: WeakReference<SplashActivity>) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            wrActivity.get()?.run {
                when (msg.what) {
                    0 -> goHome()//跳转到MainActivity
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler.postDelayed(Runnable { mHandler.sendEmptyMessage(0) }, 1000)

    }

    private fun goHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()//销毁启动页
    }
}