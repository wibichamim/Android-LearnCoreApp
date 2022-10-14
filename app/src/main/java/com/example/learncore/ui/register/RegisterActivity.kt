package com.example.learncore.ui.register

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
import com.example.learncore.databinding.ActivityRegisterBinding
import com.example.learncore.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding,RegisterViewModel>(R.layout.activity_register) {
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
                                tos("${it.message}")
                            }

                            else -> {
                                loadingDialog.dismiss()
                                tos("Opps! Error")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)

        when(v) {
            binding.btnLogin -> {
                openActivity<LoginActivity>()
                finishAffinity()
            }
            binding.btnRegister -> validateForm()
        }
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
                        loadingDialog.show("Registering")
                        viewModel.register(binding.inputEmail.textOf(), binding.inputPassword.textOf())
                    }
                }
            }
        }
    }
}