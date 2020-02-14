package com.example.notebook.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.notebook.BaseApplication;
import com.example.notebook.Fragment.AllGroupFragment;
import com.example.notebook.Fragment.AllNotesFragment;
import com.example.notebook.Fragment.AllStarFragment;
import com.example.notebook.Fragment.WastedNoteFragment;
import com.example.notebook.R;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final String ALL_NOTE_FRAGMENT = "notes";
    private static final String ALL_GROUP_FRAGMENT = "groups";
    private static final String ALL_CARD_FRAGMENT = "cards";
    private static final String ALL_STARS_FRAGMENT = "stars";
    private static final String ALL_DAY_MATTER_FRAGMENT = "days";
    private static final String TIMER_FRAGMENT = "timer";
    private static final String CALENDAR_FRAGMENT = "calendar";
    private static final String INFO_ANALYSE_FRAGMENT = "info_analyse";
    private static final String TODAY_TASK_FRAGMENT = "today_task";
    private static final String REVIEW_FRAGMENT = "review";
    private static final String TRASH_CAN_FRAGMENT = "trash_can";
    private static final String STETINGS_FRAGMENT = "settings";

    private HashMap<String, Fragment> mFragments = new HashMap<>();
    private Fragment mCurrentFragment;
    private Toolbar mToolbar;
    private FloatingActionMenu mFabMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseApplication baseApplication = (BaseApplication) getApplication();
        Log.d(TAG, "onCreate: "+baseApplication.getUser_name());
        initView();
    }

    private void initView(){
        initBaseLayout();
        initFragment();//还没完全实现
    }

    private void initBaseLayout(){
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initFabMenu();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFragment(){
        mFragments.put(ALL_NOTE_FRAGMENT,new AllNotesFragment());
        mFragments.put(ALL_GROUP_FRAGMENT,new AllGroupFragment());

        mFragments.put(ALL_STARS_FRAGMENT,new AllStarFragment());



        mFragments.put(TRASH_CAN_FRAGMENT,new WastedNoteFragment());


        showFragment(ALL_NOTE_FRAGMENT);
    }

    private void showFragment(String fragmentTag){
        Fragment fragment = mFragments.get(fragmentTag);
        mCurrentFragment = fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main_page_content,fragment,null);
        ft.commit();
    }

    private void switchFragment(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.fl_main_page_content, to).commit();
            } else {
                ft.hide(from).show(to).commit();
            }
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.icon_notes) {
            switchFragment(mCurrentFragment, mFragments.get(ALL_NOTE_FRAGMENT));
            getSupportActionBar().setTitle(R.string.tb_title_notes);
        } else if (id == R.id.icon_notebook) {
            switchFragment(mCurrentFragment, mFragments.get(ALL_GROUP_FRAGMENT));
            getSupportActionBar().setTitle(R.string.tb_title_groups);
        } else if (id == R.id.icon_english_cards) {

        } else if (id == R.id.icon_favorite) {
            switchFragment(mCurrentFragment, mFragments.get(ALL_STARS_FRAGMENT));
            getSupportActionBar().setTitle(R.string.tb_title_star);
        } else if (id == R.id.icon_tasks) {

        } else if (id == R.id.icon_day_matters) {

        } else if (id == R.id.icon_timer) {

        } else if (id == R.id.icon_calendar) {

        } else if (id == R.id.icon_info_analyse) {

        } else if (id == R.id.icon_review_tasks) {

        }else if(id == R.id.icon_trash_can){
            switchFragment(mCurrentFragment, mFragments.get(TRASH_CAN_FRAGMENT));
            getSupportActionBar().setTitle(R.string.tb_title_trash);
        }else if (id == R.id.icon_settings) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFabMenu() {
        mFabMenu = findViewById(R.id.floating_action_menu);
        FloatingActionButton fabBtnNote = findViewById(R.id.fab_menu_item_text);
        FloatingActionButton fabBtnXmind = findViewById(R.id.fab_menu_item_xmind);
        FloatingActionButton fabBtnCard = findViewById(R.id.fab_menu_item_card);
        fabBtnNote.setOnClickListener(this);
        fabBtnXmind.setOnClickListener(this);
        fabBtnCard.setOnClickListener(this);
    }

    public FloatingActionMenu getFabMenu() {
        return mFabMenu;
    }

    @Override
    public void onClick(View menuItem) {
        Intent intent = null;
        switch (menuItem.getId()){
            case R.id.fab_menu_item_text:
                intent = new Intent(MainActivity.this,NoteActivity.class);
                break;
            case R.id.fab_menu_item_xmind:

                break;
            case R.id.fab_menu_item_card:

                break;
        }
        startActivity(intent);
    }



}
