package cn.edu.zjut.urlcheck.ui.theme

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.edu.zjut.urlcheck.R
import cn.edu.zjut.urlcheck.activities.QrCodeScanActivity
import cn.edu.zjut.urlcheck.utils.*
import coil.compose.AsyncImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Column {
            Spacer(modifier = Modifier.size(70.dp))
            ScanQrCode()
            SearchText()
            NetworkDetection()
        }
    }
}

var resultsText by mutableStateOf("There is no result")
var loadingState by mutableStateOf(false)
@Composable
fun ScanQrCode() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center
        ) {
            val context = LocalContext.current
            val target = Intent(context, QrCodeScanActivity::class.java)

            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                    activityResult.data?.apply {
                        val result = getStringExtra("SCAN_RESULT").toString()
                        //resultsText=result
                        LogUtil.logInfo(result)
                        if (!UrlJudgeUtil().getCompleteUrl(result)) {
                            resultsText = "扫码结果:$result\n\n该二维码不包含URL链接"
                        } else {
                            val json = JSONObject()
                            json.put("url", result)
                            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                            loadingState=true
                            val call: Call<ResponseBody> = RequestUtil.service.checkURL(requestBody)
                            call.enqueue(
                                object : Callback<ResponseBody> {
                                    override fun onResponse(
                                        call: Call<ResponseBody>,
                                        response: Response<ResponseBody>
                                    ) {
                                        try{
                                            loadingState=false
                                            val jsonData: String = response.body()!!.string()
                                            val jsonObj = JSONObject(jsonData)
                                            val code = jsonObj.getString("code")
                                            if(code=="200"){
                                                val data = jsonObj.getJSONObject("data")
                                                val label = data.getString("label")
                                                val label2 = data.getString("label2")
                                                val src = data.getString("src")
                                                if(label!="0"&&label2!="0"){
                                                    val type=ConvertUtil.toType(label)
                                                    val type2=ConvertUtil.toType(label2)
                                                    resultsText="URL: $result\n"
                                                    if(label==label2){
                                                        resultsText+="该网站为风险网站，风险类型为$type\n网站截图:"
                                                    }else{
                                                        resultsText+="该网站为风险网站，风险类型为$type 或 $type2\n网站截图:"
                                                    }
                                                    imgUrl=src
                                                }else{
                                                    resultsText="URL: $result\n"
                                                    resultsText+="该网站为正常网站"
                                                    imgUrl=""
                                                    //跳转到浏览器
                                                    val uri = Uri.parse(result)
                                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                                    context.startActivity(intent)
                                                }
                                                LogUtil.logInfo(label)
                                                LogUtil.logInfo(label2)
                                                LogUtil.logInfo(src)
                                                //resultsText = data.toString()
                                            }else if(code=="400"){
                                                val data = jsonObj.getJSONObject("data")
                                                val msg = data.getString("msg")
                                                resultsText=msg
                                            }
                                        }catch (e: Exception){
                                            resultsText=e.toString()
                                        }

                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        loadingState=false
                                        resultsText="请求失败"
                                        LogUtil.logInfo(t.toString())

                                    }
                                }
                            )
                            //val uri = Uri.parse(result)
                            //val intent = Intent(Intent.ACTION_VIEW, uri)
                            //context.startActivity(intent)
                        }
                    }
                }
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    LogUtil.logInfo("PERMISSION GRANTED")
                    launcher.launch(target)
                } else {
                    Toast.makeText(context, "相机权限未开启!", Toast.LENGTH_SHORT).show()
                    LogUtil.logInfo("PERMISSION DENIED")
                }
            }
            Button(
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) -> {
                            LogUtil.logInfo("Camera permission has been granted")
                            launcher.launch(target)
                        }
                        else -> {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }

                },
                modifier = Modifier.size(130.dp, 130.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = urlCheckColors.cardColor)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp,80.dp)
                )

            }
            Text(
                text = "二维码检测",
                color = urlCheckColors.textColor,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(0.dp, 10.dp)
            )

        }

    }


}
var imgUrl by mutableStateOf("")
@Composable
fun SearchText() {

    var text by rememberSaveable {
        mutableStateOf("")
    }
    var isURL by rememberSaveable {
        mutableStateOf(true)
    }

    val focusManager = LocalFocusManager.current

    Box(
        Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(15.dp, 0.dp),
            verticalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                maxLines = 2,
                value = text,
                onValueChange = {
                    text = it
                },
                textStyle = TextStyle(color = urlCheckColors.textColor),
                cursorBrush = SolidColor(urlCheckColors.textColor),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        urlCheckColors.searchTextFieldColor,
                        RoundedCornerShape(percent = 30)
                    )
                    .padding(5.dp)
                    .background(
                        urlCheckColors.searchTextFieldColor,
                        RoundedCornerShape(percent = 29)
                    ),
                decorationBox = { innerTextField ->
                    Row(
                        Modifier
                            .background(
                                urlCheckColors.searchTextFieldColor,
                                RoundedCornerShape(percent = 30)
                            )
                            .padding(0.dp)
                            .background(
                                urlCheckColors.searchTextFieldColor,
                                RoundedCornerShape(percent = 29)
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Icon(Icons.Default.Search, tint = White, contentDescription = null)
                        Box(
                            modifier = Modifier
                                .padding(top = 7.dp, bottom = 7.dp, end = 7.dp)
                                .fillMaxWidth(0.9f)
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "请输入搜索的URL",
                                    style = TextStyle(
                                        color = urlCheckColors.placeholderTextColor
                                    )
                                )
                            }
                            innerTextField()
                        }
                        if (text.isNotEmpty()) {
                            Icon(Icons.Default.Close, tint = urlCheckColors.placeholderTextColor,
                                contentDescription = null,
                                modifier = Modifier.clickable { text = "" }
                            )
                        } else {
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
            )
            val context = LocalContext.current
            var hintText by rememberSaveable {
                mutableStateOf("")
            }
            Button(
                onClick = {
                    isURL = UrlJudgeUtil().getCompleteUrl(text)
                    if (isURL) {
                        val json = JSONObject()
                        json.put("url", text)
                        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                        LogUtil.logInfo(json.toString())
                        loadingState=true//转圈圈
                        focusManager.clearFocus()
                        val call: Call<ResponseBody> = RequestUtil.service.checkURL(requestBody)
                        call.enqueue(
                            object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {
                                    try{
                                        loadingState=false
                                        val jsonData: String = response.body()!!.string()
                                        val jsonObj = JSONObject(jsonData)
                                        val code = jsonObj.getString("code")
                                        if(code=="200"){
                                            val data = jsonObj.getJSONObject("data")
                                            val label = data.getString("label")
                                            val label2 = data.getString("label2")
                                            val src = data.getString("src")
                                            if(label!="0"&&label2!="0"){
                                                val type=ConvertUtil.toType(label)
                                                val type2=ConvertUtil.toType(label2)
                                                resultsText="URL: $text\n"
                                                if(label==label2){
                                                    resultsText+="该网站为风险网站，风险类型为$type\n网站截图:"
                                                }else{
                                                    resultsText+="该网站为风险网站，风险类型为$type 或 $type2\n网站截图:"
                                                }
                                                imgUrl=src
                                            }else{
                                                resultsText="URL: $text\n"
                                                resultsText+="该网站为正常网站"
                                                imgUrl=""
                                                val uri=Uri.parse(text)
                                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                                context.startActivity(intent)
                                            }
                                            LogUtil.logInfo(label)
                                            LogUtil.logInfo(label2)
                                            LogUtil.logInfo(src)
                                            //resultsText = data.toString()
                                        }else if(code=="400"){
                                            val data = jsonObj.getJSONObject("data")
                                            val msg = data.getString("msg")
                                            resultsText=msg
                                        }
                                    }catch (e: Exception){
                                        resultsText=e.toString()
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    loadingState=false
                                    resultsText="请求失败"
                                    LogUtil.logInfo(t.toString())
                                }
                            }
                        )
                    }else{
                        if (!text.startsWith("http://") && !text.startsWith("https://")){
                            hintText="请检查URL是否缺失http://或https://"
                        }else{
                            hintText="请输入有效的URL!"
                        }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .fillMaxWidth()
                    .height(43.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ButtonBlue,
                    contentColor = White
                )
            ) {
                Text(text = "Check!")
            }

            if (!isURL) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = hintText, color = YuRed, fontWeight = FontWeight.W700)
                }
            }
            if(!loadingState){
                LabelCard(resultsText)
            }else{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentAlignment = Alignment.Center
                ){
                    Row(verticalAlignment = Alignment.CenterVertically){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp),
                            color = ButtonBlue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "加载中...",
                            color = urlCheckColors.textColor
                        )
                    }
                }
            }

        }
    }
}

fun handleUrl(context:Context,url:String){
    val isURL = UrlJudgeUtil().getCompleteUrl(url)
    if (isURL) {
        val json = JSONObject()
        json.put("url", url)
        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        LogUtil.logInfo(json.toString())
        loadingState=true//转圈圈
        val call: Call<ResponseBody> = RequestUtil.service.checkURL(requestBody)
        call.enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    try{
                        loadingState=false
                        val jsonData: String = response.body()!!.string()
                        val jsonObj = JSONObject(jsonData)
                        val code = jsonObj.getString("code")
                        if(code=="200"){
                            val data = jsonObj.getJSONObject("data")
                            val label = data.getString("label")
                            val label2 = data.getString("label2")
                            val src = data.getString("src")
                            if(label!="0"&&label2!="0"){
                                val type=ConvertUtil.toType(label)
                                val type2=ConvertUtil.toType(label2)
                                resultsText="URL: $url\n"
                                if(label==label2){
                                    resultsText+="该网站为风险网站，风险类型为$type\n网站截图:"
                                }else{
                                    resultsText+="该网站为风险网站，风险类型为$type 或 $type2\n网站截图:"
                                }
                                imgUrl=src
                            }else{
                                resultsText="URL: $url\n"
                                resultsText+="该网站为正常网站"
                                imgUrl=""
                                //跳转到浏览器
                                val uri = Uri.parse(url)
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(intent)
                            }
                            LogUtil.logInfo(label)
                            LogUtil.logInfo(label2)
                            LogUtil.logInfo(src)
                        }else if(code=="400"){
                            val data = jsonObj.getJSONObject("data")
                            val msg = data.getString("msg")
                            resultsText=msg
                        }
                    }catch (e: Exception){
                        resultsText=e.toString()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    loadingState=false
                    resultsText="请求失败"
                    LogUtil.logInfo(t.toString())
                }
            }
        )
    }
}

@Composable
fun LabelCard(
    resultsText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(urlCheckColors.cardColor)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    item {
                        Text(
                            text = resultsText,
                            color = urlCheckColors.textColor,
                            modifier = Modifier.padding(15.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp,3.dp)
                        ){
                            AsyncImage(
                                model = imgUrl,
                                contentDescription = null
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun NetworkDetection() {
    val context = LocalContext.current
    var statusColor:Color
    var statusText:String
    val status by NetWorkManager(context).observe()
        .collectAsStateWithLifecycle(ConnectivityObserver.Status.Unavailable)

    if(NetWorkManager(context).isNetWorkConnected()){
        statusText="网络良好"
        statusColor=Color.Green
    }else{
        statusText="网络异常"
        statusColor=Color.Red
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if(status.toString()=="Available"){
            statusText="网络良好"
            statusColor=Color.Green
        }else{
            statusText="网络异常"
            statusColor=Color.Red
        }
        Card(
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(40.dp)
                .networkPoint(statusColor)
        ) {
            Box(
                modifier = Modifier
                    .background(urlCheckColors.cardColor)

            ) {
                Text(
                    text = statusText,
                    color= urlCheckColors.textColor,
                    fontSize= 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp, 0.dp, 0.dp, 0.dp)
                )
            }
        }
    }

}

fun Modifier.networkPoint(statusColor:Color): Modifier = drawWithContent {
    drawContent()
    drawIntoCanvas {
        val paint = Paint().apply {
            color = statusColor
        }
        it.drawCircle(
            center = Offset(x = size.width/7, y = size.height/2),
            radius = (4.dp).toPx(),
            paint = paint
        )
    }
}