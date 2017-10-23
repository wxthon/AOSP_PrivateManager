package com.robot;

import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wxthon on 10/23/17.
 */

public class RobotComponent implements Parcelable {

    private String mName;
    private int mLayoutID;

    RobotComponent(String name, int layoudID) {
        mName = name;
        mLayoutID = layoudID;
    }

    protected RobotComponent(Parcel in) {
        mName = in.readString();
        mLayoutID = in.readInt();
    }

    public static final Creator<RobotComponent> CREATOR = new Creator<RobotComponent>() {
        @Override
        public RobotComponent createFromParcel(Parcel in) {
            return new RobotComponent(in);
        }

        @Override
        public RobotComponent[] newArray(int size) {
            return new RobotComponent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mLayoutID);
    }

    public String getName() {
        return mName;
    }

    public int getLayoutID() {
        return mLayoutID;
    }
}
