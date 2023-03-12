package cn.edu.zjut.urlcheck.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.edu.zjut.urlcheck.ui.theme.URLCheckTheme
import cn.edu.zjut.urlcheck.ui.theme.UrlCheck


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            URLCheckTheme {
                UrlCheck()
            }
        }
    }
}






