package com.account.king.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.R;
import com.account.king.node.TypeNode;

import java.util.ArrayList;

/**
 * 类别
 * Created by King
 * on 2017/3/22.
 */

public class TypeRecycleAdapter extends RecyclerView.Adapter<TypeRecycleAdapter.TypeViewHoler> {

    private Context mContext;
    private ArrayList<TypeNode> arrys = new ArrayList<>();

    private int selectPosition = 0;
    private onRecyclerViewItemClickListener itemClickListener;

    public TypeRecycleAdapter(Context context) {
        mContext = context;
    }

    public void setArrys(ArrayList<TypeNode> arrys) {
        this.arrys = arrys;
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
        holder.type.setText(arrys.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return null == arrys ? 0 : arrys.size();
    }

    public class TypeViewHoler extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView typeSelect;
        private RelativeLayout account_type;

        public TypeViewHoler(final View itemView) {
            super(itemView);
            type = (TextView) itemView.findViewById(R.id.type);
            typeSelect = (TextView) itemView.findViewById(R.id.type_item_select);
            account_type = (RelativeLayout) itemView.findViewById(R.id.account_type);
            account_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != itemClickListener) {
                        selectPosition = getLayoutPosition();
                        if (typeSelect.getVisibility() == View.VISIBLE) {
                            typeSelect.setVisibility(View.GONE);
                        } else {
                            typeSelect.setVisibility(View.VISIBLE);
                        }
                        notifyDataSetChanged();
                        itemClickListener.onItemClick(selectPosition);
                    }
                }
            });
            account_type.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("是否删除这个类别");
                    alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            arrys.remove(getLayoutPosition());
                            selectPosition = 0;
                            notifyDataSetChanged();
                            itemClickListener.onItemLongClick(getLayoutPosition());
                            dialog.dismiss();
                        }
                    });
                    Dialog dialog = alertDialog.create();
                    dialog.show();
                    return false;
                }
            });
        }

    }

    /**
     * item点击事件
     */
    public interface onRecyclerViewItemClickListener {
        void onItemClick(int typePosition);

        void onItemLongClick(int typePosition);
    }
}
