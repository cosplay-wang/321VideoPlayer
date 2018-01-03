package com.demo.duke.videoplayer.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hasee on 2017/9/10.
 */

public class JsonUtil {
    private static Gson gson = null;
    static {
        if (gson == null) {
            gson = new Gson();
        }
    }
    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }


    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     * ,有问题，这个泛型的时候搞出问题来，处理起来很麻烦，，，，，，如果一定要处理的话，使用，单个的类转化，便利得到list
     * 最好的处理方式是，把拿下来的数据直接gsonformat，转成类，然后拿到其中你需要的部分
     * @param gsonString
     * @param cls
     * @return
     */
//    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
//        List<T> list = null;
//        if (gson != null) {
//            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
//            }.getType());
//        }
//        return list;
//    }

    /**
     * 转成list
     * 解决泛型问题
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

}
