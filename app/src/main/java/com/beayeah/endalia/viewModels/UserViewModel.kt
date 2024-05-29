package com.beayeah.endalia.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.beayeah.endalia.AppDatabase
import com.beayeah.endalia.UserRepository
import com.beayeah.endalia.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun insert(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(user)
    }

    fun getUserByEmail(email: String, callback: (User?) -> Unit) = viewModelScope.launch {
        val cursor = withContext(Dispatchers.IO) { repository.getUserByEmail(email) }
        val user = repository.cursorToUser(cursor)
        withContext(Dispatchers.Main) {
            callback(user)
        }
    }
}