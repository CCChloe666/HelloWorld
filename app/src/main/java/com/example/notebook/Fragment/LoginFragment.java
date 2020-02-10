package com.example.notebook.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.notebook.Activity.MainActivity;
import com.example.notebook.R;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.db.UserDao;


public class LoginFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText mEdit_name;
    private EditText mEdit_pwd;
    private Button mBtn_login;
    private Button mBtn_get_veri;
    private TextView mText_pwd;
    private CheckBox mCb_remember;

    private View mView;
    private String name, pwd;

    @Override
    public View initView() {
        if (mView == null) {
            mView = View.inflate(mActivity, R.layout.fragment_login, null);
            mText_pwd = mView.findViewById(R.id.text_under_login_pwd);
            mEdit_name = mView.findViewById(R.id.input_phone);
            mEdit_pwd = mView.findViewById(R.id.input_pwd);
            mBtn_login = mView.findViewById(R.id.btn_login);
            mCb_remember = mView.findViewById(R.id.cb_remember);

            //记住密码 自动显示
            pref = PreferenceManager.getDefaultSharedPreferences(mActivity);
            boolean isRemember = pref.getBoolean("remember_pwd",false);
            if(isRemember){
                name = pref.getString("user_name","");
                pwd = pref.getString("user_pwd","");
                mCb_remember.setChecked(pref.getBoolean("remember_pwd",true));
                mEdit_name.setText(name);
                mEdit_pwd.setText(pwd);
            }

            //设置点击事件
            mText_pwd.setOnClickListener(this);
            mBtn_login.setOnClickListener(this);
        }
        return mView;
    }


    @Override
    public void onClick(View v) {
        this.name = mEdit_name.getText().toString();
        this.pwd = mEdit_pwd.getText().toString();

        switch (v.getId()) {
            case R.id.text_under_login_pwd:
                //切换到pwdLoginFragment
                mActivity.switchFragment("mLoginFragement", "mRegisterFragment");
                break;
            case R.id.btn_login:
                Login(name, pwd);
                break;
            default:
                break;
        }
    }

    public void Login(String name, String pwd) {
        Intent intent = null;
        UserDao userDao = new UserDao(mActivity);
        if(userDao.login(name,pwd)){
            intent = new Intent(mActivity, MainActivity.class);
            if(mCb_remember.isChecked()){//用户名密码都正确才可以记住
                editor = pref.edit();
                editor.putBoolean("remember_pwd",true);
                editor.putString("user_name",name);
                editor.putString("user_pwd",pwd);
                editor.apply();
            }

        }else{
            ToastUtil.showMsg(mActivity,"用户名或密码不正确");
        }
        startActivity(intent);
    }



}
