package com.robot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by huayahng on 9/8/16.
 */
public class AppHolder extends RecyclerView.ViewHolder {

    public ImageView mIcon;
    public TextView mName;
    public Switch mSwitch;

    public AppHolder(View itemView) {
        super(itemView);
        mIcon = (ImageView) itemView.findViewById(R.id.id_app_icon);
        mName = (TextView) itemView.findViewById(R.id.id_app_name);
        mSwitch = (Switch) itemView.findViewById(R.id.id_app_switch);
    }
}
