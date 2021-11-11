package com.example.wfhelper;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class WFService extends Service {

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Button mBtn;
    private View displayView;

    @Override
    public void onCreate() {
        super.onCreate();
        mParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        initView();
        Toast.makeText(this, "WFHelper 已开启", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.gravity = Gravity.START | Gravity.TOP;
        mParams.x = 300;
        mParams.y = 300;
        mParams.width = WFUtils.SMALL_SIZE_WIDTH;
        mParams.height = WFUtils.SMALL_SIZE_HIGH;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        displayView = layoutInflater.inflate(R.layout.float_window, null);
        displayView.setOnTouchListener(new FloatingOnTouchListener());

        mBtn = displayView.findViewById(R.id.button);
        mBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(WFService.this, "按了一下按钮", Toast.LENGTH_SHORT).show();
            }
        });

        mWindowManager.addView(displayView,mParams);
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x=0;
        private int y=0;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mParams.x += movedX;
                    mParams.y += movedY;

                    // 更新悬浮窗控件布局
                    mWindowManager.updateViewLayout(view, mParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
