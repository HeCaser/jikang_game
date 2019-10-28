package com.example.game.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.View;

public class CircleWaterUtil {

    public static void setWaterBitmap(final Context context, final View view, String flagText) {
        setWaterBitmap(context, view, 10, 5, flagText, 8, 55, 30);
    }

    public static void setWaterBitmap(final Context context, final View view, final int paddingXDp, final int yOffsetDp, final String flagText, final int textSizeSp, final int xDp, final int yDp) {
        view.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getWaterBitmap(context, view.getWidth(), view.getHeight(), paddingXDp, yOffsetDp, flagText, textSizeSp, xDp, yDp);
                view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
            }
        });
    }

    public static Bitmap getWaterBitmap(Context context, int viewWidthPx, int viewHeightPx, String flagText) {
        return getWaterBitmap(context, viewWidthPx, viewHeightPx, 10, 5, flagText, 8, 55, 30);
    }

    public static Bitmap getWaterBitmap(Context context, int viewWidthPx, int viewHeightPx, int paddingXDp, int yOffsetDp, String flagText, int textSizeSp, int xDp, int yDp) {
        int paddingHoriPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingXDp, context.getResources().getDisplayMetrics());
        int paddingTopPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yOffsetDp, context.getResources().getDisplayMetrics());
        int xDisPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, xDp, context.getResources().getDisplayMetrics());
        int yDisPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, yDp, context.getResources().getDisplayMetrics());
        float textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSizeSp, context.getResources().getDisplayMetrics());

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#e4e4e4"));//#e4e4e4
        paint.setTextSize(textSizePx);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);
        float base = -paint.getFontMetrics().ascent - paint.getFontMetrics().leading;
        float fontHeight = paint.getFontMetrics().descent + base;
        float fontWidth = paint.measureText(flagText);


        int minBitmapWidth = (int) Math.ceil(fontHeight / 2.0 + Math.sqrt(3) * fontWidth / 2);
        int minBitmapHeight = (int) Math.ceil(fontWidth / 2.0 + Math.sqrt(3) * fontHeight / 2);
        int xDistance = getBitmapXDistance(viewWidthPx - paddingHoriPx - paddingHoriPx, xDisPx, minBitmapWidth);

        Bitmap bitmap = Bitmap.createBitmap(viewWidthPx, viewHeightPx, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Path path = new Path();

        int heightPadding = yDisPx / 2;
        float startY = (float) (minBitmapHeight - Math.sqrt(3) * paint.getFontMetrics().descent / 2) + heightPadding + paddingTopPx;
        float endY = (float) (Math.sqrt(3) * base / 2) + heightPadding + paddingTopPx;
        while (startY < viewHeightPx - paddingTopPx) {
            float startX = base / 2 + paddingHoriPx;
            float endX = minBitmapWidth - paint.getFontMetrics().descent / 2 + paddingHoriPx;
            while (endX < viewWidthPx - paddingHoriPx) {
                path.reset();
                path.moveTo(startX, startY);
                path.lineTo(endX, endY);
                canvas.drawTextOnPath(flagText, path, 0, 0, paint);
                startX += xDistance;
                endX += xDistance;
            }
            startY += (yDisPx + minBitmapHeight);
            endY += (yDisPx + minBitmapHeight);
        }
        return bitmap;
    }

    private static int getBitmapXDistance(int screenWidth, int xDPx, int minBitmapWidth) {
        for (int i = 6; i > 3; i--) {
            if (i * xDPx + minBitmapWidth < screenWidth) {
                return (screenWidth - minBitmapWidth) / i;
            }
        }
        return (screenWidth - minBitmapWidth) / 4;
    }
}
