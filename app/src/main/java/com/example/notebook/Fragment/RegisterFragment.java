package com.example.notebook.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.notebook.Entity.User;
import com.example.notebook.R;
import com.example.notebook.Util.CodeUtil;
import com.example.notebook.Util.ToastUtil;
import com.example.notebook.db.UserDao;

public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private Context mContext;

    private EditText mEdit_phone;
    private EditText mEdit_pwd;
    private EditText mEdit_pwd_again;
    private EditText mEdit_code;
    private ImageView mImage_code;
    private Button mBtn_comfirm;
    private TextView mText_login;

    private View mView;

    private String name,pwd,pwd_again,code,code_input;

    @Override
    public View initView() {
        if (mView == null) {
            mView = View.inflate(mActivity, R.layout.fragment_register, null);

            mEdit_phone = mView.findViewById(R.id.input_register_phone);
            mEdit_pwd = mView.findViewById(R.id.input_register_pwd);
            mEdit_pwd_again = mView.findViewById(R.id.input_pwd_again);
            mEdit_code = mView.findViewById(R.id.input_code);

            mImage_code = mView.findViewById(R.id.image_code);
            mImage_code.setImageBitmap(CodeUtil.getInstance().createBitmap());
//            this.code = CodeUtil.getInstance().getCode().toLowerCase();

            mBtn_comfirm = mView.findViewById(R.id.btn_confirm);
            mText_login = mView.findViewById(R.id.text_under_login_pwd);

            mImage_code.setOnClickListener(this);
            mBtn_comfirm.setOnClickListener(this);
            mText_login.setOnClickListener(this);
        }
        return mView;
    }

    @Override
    public void onClick(View v) {
        this.code = CodeUtil.getInstance().getCode().toLowerCase();
        switch (v.getId()) {
            case R.id.text_under_login_pwd:
                mActivity.switchFragment("mRegisterFragment", "mLoginFragement");
                break;
            case R.id.image_code:
                //更换验证码图片
                switchCodeImage();
                Log.d("TAG", code);
                break;
            case R.id.btn_confirm:
                //确认，保存到数据库
                registerConfirm();
                break;
            default:
                break;
        }
    }

    public void registerConfirm() {
        getInfo();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd_again) || TextUtils.isEmpty(code_input)) {
            ToastUtil.showMsg(mActivity, "信息未填写完整");
        } else if (!pwd.equals(pwd_again)) {
            ToastUtil.showMsg(mActivity, "两次密码输入不一致");
        } else if (!code_input.equals(code)) {
            ToastUtil.showMsg(mActivity, "验证码输入错误");
        } else{
            UserDao userDao = new UserDao(mActivity);
            userDao.register(name,pwd);
            ToastUtil.showMsg(mActivity,"注册成功");
        }

    }


    public void getInfo(){
        this.name = mEdit_phone.getText().toString().trim();
        this.pwd = mEdit_pwd.getText().toString().trim();
        this.pwd_again = mEdit_pwd_again.getText().toString().trim();
        this.code_input = mEdit_code.getText().toString().trim();
    }

    public void switchCodeImage() {
        mImage_code.setImageBitmap(CodeUtil.getInstance().createBitmap());
        this.code = CodeUtil.getInstance().getCode().toLowerCase();
    }





}
