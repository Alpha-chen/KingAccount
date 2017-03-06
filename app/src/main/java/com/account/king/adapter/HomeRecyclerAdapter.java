package com.account.king.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.account.king.R;
import com.account.king.node.KingAccountNode;
import com.account.king.util.ArithUtil;
import com.account.king.util.CalendarUtil;
import com.account.king.util.DensityUtils;

import java.util.ArrayList;

/**
 *
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<KingAccountNode> bookNodes;
    private SparseBooleanArray booleanArray = new SparseBooleanArray();

    private RelativeLayout.LayoutParams layoutParams;
    private int leftMargin = 60;

    public HomeRecyclerAdapter(Context context) {
        this.context = context;
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 2));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        leftMargin = DensityUtils.dp2px(context, leftMargin);
    }

    public void setParams(ArrayList<KingAccountNode> bookNodes, SparseBooleanArray booleanArray) {
        this.bookNodes = bookNodes;
        this.booleanArray = booleanArray;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_home_recycler, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final KingAccountNode bookNode = bookNodes.get(position);
        int moneyType = bookNode.getAccount_type();
        if (moneyType == KingAccountNode.MONEY_OUT) {
            holder.money.setText("-" + ArithUtil.mul(bookNode.getPrice(), bookNode.getCount(), 2));
            holder.money.setTextColor(ContextCompat.getColor(context, R.color.out_come));
        } else {
            holder.money.setText("+" + ArithUtil.mul(bookNode.getPrice(), bookNode.getCount(), 2));
            holder.money.setTextColor(ContextCompat.getColor(context, R.color.int_come));
        }
        //类别
       /* if (typeNodes != null) {
            if (bookNode.getTypeNode() == null) {
                String md5 = bookNode.getIdentifier();
                for (AccountTypeNode typeNode : typeNodes) {
                    if (typeNode.getMoneyType() == moneyType && typeNode.getIdentifier().equals(md5)) {
                        bookNode.setTypeNode(typeNode);
                        break;
                    }
                }
            }
            if (bookNode.getTypeNode() != null) {
                int typeIcon = bookNode.getTypeNode().getTypeIcon();
                holder.typeIcon.setImageResource(ImgColorResArray.getResIcon(moneyType, typeIcon));
                holder.typeName.setText(bookNode.getTypeNode().getTypeName());
            }
        }*/
        holder.typeNote.setText(bookNode.getAccount_type()+" dasd");
        holder.otherLin.setVisibility(View.VISIBLE);
 /*       if (TextUtils.isEmpty(bookNode.getAttachment().getAttachment_path())) {
            holder.hasPhoto.setVisibility(View.GONE);
            if (TextUtils.isEmpty(bookNode.getAttachment().getContent())) {
                holder.otherLin.setVisibility(View.GONE);
            }
        } else {
            holder.hasPhoto.setVisibility(View.VISIBLE);
        }*/
        long timeMilis = bookNode.getYmd_hms();
        int date = CalendarUtil.timeMilis2Date(timeMilis);
 /*       if (booleanArray.get(position, false)) {
            holder.day.setVisibility(View.VISIBLE);
            holder.month.setVisibility(View.VISIBLE);
            holder.day.setText(CalendarUtil.PadZero(CalendarUtil.getDay(date)));
            holder.month.setText(CalendarUtil.getMonth(date) + "月");
            layoutParams.leftMargin = 0;
            holder.dashLine.setLayoutParams(layoutParams);
        } else {
            holder.day.setVisibility(View.GONE);
            holder.month.setVisibility(View.GONE);
            layoutParams.leftMargin = leftMargin;
            holder.dashLine.setLayoutParams(layoutParams);
        }*/
        holder.day.setVisibility(View.VISIBLE);
        holder.month.setVisibility(View.VISIBLE);
        holder.day.setText(CalendarUtil.PadZero(CalendarUtil.getDay(date)));
        holder.month.setText(CalendarUtil.getMonth(date) + "月");
        layoutParams.leftMargin = 0;
        holder.dashLine.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return bookNodes != null ? bookNodes.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView typeName;
        ImageView typeIcon;
        TextView money;
        TextView day;
        TextView month;
        View dashLine;
        ImageView hasPhoto;
        TextView typeNote;
        LinearLayout otherLin;

        public MyViewHolder(View view) {
            super(view);
            typeName = (TextView) view.findViewById(R.id.item_type_name);
            typeIcon = (ImageView) view.findViewById(R.id.item_type_icon);
            money = (TextView) view.findViewById(R.id.item_money);
            day = (TextView) view.findViewById(R.id.item_day);
            month = (TextView) view.findViewById(R.id.item_month);
            dashLine = view.findViewById(R.id.item_dash_line);
            hasPhoto = (ImageView) view.findViewById(R.id.item_has_photo);
            typeNote = (TextView) view.findViewById(R.id.item_type_note);
            otherLin = (LinearLayout) view.findViewById(R.id.item_type_other);
        }
    }
}
