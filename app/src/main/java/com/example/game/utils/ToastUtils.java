package com.example.game.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;


/**
 * @author: hdib
 * @time: 2018/5/26 11:03
 * @e-mail: guohaichang@163.com
 * @des:
 */
public class ToastUtils {

    private static Toast mToast;
    private static Handler handler = new Handler();
    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mToast != null) {
                mToast.cancel();
            }
        }
    };

    /**
     * LENGTH_SHORT:4000ms
     * LENGTH_LONG:7000ms
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
//        handler.removeCallbacks(runnable);
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.show();
    }


    public static void showCenter(Context context, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);

        mToast.show();
    }
}
