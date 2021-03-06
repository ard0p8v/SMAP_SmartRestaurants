package com.sample.smartrestaurants.services;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sample.smartrestaurants.R;

public class ViewHolderMenu extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolderMenu(View itemViewMenu) {
        super(itemViewMenu);

        mView = itemViewMenu;
    }

    public void setDetailsMenu(Context ctx, String menuName, Double price) {
        TextView mNameTv = mView.findViewById(R.id.rNameTv);
        TextView mPriceTv = mView.findViewById(R.id.rPriceTv);

        mNameTv.setText(menuName);
        mPriceTv.setText(price.toString() + " Kč");
    }

}
