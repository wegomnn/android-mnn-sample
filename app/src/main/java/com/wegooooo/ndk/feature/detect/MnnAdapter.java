package com.wegooooo.ndk.feature.detect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.wegooooo.ndk.R;
import com.wegooooo.ndk.feature.search.SearchActivity;
import com.wegooooo.ndk.utils.Utils;

/**
 * <pre>
 *     Copyright (C), 2014-2022, 深圳市微购科技有限公司
 *     Author : tangjianye
 *     Email  : jianye.tang@foxmail.com
 *     Time   : 2022/8/23 15:16
 *     Desc   : 列表 item 适配器
 *     Version: 1.0
 * </pre>
 */
public class MnnAdapter extends RecyclerView.Adapter<MnnAdapter.ViewHolder> {

    //需要展示的数据保存在这里
    private Context mContext;
    private List<String> mData;

    public MnnAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mnn, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String src = Utils.fullPath(mContext, mData.get(position));
        Bitmap bitmap = Utils.getLocalBitmap(src);
        if (bitmap != null) {
            holder.itemImage.setOnClickListener(view -> {
                Log.i("wegomnn", "position = " + position);
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("size", mData.size());
                mContext.startActivity(intent);
            });
            holder.itemImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.iv_mnn);
        }
    }
}

