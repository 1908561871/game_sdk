package com.zhibo8.game.sdk.pay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhibo8.game.sdk.R;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author : ZhangWeiBo
 * date : 2022/09/28
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8PayGoodsAdapter extends RecyclerView.Adapter<ZB8PayGoodsAdapter.ViewHolder> {

    private JSONArray jsonArray;

    public ZB8PayGoodsAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.zb8_adapter_pay_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            ViewHolder v = holder;
            JSONObject jsonObject = jsonArray.optJSONObject(position);
            String key = jsonObject.optString("key");
            String value = jsonObject.optString("value");
            v.tvTitle.setText(key + value);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
