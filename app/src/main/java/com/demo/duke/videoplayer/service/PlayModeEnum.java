package com.demo.duke.videoplayer.service;

/**
 * Created by zhiwei.wang on 2018/1/18.
 * wechat 760560322
 * 作用：
 */

public enum PlayModeEnum {
    LOOP(0),SINGLE(1),RANDOM(2);
    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return SINGLE;
            case 2:
                return RANDOM;
            case 0:
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}
