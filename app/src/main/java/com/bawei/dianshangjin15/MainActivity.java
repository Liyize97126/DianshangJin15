package com.bawei.dianshangjin15;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AppCompatActivity {
    //定义
    private ImageView image01,image02;
    private ImageButton open_camera,open_gallery;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.hide();
        image01 = findViewById(R.id.image01);
        image02 = findViewById(R.id.image02);
        open_camera = findViewById(R.id.open_camera);
        open_gallery = findViewById(R.id.open_gallery);
        //权限申请
        //判断系统版本，高于API 23的需要手动获取权限
        if(Build.VERSION.SDK_INT >= 23){
            //checkSelfPermission方法就是检测是否有权限
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                //拥有权限
                Toast.makeText(MainActivity.this,"您已经获取应用所有权限，尽情使用吧！",Toast.LENGTH_LONG).show();
            }else {
                //没有权限，申请权限
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
            }
        }
        //打开相机
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });
        //打开相册（带裁剪）
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,200);
            }
        });
        //打开相册（不带裁剪）
        image01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,800);
            }
        });
        image02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1000);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    //处理权限返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断
        if(requestCode == 200 && grantResults.length == 3
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this,"权限获取成功！",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this,"权限获取失败，应用无法运行！",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    //结果回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(requestCode == 100){
                Bitmap bitmap = data.getParcelableExtra("data");
                image01.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image01.setImageBitmap(bitmap);
            } else if(requestCode == 200){
                Uri uri = data.getData();
                //裁剪
                Intent intent = new Intent("com.android.camera.action.CROP");
                //设置是否支持裁剪
                intent.putExtra("CROP",true);
                //设置图片的数据及类型
                intent.setDataAndType(uri,"image/*");
                //设置裁剪的宽高比
                intent.putExtra("aspectX",150);
                intent.putExtra("aspectY",150);
                //设置图片输出的大小
                intent.putExtra("outputX",150);
                intent.putExtra("outputY",150);
                //返回数据
                intent.putExtra("return-data",true);
                startActivityForResult(intent,500);
            } else if (requestCode == 500){
                Bitmap bitmap = data.getParcelableExtra("data");
                image02.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image02.setImageBitmap(bitmap);
            } else if(requestCode == 800){
                Uri uri = data.getData();
                image01.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image01.setImageURI(uri);
            } else if(requestCode == 1000){
                Uri uri = data.getData();
                image02.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image02.setImageURI(uri);
            }
        }
    }
}
