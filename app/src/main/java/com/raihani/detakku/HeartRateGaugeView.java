package com.raihani.detakku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class HeartRateGaugeView extends View {

    private Paint paint;
    private RectF rectF;
    private int bpm = 92;

    public HeartRateGaugeView(Context context) {
        super(context);
        init();
    }

    public HeartRateGaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartRateGaugeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectF = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = Math.min(getWidth(), getHeight());
        float strokeWidth = size * 0.18f;
        float center = size / 2f;

        rectF.set(
                strokeWidth,
                strokeWidth,
                size - strokeWidth,
                size - strokeWidth
        );

        paint.setStrokeWidth(strokeWidth);

        // FAST
        paint.setColor(Color.RED);
        canvas.drawArc(rectF, 180f, 120f, false, paint);

        // SLOW
        paint.setColor(Color.YELLOW);
        canvas.drawArc(rectF, 300f, 60f, false, paint);

        // NORMAL
        paint.setColor(Color.GREEN);
        canvas.drawArc(rectF, 0f, 120f, false, paint);

        // Text BPM
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(size * 0.14f);

        canvas.drawText(
                bpm + " bpm",
                center,
                center + paint.getTextSize() / 3,
                paint
        );

        paint.setStyle(Paint.Style.STROKE);
    }
}
