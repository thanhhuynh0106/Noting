package com.example.app.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class CustomClock extends View {

    private Paint paintCircle, paintCenter, paintHour, paintMinute, paintSecond, paintMark;
    private int width, height, radius;
    private Paint paintText, paintBorder;
    private Handler handler = new Handler();

    public CustomClock(Context context) {
        super(context);
        init();
    }

    public CustomClock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(Color.WHITE);
        paintCircle.setStyle(Paint.Style.FILL);

        paintMark = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMark.setColor(Color.BLACK);
        paintMark.setStrokeWidth(14f);

        paintCenter = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCenter.setColor(Color.BLACK);
        paintCenter.setStyle(Paint.Style.FILL);

        paintHour = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintHour.setColor(Color.BLACK);
        paintHour.setStrokeWidth(12f);
        paintHour.setStrokeCap(Paint.Cap.ROUND);

        paintMinute = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMinute.setColor(Color.DKGRAY);
        paintMinute.setStrokeWidth(8f);
        paintMinute.setStrokeCap(Paint.Cap.ROUND);

        paintSecond = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSecond.setColor(Color.RED);
        paintSecond.setStrokeWidth(4f);
        paintSecond.setStrokeCap(Paint.Cap.ROUND);

        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(6f);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(32f);
        paintText.setTextAlign(Paint.Align.CENTER);

        handler.post(tickRunnable);
    }

    private final Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getWidth();
        height = getHeight();
        int min = Math.min(width, height);
        radius = min / 2 - 20;

        int cx = width / 2;
        int cy = height / 2 ;

        canvas.drawCircle(cx, cy, radius + 12, paintBorder);
        canvas.drawCircle(cx, cy, radius, paintCircle);

        String[] romanNumerals = {
                "XII", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI"
        };

        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30 - 90);
            float textX = (float) (cx + Math.cos(angle) * (radius - 40));
            float textY = (float) (cy + Math.sin(angle) * (radius - 40) + paintText.getTextSize() / 3);
            canvas.drawText(romanNumerals[i], textX, textY, paintText);
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        float hourAngle = (hour + minute / 60f) * 30 - 90;
        drawHand(canvas, cx, cy, hourAngle, radius * 0.5f, paintHour);

        float minuteAngle = (minute + second / 60f) * 6 - 90;
        drawHand(canvas, cx, cy, minuteAngle, radius * 0.7f, paintMinute);

        float secondAngle = second * 6 - 90;
        drawHand(canvas, cx, cy, secondAngle, radius * 0.8f, paintSecond);

        canvas.drawCircle(cx, cy, 16, paintCenter);
    }

    private void drawHand(Canvas canvas, int cx, int cy, float angle, float length, Paint paint) {
        double rad = Math.toRadians(angle);
        float x = (float) (cx + Math.cos(rad) * length);
        float y = (float) (cy + Math.sin(rad) * length);
        canvas.drawLine(cx, cy, x, y, paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(tickRunnable);
    }
}