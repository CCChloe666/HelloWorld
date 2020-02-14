package com.example.notebook.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.notebook.Activity.GroupDetailActivity;
import com.example.notebook.Activity.MainActivity;
import com.example.notebook.Adapter.GroupAdapter;
import com.example.notebook.Adapter.base.BaseAdapter;
import com.example.notebook.Adapter.base.OnItemClickListener;
import com.example.notebook.Adapter.base.OnItemLongClickListener;
import com.example.notebook.BaseApplication;
import com.example.notebook.Entity.Group;
import com.example.notebook.R;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.db.GroupDao;
import com.example.notebook.db.NoteDao;

import java.util.List;

import view.ItemListDialogFragment;
import view.SpacesItemDecoration;

public class AllGroupFragment extends Fragment implements ItemListDialogFragment.Listener {
    protected View mRootView;
    protected GroupAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected MainActivity mMainActivity;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Group> groupList = null;
    protected String name;
    private Intent intent;

    private static final String TAG = "NoteBook.AllGroupFragment";
    private static final int BOTTOM_SHEET_ITEM_RENAME = 0;
    private static final int BOTTOM_SHEET_ITEM_REMOVE = 1;
    private int mLongClickNoteId;

    private GroupDao groupDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseApplication baseApplication = (BaseApplication) BaseApplication.getInstance();
        name = baseApplication.getUser_name();
        mRootView = inflater.inflate(R.layout.fragment_main_page, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mMainActivity = (MainActivity) getActivity();
        initBaseView();
    }

    public void initBaseView() {
        mRecyclerView = mRootView.findViewById(R.id.rv_main_content);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new GroupAdapter(getActivity(), onGetGroup());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_refresh_main_page);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorGrayDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                gotoGroupDetailActivity(position);
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                createAndShowDialog();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        resumeUI();
    }

    /**
     * Fragment的hide与show不会走常规生命周期函数，通过该函数判断
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resumeUI();
        }
    }

    private void resumeUI() {
        if (!mMainActivity.getFabMenu().isShown()) {
            mMainActivity.getFabMenu().showMenu(true);
        }
        onRefreshData();
    }

    public void gotoGroupDetailActivity(int position) {
        intent = new Intent(mMainActivity, GroupDetailActivity.class);
        intent.putExtra("group_id", position);
        startActivity(intent);
    }


    void onRefreshData() {
        groupList = new GroupDao(mMainActivity).queryGroupAll(name);
        mAdapter.refreshData(groupList);
    }

    public List<Group> onGetGroup() {
        groupDao = new GroupDao(mMainActivity);
        groupList = groupDao.queryGroupAll(name);
        if( groupList.size()<=0 ){
            ToastUtil.showMsg(mMainActivity,"没有笔记本");
        }
        return groupList;
    }

    public void onItemClicked(int position) {
        switch (position) {
            case BOTTOM_SHEET_ITEM_RENAME:
                showRenameDialog();
                break;
            case BOTTOM_SHEET_ITEM_REMOVE: {
                AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
                builder.setTitle("注意")
                        .setMessage("确定要删除该分组及其下所有笔记?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                removeGroup();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
            break;
        }
    }

    private void removeGroup() {
        //连同分组中的所有笔记也一并删除
        int key = groupList.get(mLongClickNoteId).getId();
        new NoteDao(mMainActivity).deleteGroupNote(key);
        new GroupDao(mMainActivity).deleteGroup(key);
        ToastUtil.showMsg(mMainActivity,"分组删除成功");
        mAdapter.refreshData(groupList);
    }

    private void createAndShowDialog() {//显示有问题!!!
        int[] icons = {R.drawable.dialog_item_rename, R.drawable.dialog_item_remove};
        String[] texts = {getString(R.string.dialog_rename), getString(R.string.dialog_remove)};
        ItemListDialogFragment.newInstance(icons, texts).setListener(this).show(getActivity().getSupportFragmentManager(), TAG);
    }

    private void showRenameDialog() {
        final EditText popContentView = (EditText) View.inflate(getContext(), R.layout.view_pop_content_input, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("重命名")
                .setView(popContentView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String newName = popContentView.getText().toString();
                        if (!TextUtils.isEmpty(newName)) {
                            Group current = groupList.get(mLongClickNoteId);
                            current.setName(newName);
                            new GroupDao(mMainActivity).updateGroup(current);
                            ToastUtil.showMsg(mMainActivity,"重命名成功");
                        }
                        mAdapter.refreshData(groupList);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();
    }


}
