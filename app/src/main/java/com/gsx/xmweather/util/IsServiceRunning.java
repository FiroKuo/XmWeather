package com.gsx.xmweather.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * @author Administrator
 * @version $Rev
 * @time 2017/1/26 15:53
 * @des ${TODO}
 * @updateAuthor $Author
 * @updateDate $Date
 * @updateDes ${TODO}
 */

public class IsServiceRunning {

    public static boolean isServiceRunning(Context context,String name){

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(200);
        for (ActivityManager.RunningServiceInfo info:runningServices) {
            if (name.equals(info.getClass().getName())){
                return true;
            }
        }
        return false;
    }
}
