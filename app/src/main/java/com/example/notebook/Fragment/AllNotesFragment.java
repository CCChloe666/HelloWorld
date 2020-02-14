package com.example.notebook.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.db.NoteDao;

import java.util.List;

import view.ItemListDialogFragment;

public class AllNotesFragment extends BaseListFragment implements ItemListDialogFragment.Listener {
    private static final String TAG = "NoteBook.AllNoteFragment";
    private static final int BOTTOM_SHEET_ITEM_RENAME = 0;
    private static final int BOTTOM_SHEET_ITEM_STAR = 1;
    private static final int BOTTOM_SHEET_ITEM_REMOVE = 2;
    private int mLongClickNoteId;

    @Override
    View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_page,container,false);
    }

    @Override
    void onRefreshData() {
        mNotes = new NoteDao(mMainActivity).queryGroupNotes(-1);
        mAdapter.refreshData(mNotes);
    }

    @Override
    List onGetNotes() {
        mNotes = new NoteDao(mMainActivity).queryGroupNotes(-1);
        return mNotes;
    }

    @Override
    void onNoteItemLongClick(View v, int position) {
        mLongClickNoteId = position;
        createAndShowDialog();
    }
    private void createAndShowDialog(){
        int[] icons = {R.drawable.dialog_item_rename,R.drawable.icon_favorite,R.drawable.dialog_item_remove};
        String[] texts = {getString(R.string.dialog_rename),getString(R.string.dialog_star),getString(R.string.dialog_remove)};
        ItemListDialogFragment.newInstance(icons,texts).setListener(this).show(getActivity().getSupportFragmentManager(),TAG);
    }

    @Override
    public void onItemClicked(int position) {
        switch (position){
            case BOTTOM_SHEET_ITEM_RENAME:
                showRenameDialog();
                break;
            case BOTTOM_SHEET_ITEM_STAR:
                AddToStar();
                break;
            case BOTTOM_SHEET_ITEM_REMOVE:
                removeNote();
                break;
        }
    }

    private void AddToStar(){
        Note n = mNotes.get(mLongClickNoteId);
        n.setIsStared(1);
        new NoteDao(mMainActivity).updateNote(n);
        ToastUtil.showMsg(mMainActivity,"成功添加到我的收藏");
    }

    private void removeNote(){
        new NoteDao(mMainActivity).deleteNote(mNotes.get(mLongClickNoteId).getId());
        ToastUtil.showMsg(mMainActivity,"删除成功");
        mAdapter.refreshData(mNotes);
    }

    private void showRenameDialog(){
        final EditText popContentView = (EditText) View.inflate(getContext(),R.layout.view_pop_content_input,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("重命名")
                .setView(popContentView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTitle = popContentView.getText().toString();
                        if(!TextUtils.isEmpty(newTitle)){
                            Note current = mNotes.get(mLongClickNoteId);
                            current.setTitle(newTitle);
                            new NoteDao(mMainActivity).updateNote(current);
                            ToastUtil.showMsg(mMainActivity,"重命名成功");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setCancelable(true)
                .show();
        mAdapter.refreshData(mNotes);
    }

    @Override
    void initData() {

    }

    @Override
    void initView() {

    }


}
