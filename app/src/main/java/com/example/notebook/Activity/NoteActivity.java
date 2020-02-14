package com.example.notebook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.example.notebook.BaseApplication;
import com.example.notebook.Entity.Group;
import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.Util.CommonUtil;
import com.example.notebook.Util.GlideSimpleLoader;
import com.example.notebook.Util.ImageUtils;
import com.example.notebook.Util.ImageWatcher.ImageWatcherHelper;
import com.example.notebook.Util.MyGlideEngine;
import com.example.notebook.Util.PopupWindowFactory;
import com.example.notebook.Util.SDCardUtil;
import com.example.notebook.Util.StringUtils;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.db.GroupDao;
import com.example.notebook.db.NoteDao;
import com.sendtion.xrichtext.RichTextEditor;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class NoteActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "NoteActivity";
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private static final int RC_READ_EXTERNAL_STORAGE = 11;

    private static final int BOTTOM_SHEET_ITEM_PICTURE = 0;
    private static final int BOTTOM_SHEET_ITEM_FREEHAND = 1;
    private static final int TAKE_PHOTO = 2;

    private EditText et_new_title;
    private RichTextEditor et_new_content;
    private CheckBox cb_add_review;
    private Button btn_new_group;

    private GroupDao groupDao;
    private NoteDao noteDao;
    private List<Group> groupList;
    private Note mNote;

    private String name;

    private Intent intent;

    private String myTitle;
    private String myContent;
    private int myGroupId = 0;
    private String myCreateTime;
    private String myGroupName;
    private int isWasted = 0;
    private int isAdded = 0;

    private Disposable subsLoading;
    private Disposable subsInsert;
    private Disposable mDisposable;
    private ImageWatcherHelper iwHelper;

    private int screenWidth;
    private int screenHeight;

    private Toolbar toolbar;
    /**
     * 点击附件弹出的popup menu item的con信息
     */
    private int[] mAttachmentPopMenuIcons;
    /**
     * 点击附件弹出的popup menu item的文本信息
     */
    private String[] mAttachmentPopMenuTexts;

    private HashMap<Integer, String> mGroupPopMenuMap;
    private String[] mGroupPopMenuTexts;
    private int[] mGroupPopupIcon;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        BaseApplication baseApplication = (BaseApplication) BaseApplication.getInstance();
        name = baseApplication.getUser_name();
        initView();
        initInternalData();
        setGroupPopupList();
    }

    private void initView() {
        et_new_title = findViewById(R.id.ed_new_title);
        et_new_content = findViewById(R.id.et_new_note);
        cb_add_review = findViewById(R.id.cb_add_review);
        btn_new_group = findViewById(R.id.btn_group_name);
        btn_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGroupPopupList();
            }
        });

        iwHelper = ImageWatcherHelper.with(this, new GlideSimpleLoader());

        intent = getIntent();

//        Log.d(TAG, "initView: " + (intent!=null));
        if (intent != null) {
            mNote = intent.getParcelableExtra("note_item");
            if(mNote!=null){
                myGroupId = mNote.getGroupId();
                myGroupName = mNote.getGroupName();
                myTitle = mNote.getTitle();
                myContent = mNote.getContent();
                btn_new_group.setText(myGroupName);
                if (mNote.getIsAdded() == 1) {
                    cb_add_review.setChecked(true);
                }
                et_new_title.setText(myTitle);
                et_new_content.post(new Runnable() {
                    @Override
                    public void run() {
                dealwithContent();
            }
        });
    }
        }//if (intent != null)


        //实例化note ,group
        groupDao = new GroupDao(this);
        noteDao = new NoteDao(this);

        screenWidth = CommonUtil.getScreenWidth(this);
        screenHeight = CommonUtil.getScreenHeight(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealwithExit();
            }
        });
        openSoftKeyInput();//打开软键盘显示
        dealwithContent();
    }

    public void setGroupPopupList() {
        groupDao = new GroupDao(NoteActivity.this);
        groupList = groupDao.queryGroupAll(name);
        Log.d(TAG, "setGroupPopupList: "+groupList.size());

        mGroupPopMenuMap = new HashMap<Integer, String>();
        for (int i = 0; i < groupList.size(); i++) {
            Group g = groupList.get(i);
            mGroupPopMenuMap.put(i, g.getName());
        }

        mGroupPopMenuTexts = new String[mGroupPopMenuMap.size()];
        for (int i = 0; i < mGroupPopMenuMap.size(); i++) {
            String name = mGroupPopMenuMap.get(i).toString();
            mGroupPopMenuTexts[i] = name;
        }
        mGroupPopupIcon = new int[mGroupPopMenuMap.size()];
        for (int i = 0; i < mGroupPopMenuMap.size(); i++) {
            mGroupPopupIcon[i] = R.drawable.icon_favorite;
        }

    }

    private void initInternalData() {
        mAttachmentPopMenuIcons = new int[]{
                R.drawable.menu_photo,
                R.drawable.menu_write
        };
        mAttachmentPopMenuTexts = new String[]{
                getString(R.string.menu_photo),
                getString(R.string.menu_write)
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:

                break;
            case R.id.menu_attachment:
                showAttachmentPopupDialog();
                break;
            case R.id.menu_delete:{
             if(mNote!=null){
                 mNote.setIsWasted(1);
                 new NoteDao(this).updateNote(mNote);
                 ToastUtil.showMsg(NoteActivity.this,"删除成功");
             }else{
                 onBackPressed();
             }
            }
                break;
            case R.id.menu_get_image:
                ToastUtil.showMsg(NoteActivity.this, "还未实现");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //获得groupId和groupName
    public void showGroupPopupList() {
        ListPopupWindow listPopupWindow = PopupWindowFactory.createListPopUpWindow(this, findViewById(R.id.btn_group_name),
                mGroupPopupIcon, mGroupPopMenuTexts, new PopupWindowFactory.ListPopUpWindowItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Group g = groupList.get(position);
                        btn_new_group.setText(g.getName());
                        myGroupId = position;
                        myGroupName = btn_new_group.getText().toString();
                        Log.d(TAG, "onItemClick: " + g.getName());
                    }
                });
        listPopupWindow.show();

    }

    private void showAttachmentPopupDialog() {
        ListPopupWindow listPopupWindow = PopupWindowFactory.createListPopUpWindow(this, findViewById(R.id.menu_attachment),
                mAttachmentPopMenuIcons, mAttachmentPopMenuTexts, new PopupWindowFactory.ListPopUpWindowItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case BOTTOM_SHEET_ITEM_PICTURE:
                                if (EasyPermissions.hasPermissions(NoteActivity.this,
                                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    closeSoftKeyInput();
                                    callGallery();
                                } else {
                                    EasyPermissions.requestPermissions(NoteActivity.this,
                                            getString(R.string.need_read_storage_permission),
                                            RC_READ_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE);
                                }
                                break;
                            case BOTTOM_SHEET_ITEM_FREEHAND:
                                ToastUtil.showMsg(NoteActivity.this, "还未实现");
                                break;
                        }
                    }
                });
        listPopupWindow.show();
    }

    public void dealwithContent() {
        if(mNote!=null){
            et_new_content.clearAllLayout();
        }
        showDataSync(myContent);
        //图片点击事件
        et_new_content.setOnRtImageClickListener(new RichTextEditor.OnRtImageClickListener() {
            @Override
            public void onRtImageClick(View view, String imagePath) {
//                ToastUtil.showMsg(NoteActivity.this, "onRtImageClick!");
                myContent = getEditData();
                if (!TextUtils.isEmpty(myContent)) {
                    List<String> imageList = StringUtils.getTextFromHtml(myContent, true);
                    if (!TextUtils.isEmpty(imagePath)) {
                        int currentPosition = imageList.indexOf(imagePath);
                        List<Uri> dataList = new ArrayList<>();
                        for (int i = 0; i < imageList.size(); i++) {
                            dataList.add(ImageUtils.getUriFromPath(imageList.get(i)));
                        }
                        iwHelper.show(dataList, currentPosition);
                    }
                }
            }
        });

    }

    /**
     * 异步方式显示数据
     */
    private void showDataSync(final String html) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                showEditData(emitter, html);
            }
        })
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        ToastUtil.showMsg(NoteActivity.this,"加载完成");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.showMsg(NoteActivity.this,"加载错误");
//                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                                   mDisposable = d;
                    }

                    @Override
                    public void onNext(String text) {
                        try {
                                       if ( et_new_content != null) {
                                           if (text.contains("<img") && text.contains("src=")) {
                                               //imagePath可能是本地路径，也可能是网络地址
                                               String imagePath = StringUtils.getImgSrc(text);
                                                et_new_content.addImageViewAtIndex( et_new_content.getLastIndex(), imagePath);
                                           } else {
                                                et_new_content.addEditTextAtIndex( et_new_content.getLastIndex(), text);
                                           }
                                       }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }


    /**
     * 显示数据
     */
    private void showEditData(ObservableEmitter<String> emitter, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                emitter.onNext(text);
            }
            emitter.onComplete();
        } catch (Exception e) {
            e.printStackTrace();
            emitter.onError(e);
        }
    }

    private void callGallery() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                .countable(true)
                .maxSelectable(3)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new MyGlideEngine())//图片加载方式，Glide4需要自定义实现
                .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .captureStrategy(new CaptureStrategy(true, "com.sendtion.matisse.fileprovider"))//存储到哪里
                .forResult(REQUEST_CODE_CHOOSE);//请求码
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == RC_READ_EXTERNAL_STORAGE) {
            callGallery();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE:
                    //异步方式插入图片
                    insertImagesSync(data);
                    break;
            }
        }
    }

    /**
     * 异步方式插入图片
     */
    private void insertImagesSync(final Intent data) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    et_new_content.measure(0, 0);
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    // 可以同时插入多张图片
                    for (Uri imageUri : mSelected) {
                        String imagePath = SDCardUtil.getFilePathFromUri(NoteActivity.this, imageUri);
//                        Log.d("NoteActivity", "###path=" + imagePath);
                        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, screenWidth, screenHeight);//压缩图片
                        bitmap = BitmapFactory.decodeFile(imagePath);
                        imagePath = SDCardUtil.saveToSdCard(bitmap);
//                        Log.d("NoteActivity", "###imagePath=" + imagePath);
                        emitter.onNext(imagePath);
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        ToastUtil.showMsg(NoteActivity.this, "图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showMsg(NoteActivity.this, "图片异步onError!");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        subsInsert = d;
                    }

                    @Override
                    public void onNext(String imagePath) {
                        et_new_content.insertImage(imagePath);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则保存数据
        if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
                CommonUtil.isLockScreeen(getApplicationContext())) {
            saveNoteData();//处于后台时保存数据
        }
        if (subsLoading != null && subsLoading.isDisposed()) {
            subsLoading.dispose();
        }
        if (subsInsert != null && subsInsert.isDisposed()) {
            subsInsert.dispose();
        }
    }

    private String getEditData() {
        StringBuilder content = new StringBuilder();
        try {
            List<RichTextEditor.EditData> editList = et_new_content.buildEditData();
            for (RichTextEditor.EditData itemData : editList) {
                if (itemData.inputStr != null) {
                    content.append(itemData.inputStr);
                } else if (itemData.imagePath != null) {
                    content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private void saveNoteData() {
        myTitle = et_new_title.getText().toString();
        myContent = getEditData();
        //groupId,groupName在showgroupPop()中获得
        myCreateTime = CommonUtil.date2string(new Date());
        if (cb_add_review.isChecked()) {
            isAdded = 1;
        }
        if(mNote!=null){
            mNote.setTitle(myTitle);
            mNote.setContent(myContent);
            mNote.setCreateTime(myCreateTime);
            mNote.setGroupId(myGroupId);
            mNote.setGroupName(myGroupName);
            mNote.setIsWasted(0);
            mNote.setIsAdded(isAdded);
            mNote.setIsStared(0);
            new NoteDao(this).updateNote(mNote);
        }else{
            Note note = new Note();
            note.setTitle(myTitle);
            note.setContent(myContent);
            note.setGroupId(myGroupId);
            note.setGroupName(myGroupName);
            note.setCreateTime(myCreateTime);
            note.setIsWasted(isWasted);
            note.setIsAdded(isAdded);
            long ret = new NoteDao(this).insertNote(note,name);
            Log.d(TAG, "saveNoteData: " + ret);
        }
    }

    private void dealwithExit() {
        builder = new AlertDialog.Builder(NoteActivity.this);
        builder.setMessage("是否保存笔记?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToastUtil.showMsg(NoteActivity.this, "保存!");
                        saveNoteData();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 打开软键盘
     */
    private void openSoftKeyInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && !imm.isActive() && et_new_content != null) {
            et_new_content.requestFocus();
            //第二个参数可设置为0
            //imm.showSoftInput(et_content, InputMethodManager.SHOW_FORCED);//强制显示
            imm.showSoftInputFromInputMethod(et_new_content.getWindowToken(),
                    InputMethodManager.SHOW_FORCED);
        }

    }

    /**
     * 关闭软键盘
     */
    private void closeSoftKeyInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            //imm.hideSoftInputFromInputMethod();//据说无效
            //imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0); //强制隐藏键盘
            //如果输入法在窗口上已经显示，则隐藏，反之则显示
            //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

