package cn.edu.zjut.urlcheck.utils

import java.util.regex.Pattern

class UrlJudgeUtil {
    fun getCompleteUrl(text: String): Boolean {
        val p = Pattern.compile("((https?):\\/\\/)?(((www\\.)?(([a-zA-Z0-9\\-]+\\.)+([a-zA-Z])+)|(\\b((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\b)(:[0-9]{1,5})?))((\\/[^\\/\\\\:\\*\\?\"<>\\|]*)*(\\?.*=.*(&.*=.*)*)?)?", Pattern.CASE_INSENSITIVE)
        val matcher = p.matcher(text);
        return matcher.matches();
    }

}