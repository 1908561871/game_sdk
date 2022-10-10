package com.zhibo8.game.sdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZBOrderInfo implements Parcelable {

    private String order;
    private String price;


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order);
        dest.writeString(this.price);
    }

    public void readFromParcel(Parcel source) {
        this.order = source.readString();
        this.price = source.readString();
    }

    public ZBOrderInfo() {
    }

    protected ZBOrderInfo(Parcel in) {
        this.order = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<ZBOrderInfo> CREATOR = new Parcelable.Creator<ZBOrderInfo>() {
        @Override
        public ZBOrderInfo createFromParcel(Parcel source) {
            return new ZBOrderInfo(source);
        }

        @Override
        public ZBOrderInfo[] newArray(int size) {
            return new ZBOrderInfo[size];
        }
    };
}
