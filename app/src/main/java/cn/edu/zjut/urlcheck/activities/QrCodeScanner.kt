package cn.edu.zjut.urlcheck.activities

import android.view.SurfaceView
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.king.zxing.ViewfinderView

@Composable
fun QRCodeScanner(){
    val context = LocalContext.current
    //val hasTorch = remember { context.hasCameraFlash() }
    val surfaceView = remember { SurfaceView(context) }
    val viewfinderView = remember { ViewfinderView(context) }
    var isUsingFlashLight by remember { mutableStateOf(false) }
    Button(
        onClick = {

        },
        modifier = Modifier
            .padding(8.dp)

    ) {
        Text(
            text = "提交",
        )
    }
}