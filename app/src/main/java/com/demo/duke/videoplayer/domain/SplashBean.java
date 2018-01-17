package com.demo.duke.videoplayer.domain;

import java.util.List;

/**
 * Created by zhiwei.wang on 2018/1/17.
 * wechat 760560322
 * 作用：
 */

public class SplashBean {

    /**
     * images : [{"startdate":"20180116","fullstartdate":"201801161600","enddate":"20180117","url":"/az/hprichbg/rb/BarHarborCave_ZH-CN8055769470_1920x1080.jpg","urlbase":"/az/hprichbg/rb/BarHarborCave_ZH-CN8055769470","copyright":"从缅因州巴港附近的海岸看到的银河 (© Adam Woodworth/Aurora Photos)","copyrightlink":"http://www.bing.com/search?q=%E9%93%B6%E6%B2%B3&form=hpcapt&mkt=zh-cn","quiz":"/search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20180116_BarHarborCave%22&FORM=HPQUIZ","wp":false,"hsh":"3e7d87bbcba12cc493ce39a2c48e07f6","drk":1,"top":1,"bot":1,"hs":[]}]
     * tooltips : {"loading":"正在加载...","previous":"上一个图像","next":"下一个图像","walle":"此图片不能下载用作壁纸。","walls":"下载今日美图。仅限用作桌面壁纸。"}
     */

    public TooltipsBean tooltips;
    public List<ImagesBean> images;

    public static class TooltipsBean {
        /**
         * loading : 正在加载...
         * previous : 上一个图像
         * next : 下一个图像
         * walle : 此图片不能下载用作壁纸。
         * walls : 下载今日美图。仅限用作桌面壁纸。
         */

        public String loading;
        public String previous;
        public String next;
        public String walle;
        public String walls;
    }

    public static class ImagesBean {
        /**
         * startdate : 20180116
         * fullstartdate : 201801161600
         * enddate : 20180117
         * url : /az/hprichbg/rb/BarHarborCave_ZH-CN8055769470_1920x1080.jpg
         * urlbase : /az/hprichbg/rb/BarHarborCave_ZH-CN8055769470
         * copyright : 从缅因州巴港附近的海岸看到的银河 (© Adam Woodworth/Aurora Photos)
         * copyrightlink : http://www.bing.com/search?q=%E9%93%B6%E6%B2%B3&form=hpcapt&mkt=zh-cn
         * quiz : /search?q=Bing+homepage+quiz&filters=WQOskey:%22HPQuiz_20180116_BarHarborCave%22&FORM=HPQUIZ
         * wp : false
         * hsh : 3e7d87bbcba12cc493ce39a2c48e07f6
         * drk : 1
         * top : 1
         * bot : 1
         * hs : []
         */

        public String startdate;
        public String fullstartdate;
        public String enddate;
        public String url;
        public String urlbase;
        public String copyright;
        public String copyrightlink;
        public String quiz;
        public boolean wp;
        public String hsh;
        public int drk;
        public int top;
        public int bot;
        public List<?> hs;
    }
}
