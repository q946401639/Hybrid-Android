package com.fengdewang.hybrid_android;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by fengdewang on 2018/1/3.
 * 作为单例 去记录webviewActivity的个数 和 堆栈情况，相当于ios中 root.viewControllers
 */

public class WebViewActivityManager extends Application {

    private static List<Activity> list = new ArrayList<>();

    //添加一个WebViewActivity的实例
    public static void addActivity(Activity activity){

        list.add(activity);

    }

    //finish n 个WebViewActivity
    public static void finishActivity(int step){

        if (list != null) {
            int listLen = list.size() - 1;
            int stepLen = list.size() - 1 - step;

            for (int i = listLen; i > stepLen; i--) {
                list.get(i).finish();
                list.remove(i);
            }
        }

    }

    //获取当前WebViewActivity堆栈实例的个数
    public static int getActivityListSize(){
        return list.size();
    }

    //获取堆栈中的某个WebViewActivity实例
    public static Activity getActivity(int i){
        return list.get(i);
    }

}
