package com.example.learncore.base.viewmodel

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.data.CoreSession
import com.example.learncore.api.ApiService
import com.example.learncore.data.const.Const
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

open class BaseViewModel(private val apiService: ApiService, private val session: CoreSession) : CoreViewModel() {

    @Inject
    lateinit var gson : Gson

    override fun apiLogout() {
        TODO("Not yet implemented")
    }

    override fun apiRenewToken() {
        TODO("Not yet implemented")
    }
}