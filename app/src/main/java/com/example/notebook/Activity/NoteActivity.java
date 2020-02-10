package com.example.notebook.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.example.notebook.Entity.Group;
import com.example.notebook.Entity.Note;
import com.example.notebook.R;
import com.example.notebook.Util.AttachmentHelper;
import com.example.notebook.Util.CommonUtil;
import com.example.notebook.Util.ImageUtils;
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

import java.io.File;
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
    private Note note;
    private int flag;//区分是新建笔记还是编辑笔记

    private Disposable subsLoading;
    private Disposable subsInsert;

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

    private String[] mGroupPopMenuTexts;

//    private Attachment mAttachment = new Attachment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
        initInternalData();
    }


    private void initView() {
        et_new_title = findViewById(R.id.ed_new_title);
        et_new_content = findViewById(R.id.et_new_note);
        cb_add_review = findViewById(R.id.cb_add_review);
        btn_new_group = findViewById(R.id.btn_group_name);
        btn_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //出现分组
                showGroupPopupList();
            }
        });

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
//                ToastUtil.showMsg(NoteActivity.this, "back!");
//                dealwithExit();
            }
        });

//        openSoftKeyInput();//打开软键盘显示

    }


    public void setGroupPopupList() {
        groupDao = new GroupDao(this);
        groupList = groupDao.queryGroupAll();
        mGroupPopMenuTexts = new String[]{};
        for (int i = 0; i < groupList.size(); i++) {
            mGroupPopMenuTexts[i] = groupList.get(i).getName();
        }
    }

    private void initInternalData() {
        setGroupPopupList();
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
            case R.id.menu_camera:

                break;
            case R.id.menu_delete:

                break;
            case R.id.menu_get_image:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showGroupPopupList() {
        ListPopupWindow listPopupWindow = PopupWindowFactory.createListPopUpWindow(this, findViewById(R.id.btn_group_name), null,
                mGroupPopMenuTexts, new PopupWindowFactory.ListPopUpWindowItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Group g = groupList.get(position);
                        btn_new_group.setText(g.getName());
                    }
                });
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
                                    callGallery();
                                } else {
                                    EasyPermissions.requestPermissions(NoteActivity.this,
                                            getString(R.string.need_read_storage_permission),
                                            RC_READ_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE);
                                }
                                break;
                            case BOTTOM_SHEET_ITEM_FREEHAND:

                                break;
                        }
                    }
                });
        listPopupWindow.show();
    }

    private void takePhoto() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File image = AttachmentHelper.createNewAttachmentFile(this, "images", ".jpg");
//        // 必须使用FileProvider，否则API>=24的设备上运行时，会抛出异常
//        // Uri photoUri = Uri.fromFile(image);
//        Uri photoUri = AttachmentHelper.getFileProviderUri(this, image);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//        mAttachment.setPath(image.getAbsolutePath());
//        mAttachment.setUri(photoUri);
//        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void dealwithExit() {

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

        ToastUtil.showMsg(NoteActivity.this, "callgallery");
    }

    private void gotoFreehandActivity() {
//        Intent intent = new Intent(NoteActivity.this, FreeHandActivity.class);
//        startActivityForResult(intent, BOTTOM_SHEET_ITEM_FREEHAND);
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
            ToastUtil.showMsg(NoteActivity.this, "该插入图片了！");
            switch (requestCode) {
                case BOTTOM_SHEET_ITEM_PICTURE: {

                }
                break;
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
        ToastUtil.showMsg(NoteActivity.this, "insertImageSysn");

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

                    // 测试插入网络图片 http://pics.sc.chinaz.com/files/pic/pic9/201904/zzpic17414.jpg
                    //emitter.onNext("http://pics.sc.chinaz.com/files/pic/pic9/201903/zzpic16838.jpg");
//                    emitter.onNext("http://b.zol-img.com.cn/sjbizhi/images/10/640x1136/1572123845476.jpg");
//                    emitter.onNext("https://img.ivsky.com/img/tupian/pre/201903/24/richu_riluo-013.jpg");
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
                        ToastUtil.showMsg(NoteActivity.this, e.getMessage());
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //如果APP处于后台，或者手机锁屏，则保存数据
        if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
                CommonUtil.isLockScreeen(getApplicationContext())) {
//            saveNoteData(true);//处于后台时保存数据
        }
        if (subsLoading != null && subsLoading.isDisposed()) {
            subsLoading.dispose();
        }
        if (subsInsert != null && subsInsert.isDisposed()) {
            subsInsert.dispose();
        }
    }

//    private void saveNoteData(boolean isBackground) {
//        String noteTitle = et_new_title.getText().toString();
//        String noteContent = getEditData();
//        //获得分组名！！！！！我的天T T
//        String groupName;
//        //获得checkbox的值，是则插入数据库 然后再插入复习计划表，否则直接插入数据库即可
////        String noteTime = tv_new_time.getText().toString();
//
//
//    }

}
