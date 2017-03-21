package com.account.king.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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

    public TypeRecycleAdapter(String[] arrys, Context context) {
        this.arrys = arrys;
        mContext = context;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public TypeViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.type_recycle_item, null);
        TypeViewHoler typeViewHoler = new TypeViewHoler(view);
        return typeViewHoler;
    }

    @Override
    public void onBindViewHolder(TypeViewHoler holder, int position) {
        if (selectPosition == position) {
            holder.typeSelect.setVisibility(View.VISIBLE);
        } else {
            holder.typeSelect.setVisibility(View.GONE);
        }
        holder.type.setText(arrys[position]);
    }

    @Override
    public int getItemCount() {
        return arrys.length;
    }

    public class TypeViewHoler extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView typeSelect;

        public TypeViewHoler(View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            typeSelect = (TextView) itemView.findViewById(R.id.type_recycle);
        }

    }

}
