package com.gsx.xmweather.util;

import android.util.Log;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/25 8:57
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class LogUtil {

    public static boolean flag=true;
    public static void e(String tag,String msg){

        if (flag) {
            Log.e(tag, msg);
        }
    }
}
