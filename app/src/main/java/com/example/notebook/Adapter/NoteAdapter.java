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
import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.Util.CommonUtil;

import java.util.List;

public class NoteAdapter extends BaseAdapter<Note,NoteAdapter.NoteItemView>{
    public NoteAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    protected NoteItemView inflateView(Context context, ViewGroup parent) {
        return new NoteItemView(context);
    }

    public class NoteItemView extends LinearLayout implements IAdapterItem<Note> {
        private TextView tv_list_title;//笔记标题
        private TextView tv_list_summary;//笔记摘要
        private TextView tv_list_time;//创建时间
        private TextView tv_list_group;//笔记分类
        private CardView card_view_note;

        public NoteItemView(Context context) {
            super(context);
            View v = LayoutInflater.from(context).inflate(R.layout.list_note_item, this, true);
            card_view_note = v.findViewById(R.id.card_view_note);
            tv_list_title = v.findViewById(R.id.tv_list_title);
            tv_list_summary = v.findViewById(R.id.tv_list_summary);
            tv_list_time = v.findViewById(R.id.tv_list_time);
            tv_list_group = v.findViewById(R.id.tv_list_group);

            adjustBodyLayoutParams();
        }

        @Override
        public void bindDataToView(Note note, int position) {
            tv_list_title.setText(note.getTitle());
            tv_list_summary.setText(note.getContent());
            tv_list_time.setText(note.getCreateTime());
            tv_list_group.setText(note.getGroupName());
        }

        private void adjustBodyLayoutParams(){
            ViewGroup.LayoutParams params = tv_list_summary.getLayoutParams();
            params.width = CommonUtil.getScreenWidth(mContext);
            params.height = params.width/6;
            tv_list_summary.setLayoutParams(params);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }

    }


}
