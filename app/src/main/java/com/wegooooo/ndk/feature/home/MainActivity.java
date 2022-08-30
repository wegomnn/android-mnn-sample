package com.wegooooo.ndk.feature.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wegooooo.mnn.WegoMnn;
import com.wegooooo.ndk.databinding.ActivityMainBinding;
import com.wegooooo.ndk.feature.detect.AppendParseActivity;
import com.wegooooo.ndk.feature.detect.BatchParseActivity;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mViewBinding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
    }

    private void initView() {
        File dir = new File(this.getCacheDir() + File.separator + "thumbnails");
        boolean isDirExists = dir.exists();
        Log.i("wegomnn", isDirExists + "");

        if (isDirExists) {
            mViewBinding.btnUnzip.setText("图片包已解压");
        }

        mViewBinding.btnUnzip.setOnClickListener(view -> {
            if (isDirExists) {
                Toast.makeText(this, "图片包已解压", Toast.LENGTH_SHORT).show();
            } else {
                preImage();
            }
        });

        mViewBinding.btnBatch.setOnClickListener(view -> {
            if (isDirExists) {
                gotoBatchPage();
            } else {
                Toast.makeText(this, "请先解压图片包", Toast.LENGTH_SHORT).show();
            }
        });

        mViewBinding.btnAppend.setOnClickListener(view -> {
            if (isDirExists) {
                gotoAppendPage();
            } else {
                Toast.makeText(this, "请先解压图片包", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoBatchPage() {
        Intent intent = new Intent(this, BatchParseActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private void gotoAppendPage() {
        Intent intent = new Intent(this, AppendParseActivity.class);
        MainActivity.this.startActivity(intent);
    }

    private void preImage() {
        // Toast.makeText(this, "加载中...", Toast.LENGTH_SHORT).show();
        progressDialog = ProgressDialog.show(this, "", "图片包解压中...");
        new Thread(() -> {
            try {
                WegoMnn.copyAssetDirToFiles(this, "thumbnails", true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                progressDialog.dismiss();
                Toast.makeText(this, "图片包解压成功", Toast.LENGTH_SHORT).show();
                initView();
            });
        }).start();
    }
}