package com.demo.duke.videoplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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



        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return sDateFormat.format(new Date(dateTime));
    }
}
