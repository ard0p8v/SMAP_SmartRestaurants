package com.sample.smartrestaurants.holder;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.smartrestaurants.DetailActivity;
import com.sample.smartrestaurants.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    String name;
    public View mView;
    public ClipData.Item currentItem;
    private CardView cardView;

    public ViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);

        mView = itemView;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((TextView)view.findViewById(R.id.rNameTv)).getText().toString();
                Intent i = new Intent(view.getContext(), DetailActivity.class);
                i.putExtra("rNameTv", name);
                mView.getContext().startActivity(i);
            }
        });
    }

    public void setDetails(Context ctx, String name, String menuName, Double price, String image) {
        TextView mNameTv = mView.findViewById(R.id.rNameTv);
        TextView mMenuNameTv = mView.findViewById(R.id.mMenuNameTv);
        TextView mPriceTv = mView.findViewById(R.id.mPriceTv);
        ImageView mImageIv = mView.findViewById(R.id.imageView);

        mNameTv.setText(name);
        mMenuNameTv.setText(menuName);
        mPriceTv.setText(price.toString() + " Kč");
        Picasso.get().load(image).into(mImageIv);
    }
}
