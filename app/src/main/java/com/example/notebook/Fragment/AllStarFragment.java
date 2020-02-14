package com.example.notebook.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.db.NoteDao;

import java.util.List;

import view.ItemListDialogFragment;

public class AllStarFragment extends BaseListFragment implements ItemListDialogFragment.Listener {
    private static final String TAG = "NoteBook.AllStarFragment";
    private static final int BOTTOM_SHEET_ITEM_UNSTAR = 0;
    private static final int BOTTOM_SHEET_ITEM_REMOVE = 1;
    private int mLongClickNoteId;

    @Override
    View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page,container,false);
    }

    @Override
    void initData() {

    }

    @Override
    void initView() {

    }

    @Override
    void onRefreshData() {
        mNotes = new NoteDao(mMainActivity).getAllStared(name);
        mAdapter.refreshData(mNotes);
    }

    @Override
    List<Note> onGetNotes() {
        mNotes = new NoteDao(mMainActivity).getAllStared(name);
        return mNotes;
    }

    @Override
    void onNoteItemLongClick(View v, int position) {
        mLongClickNoteId = position;
        createAndShowDialog();
    }

    @Override
    public void onItemClicked(int position) {
        switch (position){
            case BOTTOM_SHEET_ITEM_UNSTAR:
                unStared();
                break;
            case BOTTOM_SHEET_ITEM_REMOVE:
                removeNote();
                break;
        }
    }

    private void createAndShowDialog(){
        int[] icons = {R.drawable.dialog_cancel_star,R.drawable.dialog_item_remove};
        String[] texts = {getString(R.string.dialog_unstar),getString(R.string.dialog_remove)};
        ItemListDialogFragment.newInstance(icons,texts).setListener(this).show(getActivity().getSupportFragmentManager(),TAG);
    }

    public void unStared(){
        int key = mNotes.get(mLongClickNoteId).getId();
        Note n = mNotes.get(mLongClickNoteId);
        n.setIsStared(0);
        new NoteDao(mMainActivity).updateNote(n);
        ToastUtil.showMsg(mMainActivity,"取消收藏成功");
    }

    public void removeNote(){
        new NoteDao(mMainActivity).deleteNote(mNotes.get(mLongClickNoteId).getId());
        ToastUtil.showMsg(mMainActivity,"删除成功");
        mAdapter.refreshData(mNotes);
    }


}
