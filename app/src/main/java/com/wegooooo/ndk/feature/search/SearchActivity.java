package com.wegooooo.ndk.feature.search;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.wegooooo.mnn.WegoMnn;
import com.wegooooo.ndk.databinding.ActivitySearchBinding;
import com.wegooooo.ndk.feature.detect.MnnAdapter;
import com.wegooooo.ndk.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding mViewBinding;
    private ProgressDialog progressDialog;
    private int mSize = 1000;
    private int mPosition = 0;
    private MnnAdapter mMnnAdapter;
    private long mParseTime = 0;
    private long mSearchTime = 0;
    private List<Integer> mResult = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initData();
        initView();

        progressDialog = ProgressDialog.show(this, "", "图片搜索中...");
        new Thread(() -> {
            parse();
        }).start();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mPosition = bundle.getInt("position");
        mSize = bundle.getInt("size");
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
        String src = Utils.getImagePath(this, String.format(Locale.getDefault(), "ukbench%05d.th.jpg", mPosition));
        Bitmap bitmap = Utils.getLocalBitmap(src);
        mViewBinding.ivTarget.setImageBitmap(bitmap);
    }

    private void resultView() {
        // 时间结果
        final StringBuilder sb = new StringBuilder();
        sb.append("图片搜索费时：" + mSearchTime + "毫秒");
        mViewBinding.tvResult.setText(sb);

        // 图片结果
        // 设置布局管理器(LayoutManager)
        mViewBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        // 设置适配器(Adapter)和数据
        List<String> data = getListStrings();
        mMnnAdapter = new MnnAdapter(this, data);
        mViewBinding.recyclerView.setAdapter(mMnnAdapter);
        // 添加分割线
        // mViewBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @NonNull
    private List<String> getListStrings() {
        List<String> data = new ArrayList<>();
        for (Integer item : mResult) {
            data.add(String.format(Locale.getDefault(), "ukbench%05d.th.jpg", item));
        }
        return data;
    }

    private void parse() {
        float[] target = parseImage(String.format(Locale.getDefault(), "ukbench%05d.th.jpg", mPosition));
        search(target);
    }

    private float[] parseImage(String filename) {
        String imagePath = Utils.getImagePath(this, filename);
        return WegoMnn.detect(imagePath);
    }

    private void search(float[] target) {
        long now = System.currentTimeMillis();
        float[] faiss = WegoMnn.vectorSearch(this, target, 10);
        long end = System.currentTimeMillis();
        mSearchTime = end - now;
        Log.i("wegomnn", "mnn:" + (mSearchTime / 1000) + "秒");

        for (float item : faiss) {
            mResult.add((int) item);
        }

        runOnUiThread(() -> {
            progressDialog.dismiss();
            resultView();
        });
    }
}