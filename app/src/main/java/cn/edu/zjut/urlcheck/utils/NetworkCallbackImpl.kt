package cn.edu.zjut.urlcheck.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.util.Log


class NetworkCallbackImpl(context: Context) : NetworkCallback() {
    val TAG = "MyTest"
    val appContext = context

    /*override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Log.i(TAG,"网络已连接")
    }*/

    override fun onLost(network: Network) {
        super.onLost(network)
        DialogUtil.createNormalDialog(appContext,"网络已断开","请检查网络连接设置!").show()
    }


    /*override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i(TAG,"wifi已经连接")
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i(TAG,"数据流量已经连接")
            } else {
                Log.i(TAG,"其他网络")
            }
        }
    }*/
}