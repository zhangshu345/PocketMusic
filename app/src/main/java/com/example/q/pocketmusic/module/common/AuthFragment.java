package com.example.q.pocketmusic.module.common;

import android.content.Intent;

import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.MyUser;
import com.example.q.pocketmusic.util.UserUtil;
import com.example.q.pocketmusic.util.common.LogUtils;

/**
 * Created by 鹏君 on 2017/1/26.
 */

public abstract class AuthFragment<V, T extends BasePresenter<V>> extends BaseFragment<V, T> {
    public static final String RESULT_USER = "result";


    @Override
    public void initView() {
        UserUtil.checkLocalUser(this);
        if (UserUtil.user != null) {
            LogUtils.i("user.getContribution:" + String.valueOf(UserUtil.user.getContribution()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_LOGIN) {//请求登录
            if (resultCode == Constant.SUCCESS) {
                UserUtil.user = (MyUser) data.getSerializableExtra(RESULT_USER);//成功登录并复制
            } else if (resultCode == Constant.FAIL) {
                UserUtil.user = null;//登录失败
            }
        }
    }


}
