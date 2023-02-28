package cn.edu.zjut.urlcheck.utils

import android.util.Log

class LogUtil {
    companion object{
        private val TAG = "MyLog"
        fun logInfo(msg:String){
            Log.i(TAG,msg)
        }
    }
}