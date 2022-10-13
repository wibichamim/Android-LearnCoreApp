package com.example.learncore.base.activity

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.clearNotification
import com.example.learncore.base.viewmodel.BaseViewModel
import com.google.gson.Gson
import javax.inject.Inject

open class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes layoutRes: Int) : CoreActivity<VB,VM>(layoutRes) {
    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var session: CoreSession

    override fun authLogoutSuccess() {
        loadingDialog.dismiss()
        expiredDialog.dismiss()
        session.clearAll()
        clearNotification()
    }
}