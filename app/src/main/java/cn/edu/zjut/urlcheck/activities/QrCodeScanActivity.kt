package cn.edu.zjut.urlcheck.activities

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import cn.edu.zjut.urlcheck.utils.LogUtil
import cn.edu.zjut.urlcheck.utils.RequestUtil
import cn.edu.zjut.urlcheck.utils.UrlJudgeUtil
import com.google.zxing.Result
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QrCodeScanActivity : CaptureActivity() {
    private var toast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initCameraScan() {
        super.initCameraScan()

        // 初始化解码配置
        val decodeConfig = DecodeConfig()
        decodeConfig.hints = DecodeFormatManager.QR_CODE_HINTS //只识别二维码

        cameraScan.setOnScanResultCallback(this)
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig))


    }

    override fun onScanResultCallback(result: Result?): Boolean {
        if (result != null) {

            val resultText=result.text
            if(!UrlJudgeUtil().getCompleteUrl(resultText)){
                vibrateIt()//震动
                showToast("该二维码不包含URL链接")

            }else{
                //showToast(resultText)
                val call: Call<ResponseBody> = RequestUtil.service.getQRCode(resultText)
                call.enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val s: String = response.body()!!.string()
                            showToast(s)
                            //finish()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            showToast(t.toString())

                        }
                    }
                )
            }

        }
        return super.onScanResultCallback(result)
    }

    private fun showToast(text: String) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(text)
            toast!!.setDuration(Toast.LENGTH_SHORT)
        }
        toast?.show()
    }

    private fun vibrateIt(){
        val VIBRATE_DURATION = 100L
        (this.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
            VibrationEffect.createOneShot(
                VIBRATE_DURATION,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
}