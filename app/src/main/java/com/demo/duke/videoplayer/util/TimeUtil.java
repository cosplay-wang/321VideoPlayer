package com.demo.duke.videoplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhiwei.wang on 2017/12/26.
 */

public class TimeUtil {
    /* * yy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'
            * yy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'
            * yy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00'
            * yy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am'
            * */



    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }
}
