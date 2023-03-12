package cn.edu.zjut.urlcheck.utils
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

interface ConnectivityObserver {
    fun observe(): Flow<Status>
    enum class Status { Available, Unavailable, Losing, Lost }
}

class NetWorkManager(private val context: Context): ConnectivityObserver {

    private val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isNetWorkConnected():Boolean{//判断当前网络是否可用(6.0以上版本)
        var isNetUsable = false
        val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        if (networkCapabilities != null) {
            isNetUsable =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return isNetUsable
    }

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            //监听当前网络是否断开
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    launch { send(ConnectivityObserver.Status.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable() {
                    launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager.registerDefaultNetworkCallback(callback)
            } else {
                val request = NetworkRequest.Builder()
                    .addCapability(NET_CAPABILITY_INTERNET)
                    .build()
                manager.registerNetworkCallback(request, callback)
            }
            awaitClose { manager.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
    }
}
