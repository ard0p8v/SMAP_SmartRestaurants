package com.sample.smartrestaurants.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.smartrestaurants.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context ctx, String name, Double evaluation, String image) {
        TextView mNameTv = mView.findViewById(R.id.rNameTv);
        TextView mEvaluationTv = mView.findViewById(R.id.rEvaluationTv);
        ImageView mImageIv = mView.findViewById(R.id.rImageView);

        mNameTv.setText(name);
        mEvaluationTv.setText(evaluation.toString());
        Picasso.get().load(image).into(mImageIv);
    }
}
