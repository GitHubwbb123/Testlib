package com.wxb.testlib;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wxb.zxing.Activity.CaptureActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

public class MainActivity extends AppCompatActivity {
Button scanZcode;
private static final int REQUEST_SCAN = 0;
private Context mcontext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext=this;
        scanZcode=(Button)findViewById(R.id.scan);
        scanZcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(AndPermission.hasPermission(mcontext,Manifest.permission.CAMERA)){
                    startActivityForResult(new Intent(MainActivity.this,CaptureActivity.class),REQUEST_SCAN );
                }
                else{
                        AndPermission.with(MainActivity.this)
                                .permission(Manifest.permission.CAMERA)
                                .requestCode(100)
                                .send();
                    }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_SCAN&&resultCode==RESULT_OK){
            Toast.makeText(mcontext,data.getStringExtra("barCode"),Toast.LENGTH_LONG).show();
        }
    }
//权限结果，用And来接收回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(MainActivity.this,requestCode,permissions,grantResults);
    }
//注释回调
    @PermissionYes(100)
    private  void getSucceed(List<String> grantedPermission){
        startActivityForResult(new Intent(MainActivity.this,CaptureActivity.class),REQUEST_SCAN );
    }
    @PermissionNo(100)
    private  void getDefeated(List<String> deniedPermission){
       Toast.makeText(this,"您拒绝可权限",Toast.LENGTH_SHORT).show();
    }
}


