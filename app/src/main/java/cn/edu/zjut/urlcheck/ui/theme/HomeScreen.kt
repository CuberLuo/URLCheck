package cn.edu.zjut.urlcheck.ui.theme

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import cn.edu.zjut.urlcheck.BottomMenuContent
import cn.edu.zjut.urlcheck.R
import cn.edu.zjut.urlcheck.activities.MainActivity
import cn.edu.zjut.urlcheck.utils.LogUtil
import cn.edu.zjut.urlcheck.utils.RequestUtil
import cn.edu.zjut.urlcheck.utils.UrlJudgeUtil
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.analyze.MultiFormatAnalyzer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun HomeScreen(){
        Box(modifier = Modifier
            .fillMaxSize())
        {
            Column {
                Spacer(modifier = Modifier.size(70.dp))
                SearchText()

            }
        }
}

@Composable
fun SearchText(){
    var text by remember {
        mutableStateOf("") }
    var resultsText by remember {
        mutableStateOf("There is no result")
    }
    var isURL by remember {
        mutableStateOf(true) }

    val focusManager = LocalFocusManager.current

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
                value = text,
                onValueChange = {
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
                    Row(
                        Modifier
                            .background(BabyBlue, RoundedCornerShape(percent = 30))
                            .padding(0.dp)
                            .background(BabyBlue, RoundedCornerShape(percent = 29)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween )
                    {
                        Icon(Icons.Default.Search, tint = White, contentDescription = null)
                        Box(modifier = Modifier
                            .padding(top = 7.dp, bottom = 7.dp, end = 7.dp)
                            .fillMaxWidth(0.9f)) {
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
            /*val openNormAlertDialog = remember {
                mutableStateOf(false)
            }
            val msg = remember {
                mutableStateOf("")
            }
            AlertDialogComponent(
                dialogState = openNormAlertDialog,
                msg=msg.value
            )*/
            Button(onClick = {
                isURL= UrlJudgeUtil().getCompleteUrl(text)
                if (!isURL){
                val call: Call<ResponseBody> = RequestUtil.service.getQRCode(text)
                call.enqueue(
                    object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val s: String = response.body()!!.string()
//                            msg.value=s
//                            openNormAlertDialog.value = !openNormAlertDialog.value
                            resultsText = s
                            focusManager.clearFocus()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            LogUtil.logInfo(t.toString())

                        }
                    }
                )
            } },
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
            if(!isURL){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text="请输入有效的URL!", color = YuRed, fontWeight = FontWeight.W700)
                }
            }
            LabelCard(resultsText)
        }
    }


}

@Composable
fun AlertDialogComponent(
    dialogState: MutableState<Boolean>,
    msg:String
) {
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = { dialogState.value = false },
            title = { Text(text = "请求结果") },
            text = { Text(text = msg) },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogState.value = false
                    }
                ) {
                    Text(text = "确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogState.value = false
                    }
                ) {
                    Text(text = "取消")
                }
            }
        )
    }
}

@Composable
fun LabelCard(
    resultsText:String
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 15.dp),
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
                Text(text = resultsText, color = White, modifier = Modifier.padding(15.dp))
            }
        }
    }


}