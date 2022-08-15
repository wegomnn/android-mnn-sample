package com.wegooooo.ndk;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wegooooo.mnn.MnnInfo;
import com.wegooooo.mnn.WegoMnn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MnnActivity extends AppCompatActivity {
    private ImageView mImageView;
    private TextView mTextView;
    private TextView mResultText;
    private TextView mTimeText;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnn);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mImageView = findViewById(R.id.imageView);
        mTextView = findViewById(R.id.textView);
        mResultText = findViewById(R.id.editText);
        mTimeText = findViewById(R.id.timeText);

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        // show image
        AssetManager am = getAssets();
        try {
            final InputStream picStream = am.open("cat.jpg");
            mBitmap = BitmapFactory.decodeStream(picStream);
            picStream.close();
            mImageView.setImageBitmap(mBitmap);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = MnnActivity.this.getFilesDir() + File.separator;
                String imagePath = path + "/cat.jpg";

                try {
                    WegoMnn.copyAssetFileToFiles(MnnActivity.this, "cat.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MnnInfo[] result = WegoMnn.decode(MnnActivity.this, imagePath);

                final StringBuilder sb = new StringBuilder();
                for (MnnInfo item : result) {
                    sb.append(String.format("%e", item.x0));
                    sb.append("\n");
                }

                mResultText.setText(sb);
            }
        });
    }
}