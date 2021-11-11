package com.example.wfhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private boolean startOverlay() {
        if (!Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            Toast.makeText(this, "需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void initView(){
        mStartBtn = findViewById(R.id.StartButton);
        mStartBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (startOverlay()){
                    mStartBtn.setEnabled(false);
                    mStartBtn.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(MainActivity.this, WFService.class);
                    startService(intent);
                }
            }
        });
    }
}