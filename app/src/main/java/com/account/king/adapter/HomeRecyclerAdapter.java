package com.account.king.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.account.king.util.LogUtil;
import com.account.king.util.TypeUtil;
import com.account.king.util.glide.GlideUtil;

import java.util.ArrayList;

/**
 *
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {

    private String TAG = "HomeRecyclerAdapter";
    private Context context;
    private ArrayList<KingAccountNode> bookNodes;

    private RelativeLayout.LayoutParams layoutParams;
    private int leftMargin = 60;
    private OnItemClickListener mClickListener;

    public HomeRecyclerAdapter(Context context) {
        this.context = context;
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(context, 2));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        leftMargin = DensityUtils.dp2px(context, leftMargin);
    }

    public void setParams(ArrayList<KingAccountNode> bookNodes) {
        if (null == bookNodes || bookNodes.size() == 0) {
            this.bookNodes = new ArrayList<>();
        }else {
            this.bookNodes = bookNodes;
        }
        notifyDataSetChanged();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_home_recycler, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (null == bookNodes || bookNodes.size() == 0) {
            return;
        }
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
        String[] arrays = null;
        if (bookNode != null) {
            holder.typeName.setVisibility(View.VISIBLE);
            holder.typeName.setText(TypeUtil.getType(context, bookNode.getAccount_type(), bookNode.getType()));
        }

        holder.otherLin.setVisibility(View.VISIBLE);
        LogUtil.d(TAG, "onBindViewHolder->bookNode.getAttachment()=" + bookNode.getAttachment());
        if (null == bookNode.getAttachment()) {
            holder.hasPhoto.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(bookNode.getAttachment().getAttachment_path())) {
                holder.hasPhoto.setVisibility(View.GONE);
                if (TextUtils.isEmpty(bookNode.getAttachment().getContent())) {
                    holder.typeNote.setVisibility(View.GONE);
                }
            } else {
                holder.hasPhoto.setVisibility(View.VISIBLE);
                GlideUtil.loadRound(context, bookNode.getAttachment().getAttachment_path(), holder.hasPhoto);
                holder.typeNote.setVisibility(View.VISIBLE);
                holder.typeNote.setText(bookNode.getAttachment().getContent());
            }
        }

        long timeMilis = bookNode.getYmd_hms();
        int date = CalendarUtil.timeMilis2Date(timeMilis);

        holder.day.setVisibility(View.VISIBLE);
        holder.month.setVisibility(View.VISIBLE);
        holder.day.setText(CalendarUtil.PadZero(CalendarUtil.getDay(date)));
        holder.month.setText(CalendarUtil.getMonth(date) + "月");
        layoutParams.leftMargin = 0;
        holder.dashLine.setLayoutParams(layoutParams);
        holder.home_item.setTag(position);

        holder.home_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "setOnClickListener");
                mClickListener.onItemClick(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookNodes != null ? bookNodes.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView typeName;
        TextView money;
        TextView day;
        TextView month;
        View dashLine;
        ImageView hasPhoto;
        TextView typeNote;
        LinearLayout otherLin;
        private RelativeLayout home_item;

        public MyViewHolder(View view) {
            super(view);
            typeName = (TextView) view.findViewById(R.id.item_type_name);
            money = (TextView) view.findViewById(R.id.item_money);
            day = (TextView) view.findViewById(R.id.item_day);
            month = (TextView) view.findViewById(R.id.item_month);
            dashLine = view.findViewById(R.id.item_dash_line);
            hasPhoto = (ImageView) view.findViewById(R.id.item_has_photo);
            typeNote = (TextView) view.findViewById(R.id.item_type_note);
            otherLin = (LinearLayout) view.findViewById(R.id.account_data);
            home_item = (RelativeLayout) view.findViewById(R.id.home_item);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int position);
    }
}
