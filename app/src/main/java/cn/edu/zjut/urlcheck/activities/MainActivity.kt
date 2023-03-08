package cn.edu.zjut.urlcheck.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cn.edu.zjut.urlcheck.ui.theme.URLCheckTheme
import cn.edu.zjut.urlcheck.ui.theme.UrlCheck
import cn.edu.zjut.urlcheck.utils.DialogUtil
import cn.edu.zjut.urlcheck.utils.LogUtil
import cn.edu.zjut.urlcheck.utils.NetworkCallbackImpl


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            URLCheckTheme {
                UrlCheck("There is no result")
            }
        }

        if (isNetSystemUsable(this)) {
            Toast.makeText(this, "网络连接正常", Toast.LENGTH_SHORT).show();
        } else {
            DialogUtil.createNormalDialog(this, "网络已断开", "请检查网络连接设置!").show()
        }
        addNetworkListener()


    }

    /*
    * 监听当前网络是否断开
    * */
    private fun addNetworkListener() {
        val networkCallback = NetworkCallbackImpl(this)
        val builder = NetworkRequest.Builder()
        val request = builder.build()
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr?.registerNetworkCallback(request, networkCallback)
    }


    /**
     * 判断当前网络是否可用(6.0以上版本)
     */
    private fun isNetSystemUsable(context: Context): Boolean {
        var isNetUsable = false
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        if (networkCapabilities != null) {
            isNetUsable =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return isNetUsable
    }
}






