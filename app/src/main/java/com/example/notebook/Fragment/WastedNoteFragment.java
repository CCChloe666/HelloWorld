package com.example.notebook.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.db.NoteDao;

import java.util.List;

import view.ItemListDialogFragment;

public class WastedNoteFragment extends BaseListFragment implements ItemListDialogFragment.Listener {
    private static final String TAG = "QuickNote.WasteBasketFragment";
    private static final int BOTTOM_SHEET_ITEM_RECOVERY = 0;
    private static final int BOTTOM_SHEET_ITEM_DELETE_FOREVER = 1;
    private int mLongClickNoteId;

    @Override
    View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    @Override
    void initData() {

    }

    @Override
    void initView() {

    }

    @Override
    void onRefreshData() {
        mNotes = new NoteDao(mMainActivity).getAllWastedNotes(name);
        mAdapter.refreshData(mNotes);
    }

    @Override
    List<Note> onGetNotes() {
        mNotes = new NoteDao(mMainActivity).getAllWastedNotes(name);
        return mNotes;
    }

    @Override
    void onNoteItemLongClick(View v, int position) {
        mLongClickNoteId = position;
        createAndShowDialog();
    }

    private void createAndShowDialog() {//显示有问题!!!
        int[] icons = {R.drawable.dialog_item_recovery, R.drawable.dialog_remove_forever};
        String[] texts = {getString(R.string.dialog_recovery), getString(R.string.dialog_remove_forever)};
        ItemListDialogFragment.newInstance(icons, texts).setListener(this).show(getActivity().getSupportFragmentManager(), TAG);
    }

    @Override
    public void onItemClicked(int position) {
        switch (position) {
            case BOTTOM_SHEET_ITEM_RECOVERY:
                recoverNote();
                break;
            case BOTTOM_SHEET_ITEM_DELETE_FOREVER:
                removeNoteForever();
                break;
        }
    }

    public void recoverNote() {
        int key = mNotes.get(mLongClickNoteId).getId();
        Note n = mNotes.get(mLongClickNoteId);
        n.setIsWasted(0);
        new NoteDao(mMainActivity).updateNote(n);
        mAdapter.refreshData(mNotes);
    }

    public void removeNoteForever() {
        new NoteDao(mMainActivity).deleteNote(mNotes.get(mLongClickNoteId).getId());
        mAdapter.refreshData(mNotes);
    }



}
