package cn.edu.zjut.urlcheck.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.zjut.urlcheck.BottomMenuContent
import cn.edu.zjut.urlcheck.R

@Composable
fun HomeScreen(){
    Box(modifier = Modifier
        .background(Background)
        .fillMaxSize())
    {
        Column() {
            ScreenTitle()
            SearchText()
            LabelCard()
        }
        BottomMenu(items = listOf(
            BottomMenuContent("主页", R.drawable.icon_home) ,
            BottomMenuContent("设置", R.drawable.icon_setting)
        ), modifier = Modifier.align(Alignment.BottomCenter))


    }
}


@Composable
fun BottomMenu(
    items:List<BottomMenuContent>,
    modifier: Modifier=Modifier,
    activeHighlightColor: Color= ButtonBlue,
    activeTextColor: Color= White,
    inactiveTextColor: Color=DarkBlue ,
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
}

@Composable
fun BottomMenuItem(
    item:BottomMenuContent,
    isSelected:Boolean =false,
    activeTextColor: Color= White,
    activeHighlightColor:Color= ButtonBlue,
    inactiveTextColor: Color= DarkBlue,
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


@Composable
fun ScreenTitle() {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ){
        Row() {
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
fun SearchText(){
    var text by remember {
        mutableStateOf("") }

    Box(Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                maxLines=2,
                value = text, onValueChange = {
                    text = it
                },
                textStyle = TextStyle(color = White),
                cursorBrush = SolidColor(White),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(BabyBlue, RoundedCornerShape(percent = 30))
                    .padding(5.dp)
                    .background(BabyBlue, RoundedCornerShape(percent = 29)),
                decorationBox = {innerTextField ->
                    Row(Modifier
                        .background(BabyBlue, RoundedCornerShape(percent = 30))
                        .padding(0.dp)
                        .background(BabyBlue, RoundedCornerShape(percent = 29)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween )
                    {
                        Icon(Icons.Default.Search, tint = White, contentDescription = null)
                        Box(modifier = Modifier.padding(top = 7.dp, bottom = 7.dp, end = 7.dp).fillMaxWidth(0.9f)) {
                            if(text.isEmpty()){
                                Text(
                                    text="请输入搜索的URL",
                                    style = TextStyle(
                                        color=Color(107, 118, 179)
                                    )
                                )
                            }
                            innerTextField()
                        }
                        if(!text.isEmpty()){
                            Icon(Icons.Default.Close, tint = Color(107, 118, 179),
                                contentDescription = null,
                                modifier = Modifier.clickable { text="" }
                            )
                        }else{
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            )
            Button(onClick = { /*TODO*/ } ,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .fillMaxWidth(1f)
                    .height(43.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ButtonBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Check!")
            }
        }
    }


}

@Composable
fun LabelCard(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp, 15.dp),
        contentAlignment = Alignment.Center
    ){
        Card(shape = RoundedCornerShape(20.dp),
            elevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(CardBlue)){
                Text(text = "Search result label", color = White, modifier = Modifier.padding(15.dp))
            }
        }
    }


}