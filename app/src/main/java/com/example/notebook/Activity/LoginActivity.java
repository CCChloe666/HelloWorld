package com.example.notebook.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.notebook.Fragment.LoginFragment;
import com.example.notebook.Fragment.RegisterFragment;
import com.example.notebook.R;

public class LoginActivity extends BaseActivity {

    private LoginFragment mLoginFragement = new LoginFragment();
    private RegisterFragment mRegisterFragment = new RegisterFragment();
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFragments();
    }

    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.fl_login_page_content, mLoginFragement, "mLoginFragement")
                .add(R.id.fl_login_page_content, mRegisterFragment, "mRegisterFragment").hide(mRegisterFragment)
                .commit();
    }

    public void switchFragment(String fromTag, String toTag) {
        Fragment from = mFragmentManager.findFragmentByTag(fromTag);
        Fragment to = mFragmentManager.findFragmentByTag(toTag);
        if(mCurrentFragment!=to){
            mCurrentFragment = to;
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(R.animator.from_right,R.animator.out_left);

            if(!to.isAdded()){
                mFragmentTransaction.hide(from).add(R.id.fl_login_page_content,to).commit();
            }else{
                mFragmentTransaction.hide(from).show(to).commit();
            }
        }
    }



}
