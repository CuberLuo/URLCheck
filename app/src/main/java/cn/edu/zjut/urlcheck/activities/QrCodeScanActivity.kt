package cn.edu.zjut.urlcheck.activities

import android.os.Bundle
import android.widget.Toast
import cn.edu.zjut.urlcheck.utils.DialogUtil
import com.google.zxing.Result
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer


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
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig));
    }

    override fun onScanResultCallback(result: Result?): Boolean {
        if (result != null) {
            //DialogUtil.createNormalDialog(this,"扫码结果",result.text)
            showToast(result.text)
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
}