package cn.edu.zjut.urlcheck.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.edu.zjut.urlcheck.ui.theme.URLCheckTheme
import cn.edu.zjut.urlcheck.utils.DialogUtil
import cn.edu.zjut.urlcheck.utils.LogUtil
import cn.edu.zjut.urlcheck.utils.NetworkCallbackImpl
import cn.edu.zjut.urlcheck.utils.RequestUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference


class MainActivity : ComponentActivity() {
    private val resHandler = ResHandler(WeakReference(this))

    private class ResHandler(val wrActivity: WeakReference<MainActivity>) :
        Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            wrActivity.get()?.run {
                when (msg.what) {
                    0 -> {
                        DialogUtil.createNormalDialog(this, "请求结果", msg.obj as String).show()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            URLCheckTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainLayout()
                }
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


    private fun sendRequest(text: String) {
        Thread(Runnable {
            val call: Call<ResponseBody> = RequestUtil.service.getQRCode(text)
            call.enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val s: String = response.body()!!.string()
                    val message = Message()
                    message.obj = s
                    message.what = 0
                    resHandler.sendMessage(message)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    LogUtil.logInfo(t.toString())
                }
            }
            )
        }).start()

    }

    @Composable
    fun MainLayout() {
        Column(//外边距8dp，内边距15dp
            modifier = Modifier
                .padding(18.dp)
                .border(2.dp, Color.Red, shape = RoundedCornerShape(20.dp))//红色圆角边框
                .padding(15.dp)
        ) {
            var text by remember { mutableStateOf("") }
            val focusManager = LocalFocusManager.current
            TextField(
                value = text,
                onValueChange = {
                    LogUtil.logInfo(it)
                    text = it
                },
                label = {
                    Text("学号")
                },
                placeholder = {
                    Text("请输入学号")
                },
                keyboardActions = KeyboardActions(onDone = {
                    sendRequest(text = text)
                    focusManager.clearFocus()
                }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Button(
                onClick = {
                    sendRequest(text = text)
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "提交",
                )
            }


        }

    }

}






