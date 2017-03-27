package com.account.king.presenter.contract;

import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;


import com.account.king.node.KingAccountNode;

import java.util.List;

/**
 * @author king
 */
public class AddAccountContract {


    public interface IAddAcountView {

        public void selectCostType(boolean isSelectType);

        public void selectIncomeType(boolean isSelectType);

        public void setNoteRes(int Res, String note);

        //        public void selectTypeNode(AccountTypeNode node);
        public void setDateText(String date);

        public void jitterMoney(PropertyValuesHolder pvhRotate);

        public void showKeyBoard();

        void showType(String type);
    }

    public interface IAddAcountPresenter {

        void loadType(int accountType, int type);

        public List<KingAccountNode> getTypeNodes(Context context);

        public boolean validation(Context context, KingAccountNode bookNode, String price, String count);

        public boolean insertBookNode(Context context, KingAccountNode bookNode);

        public boolean updateBookNode(Context context, KingAccountNode bookNode);

        public boolean saveBookNode(Context context, boolean isEdit, KingAccountNode bookNode);

        public void loadImg(Context context, String path, ImageView selectPhotoImg);

        public void loadNote(Context context, String note);

        public void selectDate(Context context, final KingAccountNode bookNode);

        public void selectPhoto(Activity context, KingAccountNode bookNode);

        public void selectType(Activity context, KingAccountNode bookNode);

        public void clickWriterNote(Activity context, final KingAccountNode bookNode);

        public void onItemClick(Context context, RecyclerView.ViewHolder vh, int type,
                                List<KingAccountNode> typeNodes, ImageView moveTypeIcon, ImageView icon);
//        public void sortTypeNodes(Context context, List<AccountTypeNode> costTypeNodes, List<AccountTypeNode> incomeTypeNodes);
    }
}
