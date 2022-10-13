package com.example.learncore.ui.main

import com.crocodic.core.data.CoreSession
import com.example.learncore.api.ApiService
import com.example.learncore.base.viewmodel.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiService: ApiService, private val session: CoreSession) : BaseViewModel(apiService,session) {

}