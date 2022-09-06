package com.wegooooo.ndk.feature.detect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wegooooo.mnn.WegoMnn;
import com.wegooooo.ndk.R;
import com.wegooooo.ndk.databinding.ActivityMnnBinding;
import com.wegooooo.ndk.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BatchParseActivity extends AppCompatActivity {
    private ActivityMnnBinding mViewBinding;
    private ProgressDialog progressDialog;
    private int mSize = 1000;
    private MnnAdapter mMnnAdapter;
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
        mViewBinding.spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] array = getResources().getStringArray(R.array.size);
                mSize = Integer.parseInt(array[i]);
                List<String> data = getListStrings();
                mMnnAdapter = new MnnAdapter(BatchParseActivity.this, data);
                mViewBinding.recyclerView.setAdapter(mMnnAdapter);
                Log.i("wegomnn", "spinnerSize index = " + i + " ;size = " + mSize);

                asyncParse();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mViewBinding.btnAppend.setVisibility(View.GONE);

        // 设置布局管理器(LayoutManager)
        mViewBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @NonNull
    private List<String> getListStrings() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < mSize; i++) {
            data.add(String.format(Locale.getDefault(), "ukbench%05d.jpg", i));
        }
        return data;
    }

    private void parse() {
        int size = WegoMnn.VECTOR_SIZE;      // dimension 5
        int nb = mSize;                       // database size 100

        float[] collection = new float[size * nb];

        long now = System.currentTimeMillis();
        for (int i = 0; i < nb; i++) {
            float[] array = parseImage(String.format(Locale.getDefault(), "ukbench%05d.jpg", i));
            for (int j = 0; j < array.length; j++) {
                collection[i * size + j] = array[j];
            }
        }
        long end = System.currentTimeMillis();
        mParseTime = end - now;
        Log.i("wegomnn", "mnn:" + (mParseTime / 1000) + "秒");

        // 清空之前的图片向量
        WegoMnn.vectorClear(this);
        // 每次新追加图片向量
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
        String imagePath = Utils.getImagePath(this, filename);
        return WegoMnn.detect(imagePath);
    }

    private void resultView() {
        // 时间结果
        final StringBuilder sb = new StringBuilder();
        sb.append("图片解析费时：" + mParseTime / 1000 + "秒，" + "向量保存费时：" + mSaveTime + "毫秒");
        sb.append("\n");
        sb.append("请点击目标图片，会直接触发搜索功能");
        mViewBinding.tvResult.setText(sb);
    }
}