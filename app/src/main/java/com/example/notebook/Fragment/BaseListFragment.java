package com.example.notebook.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.notebook.Activity.MainActivity;
import com.example.notebook.Activity.NoteActivity;
import com.example.notebook.Activity.NoteDetailActivity;
import com.example.notebook.Adapter.NoteAdapter;
import com.example.notebook.Adapter.base.BaseAdapter;
import com.example.notebook.Adapter.base.OnItemClickListener;
import com.example.notebook.Adapter.base.OnItemLongClickListener;
import com.example.notebook.BaseApplication;
import com.example.notebook.Entity.Note;
import com.example.notebook.R;

import java.util.List;

import view.SpacesItemDecoration;


public abstract class BaseListFragment extends Fragment {
    private static final String TAG = "BaseListFragment";

    protected View mRootView;
    protected BaseAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected MainActivity mMainActivity;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected List<Note> mNotes;
    protected String name;

    abstract View createView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    abstract void initData();

    abstract void initView();

    abstract void onRefreshData();

    abstract List<Note> onGetNotes();

    abstract void onNoteItemLongClick(View v, int position);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseApplication baseApplication = (BaseApplication) BaseApplication.getInstance();
        name = baseApplication.getUser_name();
        mRootView = createView(inflater, container, savedInstanceState);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mMainActivity = (MainActivity) getActivity();
        initBaseView();
        initData();
    }

    private void initBaseView() {
        mRecyclerView = mRootView.findViewById(R.id.rv_main_content);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new NoteAdapter(getActivity(), onGetNotes());
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
                gotoNoteDetailActivity(position);
            }
        });
        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                onNoteItemLongClick(v, position);
            }
        });
        initView();
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

    protected void gotoNoteDetailActivity(int position){
        Note note = mNotes.get(position);
        Intent intent = new Intent(mMainActivity, NoteActivity.class);
//        Log.d(TAG, "gotoNoteDetailActivity: "+(intent!=null));
        intent.putExtra("note_item",note);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_search:
                //查找笔记
                break;
            case R.id.menu_diction:
                //英语字典
                break;
            case R.id.action_settings:

                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
