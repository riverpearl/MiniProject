package com.tacademy.miniproject.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tacademy.miniproject.R;
import com.tacademy.miniproject.data.ContentData;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tacademy on 2016-08-16.
 */
public class ContentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_content)
    TextView textView;

    @BindView(R.id.image_content)
    ImageView imageView;

    public ContentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setContent(ContentData data) {
        textView.setText(data.getContent());
        Glide.with(imageView.getContext())
                .load(data.getImageUrl())
                .into(imageView);
    }
}
