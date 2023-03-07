package cn.edu.zjut.urlcheck.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.zjut.urlcheck.BottomMenuContent
import cn.edu.zjut.urlcheck.R


@Composable
fun UrlCheck(){
    Box(modifier = Modifier
        .background(Background)
        .fillMaxSize())
    {
        Column {
            ScreenTitle()
//            SearchText()
        }
        BottomMenu(items = listOf(
            BottomMenuContent("主页", R.drawable.icon_home) ,
            BottomMenuContent("设置", R.drawable.icon_setting)
        ), modifier = Modifier.align(Alignment.BottomCenter))

    }
}


@Composable
fun ScreenTitle() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        Row{
            val imageModifier = Modifier
                .size(30.dp)
                .clip(CircleShape)

            val image: Painter = painterResource(id = R.drawable.urlcheck_logo)
            Image(painter = image,contentDescription = "",imageModifier)

            Text(text="URL Check",
                modifier=Modifier.padding(horizontal = 10.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    letterSpacing = (-0.5).sp,
                    color = White
                ))
        }




    }

}

@Composable
fun BottomMenu(
    items:List<BottomMenuContent>,
    modifier: Modifier=Modifier,
    activeHighlightColor: Color = ButtonBlue,
    activeTextColor: Color = White,
    inactiveTextColor: Color =DarkBlue,
    initialSelectedIndex:Int=0
){
    var selectedItemIndex by remember {
        mutableStateOf(initialSelectedIndex)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(LightBlue)
            .padding(15.dp)
    ) {
        items.forEachIndexed{index,item->
            BottomMenuItem(
                item = item,
                isSelected = index == selectedItemIndex,
                activeHighlightColor=activeHighlightColor,
                activeTextColor=activeTextColor,
                inactiveTextColor=inactiveTextColor
            ){
                selectedItemIndex = index
            }
        }
    }
    when(selectedItemIndex) { // 根据state值的变化动态切换当面显示的页面
        0 -> HomeScreen()
        1 -> SettingSreen()
    }
}

@Composable
fun BottomMenuItem(
    item:BottomMenuContent,
    isSelected:Boolean =false,
    activeTextColor: Color = White,
    activeHighlightColor: Color = ButtonBlue,
    inactiveTextColor: Color = DarkBlue,
    onItemClick:()->Unit
){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onItemClick() }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) activeHighlightColor else Color.Transparent)
                .padding(10.dp)
        ){
            Icon(painter = painterResource(id = item.iconId),
                contentDescription = item.title,
                tint = if (isSelected) activeTextColor else inactiveTextColor,
                modifier = Modifier.size(25.dp))
          }
    }
}