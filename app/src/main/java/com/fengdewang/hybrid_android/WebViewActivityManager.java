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
 */

public class WebViewActivityManager extends Application {

    private static List<Activity> list = new ArrayList<>();

    public static void addActivity(Activity activity){

        list.add(activity);

    }

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

    public static int getActivityListSize(){
        return list.size();
    }

    public static Activity getActivity(int i){
        return list.get(i);
    }

}
