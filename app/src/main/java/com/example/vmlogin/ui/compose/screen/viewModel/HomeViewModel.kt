package com.example.vmlogin.ui.compose.screen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.vmlogin.auth.store.user.UserDataStore
import com.example.vmlogin.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userDataStore: UserDataStore
) : ViewModel() {

    val userModel: LiveData<UserModel> = userDataStore.userModelFlow.asLiveData()
}