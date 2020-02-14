package com.example.notebook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.notebook.Adapter.base.BaseAdapter;
import com.example.notebook.Adapter.base.IAdapterItem;
import com.example.notebook.Adapter.base.OnItemClickListener;
import com.example.notebook.Adapter.base.OnItemLongClickListener;
import com.example.notebook.Entity.Group;
import com.example.notebook.R;
import com.example.notebook.Util.CommonUtil;

import java.util.List;

public class GroupAdapter extends BaseAdapter<Group, GroupAdapter.GroupItemView> {

    public GroupAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    protected GroupItemView inflateView(Context context, ViewGroup parent) {
        return new GroupItemView(context);
    }

    public class GroupItemView extends LinearLayout implements IAdapterItem<Group>{
        private CardView card_view_group;
        private TextView tv_list_name;
        private TextView tv_list_time;

        public GroupItemView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.list_group_item,this,true);
            card_view_group = view.findViewById(R.id.card_view_group);
            tv_list_name = view.findViewById(R.id.tv_group_name);
            tv_list_time = view.findViewById(R.id.tv_group_create_time);

            adjustBodyLayoutParams();
        }

        @Override
        public void bindDataToView(Group group, int position) {
            tv_list_name.setText(group.getName());
            tv_list_time.setText(group.getCreateTime());
        }

        private void adjustBodyLayoutParams(){
            ViewGroup.LayoutParams params = card_view_group.getLayoutParams();
            params.width = (CommonUtil.getScreenWidth(mContext)*95/100);
            params.height = params.width/5;
            card_view_group.setLayoutParams(params);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }

    }


}
