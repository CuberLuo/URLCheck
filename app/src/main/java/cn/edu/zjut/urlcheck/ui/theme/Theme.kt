package cn.edu.zjut.urlcheck.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val LocalURLCheckColors = compositionLocalOf {
    URLCheckColors()
}

val urlCheckColors: URLCheckColors
    @Composable
    @ReadOnlyComposable
    get() = LocalURLCheckColors.current

interface IURLCheckColors {
    val backgroundColor:Color @Composable get() = Color.Black
    val bottomMenuColor:Color @Composable get() = Color.Black
    val textColor:Color
    val cardColor:Color
    val placeholderTextColor:Color
    val searchTextFieldColor:Color
}

private object LightColors :IURLCheckColors{
    override val backgroundColor @Composable get() = BackgroundLightMode
    override val bottomMenuColor @Composable get() = BottomMenuLightMode
    override val textColor: Color get() = TextLightMode
    override val cardColor: Color get() = CardLightMode
    override val placeholderTextColor: Color get() = TextLightMode
    override val searchTextFieldColor: Color get() = CardLightMode
}

private object DarkColors :IURLCheckColors{
    override val backgroundColor @Composable get() = BackgroundDarkMode
    override val bottomMenuColor @Composable get() = BottomMenuDarkMode
    override val textColor: Color get() = TextDarkMode
    override val cardColor: Color get() = CardDarkMode
    override val placeholderTextColor: Color get() = PlaceHolderDarkMode
    override val searchTextFieldColor: Color get() = SearchTextFieldDarkMode
}

class URLCheckColors :IURLCheckColors{
    var isLight by mutableStateOf(true)
        private set

    private val curColors by derivedStateOf {
        if (isLight) LightColors else DarkColors
    }

    fun getMode():Boolean{
        return isLight
    }

    fun toggleTheme() {
        isLight = !isLight
    }

    fun toggleToLightColor() { isLight = true }
    fun toggleToDarkColor() { isLight = false }

    override val backgroundColor @Composable get() =animatedValue(curColors.backgroundColor)
    override val bottomMenuColor @Composable get() = animatedValue(curColors.bottomMenuColor)
    override val textColor: Color get() = curColors.textColor
    override val cardColor: Color get() = curColors.cardColor
    override val placeholderTextColor: Color get() = curColors.placeholderTextColor
    override val searchTextFieldColor: Color get() = curColors.searchTextFieldColor
}

@Composable
fun URLCheckTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val urlCheckColors=URLCheckColors()

    if (darkTheme){
        urlCheckColors.toggleToDarkColor()
    }else{
        urlCheckColors.toggleToLightColor()
    }
    //根据系统主题修改状态栏颜色
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (darkTheme) Color.Black else Color.White
        )
    }

    CompositionLocalProvider(LocalURLCheckColors provides urlCheckColors){
        MaterialTheme(
            colors= darkColors(),
            content = content
        )
    }

}

@Composable
private fun animatedValue(targetValue: Color) = animateColorAsState(
    targetValue = targetValue,
    tween(1500)
).value