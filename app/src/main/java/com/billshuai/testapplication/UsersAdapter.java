package com.billshuai.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Houge on 2017/4/11.
 */

public class UsersAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater inflater;

    private List<String> itemsUser;
    private List<String> itemsPass;

    private int selectedPosition = -1;

    private OnDeleteListener onDeleteListener;

    public UsersAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setItemsUser(List<String> itemsUser) {
        this.itemsUser = itemsUser;
    }
    public void setItemsPass(List<String> itemsPass) {
        this.itemsPass = itemsPass;
    }

    @Override
    public int getCount() {
        return itemsUser.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = (View) inflater.inflate(R.layout.items, parent, false);
            holder.item_text = (TextView) convertView.findViewById(R.id.item);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
            holder.iv_delete.setTag(position);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /**
         * 点击事件
         */
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posi = Integer.parseInt(v.getTag().toString());
                String s= itemsUser.get(posi).toString();
                itemsUser.remove(posi);
                itemsPass.remove(posi);
                notifyDataSetChanged();
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete(s);
                }
            }
        });
        holder.item_text.setText(itemsUser.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView item_text;
        ImageView iv_delete;
    }

    //定义一个接口
    public interface OnDeleteListener {
        public void onDelete(String name);
    }

    /**
     * 自定义控件的自定义事件
     *
     * @param onDeleteListener 接口类型
     */
    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }
}
