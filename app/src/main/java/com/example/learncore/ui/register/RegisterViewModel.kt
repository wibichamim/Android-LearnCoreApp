package com.example.learncore.ui.register

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
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: CoreSession
    ) : BaseViewModel(apiService, session) {

    fun register(email : String?, password : String?) {
        viewModelScope.launch {
            ApiObserver(
                block = {apiService.register(email, password)},
                toast = true,
                responseListener = object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        session.clearAll()

                        val token = response.getString("token")
                        val id = response.getInt("id")
                        session.setValue(Const.ISLOGIN.SAVE_LOGIN,true)
                        token.let { session.setValue(Const.SESSION.PREF_TOKEN,it) }
                        _apiResponse.send(ApiResponse().responseSuccess(data = listOf(token,id)))
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