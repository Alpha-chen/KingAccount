package com.account.king.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.R;

/**
 * 类别
 * Created by King
 * on 2017/3/22.
 */

public class TypeRecycleAdapter extends RecyclerView.Adapter<TypeRecycleAdapter.TypeViewHoler> {

    private Context mContext;
    private String[] arrys = null;

    private int selectPosition = 0;
    private onRecyclerViewItemClickListener itemClickListener;

    public TypeRecycleAdapter(String[] arrys, Context context) {
        this.arrys = arrys;
        mContext = context;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setItemClickListener(onRecyclerViewItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public TypeViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.type_recycle_item, null);
        TypeViewHoler typeViewHoler = new TypeViewHoler(view);
        return typeViewHoler;
    }

    @Override
    public void onBindViewHolder(final TypeViewHoler holder, final int position) {
        if (selectPosition == position) {
            holder.typeSelect.setVisibility(View.VISIBLE);
        } else {
            holder.typeSelect.setVisibility(View.GONE);
        }
        holder.type.setText(arrys[position]);
        holder.account_type.setTag(position);
        holder.account_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    selectPosition = position;
                    if (holder.typeSelect.getVisibility() == View.VISIBLE) {
                        holder.typeSelect.setVisibility(View.GONE);
                    } else {
                        holder.typeSelect.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                    itemClickListener.onItemClick((Integer) v.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrys.length;
    }

    public class TypeViewHoler extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView typeSelect;
        private RelativeLayout account_type;

        public TypeViewHoler(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            typeSelect = (TextView) itemView.findViewById(R.id.type_item_select);
            account_type = (RelativeLayout) itemView.findViewById(R.id.account_type);
        }

    }

    /**
     * item点击事件
     */
    public interface onRecyclerViewItemClickListener {
        void onItemClick(int typePosition);
    }
}
