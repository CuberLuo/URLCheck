package cn.edu.zjut.urlcheck.utils

class ConvertUtil {
    companion object{
        fun toType(label:String): String {
            return when (label) {
                "0" -> "正常"
                "1" -> "购物消费"
                "2" -> "婚恋交友"
                "3" -> "假冒身份"
                "4" -> "钓鱼网站"
                "5" -> "冒充公检法"
                "6" -> "平台诈骗"
                "7" -> "招聘兼职"
                "8" -> "杀猪盘"
                "9" -> "博彩赌博"
                "10" -> "信贷理财"
                "11" -> "刷单诈骗"
                "12" -> "中奖诈骗"
                else -> "未知"
            }

        }
    }
}