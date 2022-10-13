package com.example.learncore.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.example.learncore.R
import com.example.learncore.base.activity.BaseActivity
import com.example.learncore.data.const.Const
import com.example.learncore.databinding.ActivityLoginBinding
import com.example.learncore.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when(it.status) {
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                tos("${it.data}")
                            }
                            ApiStatus.ERROR -> {
                                loadingDialog.dismiss()
                                tos("${it.rawResponse}")
                            }
                            else -> {
                                loadingDialog.dismiss()
                                tos("${it.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.btnLogin -> validateForm()
            binding.btnRegister -> {
                openActivity<RegisterActivity>()
                finishAffinity()
            }
        }

        super.onClick(v)
    }

    private fun validateForm() {
        if (listOf(
            binding.inputEmail,
            binding.inputPassword
        ).isEmptyRequired(R.string.form_notvalid)) else {
            loadingDialog.show("Loading")
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        loadingDialog.show("Loging in")
                        viewModel.login(binding.inputEmail.textOf(), binding.inputPassword.textOf())
                    }
                }
            }
        }
    }
}