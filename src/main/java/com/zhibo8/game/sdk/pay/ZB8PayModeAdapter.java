package com.zhibo8.game.sdk.pay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zhibo8.game.sdk.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author : ZhangWeiBo
 * date : 2022/09/29
 * email : 1908561871@qq.com
 * description : TODO
 */
public class ZB8PayModeAdapter extends RecyclerView.Adapter<ZB8PayModeAdapter.ViewHolder> {

    private JSONArray jsonArray;
    private int index = 0;
    public ZB8PayModeAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public ZB8PayModeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ZB8PayModeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.zb8_adapter_pay_mode, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ZB8PayModeAdapter.ViewHolder holder, int position) {
        try {
            JSONObject style = jsonArray.optJSONObject(position);
            if (style != null){
                String type = style.optString("type");
                String name = style.optString("name");
                String logo = style.optString("logo");
                holder.tvPayTitle.setText(name);
                Glide.with(holder.ivPayIcon).load(logo).into(holder.ivPayIcon);
                holder.ivPayMode.setSelected(index == position);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = position;
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPayTitle;
        ImageView ivPayMode;
        ImageView ivPayIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPayTitle = itemView.findViewById(R.id.tv_pay_title);
            ivPayMode = itemView.findViewById(R.id.iv_pay_mode);
            ivPayIcon = itemView.findViewById(R.id.iv_pay_icon);
        }
    }

    public int getCurrentItem() {
        return index;
    }
}
