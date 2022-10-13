package com.example.learncore.ui.login

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.data.CoreSession
import com.example.learncore.api.ApiService
import com.example.learncore.base.viewmodel.BaseViewModel
import com.example.learncore.data.const.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: CoreSession
) : BaseViewModel(apiService,session) {

    fun login(email : String?, password : String?) {
        viewModelScope.launch {
            ApiObserver(
                block = {apiService.login(email, password)},
                toast = true,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val token = response.getString("token")
                        session.setValue(Const.ISLOGIN.SAVE_LOGIN,true)
                        token.let { session.setValue(Const.SESSION.PREF_TOKEN,it) }
                        _apiResponse.send(ApiResponse().responseSuccess(data = token))
                    }

                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                        _apiResponse.send(ApiResponse().responseError())
                    }
                }
            )
        }
    }

}