package cn.edu.zjut.urlcheck.utils

import android.content.Context
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network


class NetworkCallbackImpl(context: Context) : NetworkCallback() {
    val appContext = context

    override fun onLost(network: Network) {
        super.onLost(network)
        DialogUtil.createNormalDialog(appContext,"网络已断开","请检查网络连接设置!").show()
    }

}