package cn.edu.zjut.urlcheck.activities


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.edu.zjut.urlcheck.R.id
import cn.edu.zjut.urlcheck.R.layout
import com.google.zxing.Result
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.util.CodeUtils


class QrCodeScanActivity : CaptureActivity() {
    val REQUEST_CODE_PHOTO = 0X02

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_PHOTO -> parsePhoto(data)
            }
        }
    }

    override fun getLayoutId(): Int {
        return layout.my_capture_activity
    }

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
            .setVibrate(true)//震动
            .setPlayBeep(true)//蜂鸣声
    }

    private fun parsePhoto(data: Intent) {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
        /*val source = ImageDecoder.createSource(contentResolver, data.data as Uri)
        val bitmap = ImageDecoder.decodeBitmap(source)*/
        //异步解析
        Thread {
            try {
                val result = CodeUtils.parseCode(bitmap)
                //LogUtil.logInfo(result)
                runOnUiThread {
                    val intent = Intent()
                    intent.putExtra("SCAN_RESULT",result)
                    setResult(Activity.RESULT_OK,intent)//携带扫码结果返回主页面
                    finish()
                }
            } catch (e: NullPointerException) {
                runOnUiThread{
                    AlertDialog.Builder(this)
                        .setMessage("照片中未识别到二维码")
                        .setPositiveButton("确定"){dialog, which -> run {} }
                        .create()
                        .show()
                }

            }


        }.start()

    }

    override fun onScanResultCallback(result: Result?): Boolean {
        return super.onScanResultCallback(result)
    }

    fun onClick(v: View) {
        when (v.id) {
            id.album_button -> {
                val isGranted = checkIsPhotoPermissionGranted()
                if (isGranted) {
                    startPhotoCode()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_PHOTO
                    )
                }

            }
        }
    }

    private fun checkIsPhotoPermissionGranted(): Boolean {
        //检查是否授予了访问外部存储的权限
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startPhotoCode() {
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        ActivityCompat.startActivityForResult(this, pickIntent, REQUEST_CODE_PHOTO, null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {//向用户申请授权的回调
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isGranted = true
        if (requestCode == REQUEST_CODE_PHOTO) {
            for (grant in grantResults) {
                if (grant == PackageManager.PERMISSION_DENIED) {
                    isGranted = false
                    break
                }
            }
        }

        if (isGranted) {
            startPhotoCode()
        } else {
            Toast.makeText(this, "请授权媒体访问权限", Toast.LENGTH_SHORT).show()
        }
    }
}