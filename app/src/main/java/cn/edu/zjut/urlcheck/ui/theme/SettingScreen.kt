package cn.edu.zjut.urlcheck.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.zjut.urlcheck.R

@Composable
fun SettingScreen(){
    Box(modifier = Modifier
        .fillMaxSize())
    {
        Column {
            Spacer(modifier = Modifier.size(280.dp))
            ChangeTheme()
            HelpingPage()
        }
        
    }
}

@Composable
fun ChangeTheme(){
    val urlCheckColors= LocalURLCheckColors.current
    val checkedState = rememberSaveable {
        mutableStateOf(!urlCheckColors.getMode())
    }
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
                    color = urlCheckColors.textColor,
                    fontWeight = FontWeight.W500
                ))
            val urlCheckColors= LocalURLCheckColors.current
            Switch(
                modifier=Modifier
                    .align(Alignment.CenterVertically),
                checked = checkedState.value,
                colors= SwitchDefaults.colors(
                    uncheckedThumbColor= White,
                    checkedThumbColor= White,          //启用和选中时用于拇指的颜色
                    uncheckedTrackColor= Gray,
                    checkedTrackColor= ButtonBlue,             //启用和选中时用于轨道的颜色
                ),
                onCheckedChange = {
                    checkedState.value = it
                    urlCheckColors.toggleTheme()
                }
            )

        }
    }
}

@Composable
fun HelpingPage(){
    var isDialogShowing by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isDialogShowing=true
            }
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
                    color = urlCheckColors.textColor,
                    fontSize = 19.sp,
                    letterSpacing = (-0.5).sp,
                    fontWeight = FontWeight.W500
                ))
            Icon(Icons.Default.KeyboardArrowRight, tint = urlCheckColors.textColor, contentDescription = null, modifier = Modifier.align(Alignment.CenterVertically))
            if (isDialogShowing){
                AlertDialog(
                    onDismissRequest = {
                        isDialogShowing = false
                    },
                    title = {
                        Text("帮助文档")
                    },
                    text = {
                        Text("这里是帮助界面")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isDialogShowing = false
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White // 设置按钮文本颜色为白色
                            )
                        ) {
                            Text("确定")
                        }
                    },
                    backgroundColor = urlCheckColors.bottomMenuColor, // 设置对话框的背景颜色
                    contentColor = White // 设置对话框中所有文本和图标所使用的颜色
                )
            }

        }
    }
}
