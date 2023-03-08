package cn.edu.zjut.urlcheck.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.zjut.urlcheck.R

@Composable
fun SettingSreen(){
    Box(modifier = Modifier
        .fillMaxSize())
    {
        Column {
            Spacer(modifier = Modifier.size(280.dp))
            ChangeTheme()
            MonitoringPage()
            HelpingPage()
        }
        
    }
}

@Composable
fun ChangeTheme(){
    val checkedState = remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(25.dp),
        ){
            Box(modifier = Modifier.fillMaxWidth(0.3f)){
                val imageModifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                val image: Painter = painterResource(id = R.drawable.icon_theme)
                Image(painter = image,contentDescription = "",imageModifier)
            }
            Text(text="深色模式",
                modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.75f),
                style = TextStyle(
                    fontSize = 19.sp,
                    letterSpacing = (-0.5).sp,
                    color = White,
                    fontWeight = FontWeight.W500
                ))
            Switch(
                modifier=Modifier
                    .align(Alignment.CenterVertically),
                checked = checkedState.value,
                colors= SwitchDefaults.colors(
                    checkedThumbColor= White,          //启用和选中时用于拇指的颜色
                    checkedTrackColor= ButtonBlue,             //启用和选中时用于轨道的颜色
                ),
                onCheckedChange = {
                    checkedState.value = it
                }
            )

        }
    }
}

@Composable
fun MonitoringPage(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(25.dp),
        ){
            Box(modifier = Modifier.fillMaxWidth(0.3f)){
                val imageModifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                val image: Painter = painterResource(id = R.drawable.icon_monitoring)
                Image(painter = image,contentDescription = "",imageModifier)
            }
            Text(text="开启自动监控",
                modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.8f),
                style = TextStyle(
                    color = White,
                    fontSize = 19.sp,
                    letterSpacing = (-0.5).sp,
                    fontWeight = FontWeight.W500
                ))
            Icon(Icons.Default.KeyboardArrowRight, tint = White, contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun HelpingPage(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .padding(25.dp),
        ){
            Box(modifier = Modifier.fillMaxWidth(0.3f)){
                val imageModifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                val image: Painter = painterResource(id = R.drawable.icon_help)
                Image(painter = image,contentDescription = "",imageModifier)
            }
            Text(text="帮助",
                modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.8f),
                style = TextStyle(
                    color = White,
                    fontSize = 19.sp,
                    letterSpacing = (-0.5).sp,
                    fontWeight = FontWeight.W500
                ))
            Icon(Icons.Default.KeyboardArrowRight, tint = White, contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}
