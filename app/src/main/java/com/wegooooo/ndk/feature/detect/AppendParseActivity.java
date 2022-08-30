package com.wegooooo.ndk.feature.detect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wegooooo.mnn.WegoMnn;
import com.wegooooo.ndk.databinding.ActivityMnnBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppendParseActivity extends AppCompatActivity {
    private static final int SIZE = 100;
    private ActivityMnnBinding mViewBinding;
    private ProgressDialog progressDialog;
    private int mIndex = 0;
    private MnnAdapter mMnnAdapter;
    private List<String> mData = new ArrayList<>(); // 列表显示的图片数据
    private List<String> mAppendImages = new ArrayList<>(); // 每次追加的图片数据
    private long mParseTime = 0;
    private long mSaveTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivityMnnBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        WegoMnn.vectorClear(this);
        WegoMnn.create(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WegoMnn.release();
    }

    private void asyncParse() {
        progressDialog = ProgressDialog.show(this, "", "图片向量提取中...");
        new Thread(() -> {
            parse();
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mViewBinding.rlSpinner.setVisibility(View.GONE);

        mViewBinding.btnAppend.setOnClickListener(view -> {
            List<String> data = getAppendImages();
            mAppendImages.clear();
            mAppendImages.addAll(data);

            mData.addAll(data);
            mMnnAdapter.notifyDataSetChanged();

            mViewBinding.btnAppend.setText("点击追加100张图片/当前" + mData.size() + "张图片");
            mIndex += 1;

            asyncParse();
        });

        // 设置布局管理器(LayoutManager)
        mViewBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mMnnAdapter = new MnnAdapter(AppendParseActivity.this, mData);
        mViewBinding.recyclerView.setAdapter(mMnnAdapter);

        // 模拟点击事件
        mViewBinding.btnAppend.performClick();
    }

    @NonNull
    private List<String> getAppendImages() {
        List<String> data = new ArrayList<>();
        for (int i = mIndex * SIZE; i < (mIndex + 1) * SIZE; i++) {
            data.add(String.format("ukbench%05d.th.jpg", i));
        }
        return data;
    }

    private void parse() {
        int size = WegoMnn.VECTOR_SIZE;
        int nb = SIZE;

        float[] collection = new float[size * nb];

        long now = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            float[] array = parseImage("thumbnails" + File.separator + mAppendImages.get(i));
            for (int j = 0; j < array.length; j++) {
                collection[i * size + j] = array[j];
            }
        }
        long end = System.currentTimeMillis();
        mParseTime = end - now;
        Log.i("wegomnn", "mnn:" + (mParseTime / 1000) + "秒");

        // 图片向量添加
        int[] ids = WegoMnn.vectorInsert(this, collection);
        if (ids != null) {
            Log.i("wegomnn", "ids:" + ids[0] + " - " + ids[1]);
        }

        long end2 = System.currentTimeMillis();
        mSaveTime = end2 - end;
        Log.i("wegomnn", "mnn:" + (mSaveTime / 1000) + "秒");

        runOnUiThread(() -> {
            progressDialog.dismiss();
            resultView();
        });
    }

    private float[] parseImage(String filename) {
        String imagePath = this.getCacheDir() + File.separator + filename;
        float[] result = WegoMnn.detect(imagePath);
        return result;
    }

    private void resultView() {
        // 时间结果
        final StringBuilder sb = new StringBuilder();
        sb.append("图片解析费时：" + mParseTime + "毫秒，" + "向量保存费时：" + mSaveTime + "毫秒");
        sb.append("\n");
        sb.append("请点击目标图片，会直接触发搜索功能");
        mViewBinding.tvResult.setText(sb);
    }
}