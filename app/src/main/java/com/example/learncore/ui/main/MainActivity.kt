package com.example.learncore.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.crocodic.core.extension.openActivity
import com.example.learncore.R
import com.example.learncore.base.activity.BaseActivity
import com.example.learncore.data.const.Const
import com.example.learncore.databinding.ActivityMainBinding
import com.example.learncore.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLogin = session.getBoolean(Const.ISLOGIN.SAVE_LOGIN)
            if (isLogin) {
                openActivity<LoginActivity>()
//                overridePendingTransition(R.anim.transition_from_bottom, R.anim.transition_to_top)
                finish()
            } else {
                openActivity<LoginActivity>()
//                overridePendingTransition(R.anim.transition_from_bottom, R.anim.transition_to_top)
                finish()
            }
        }, 700)    }
}