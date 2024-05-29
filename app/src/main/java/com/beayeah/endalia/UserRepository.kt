package com.beayeah.endalia

import android.database.Cursor
import com.beayeah.endalia.dao.UserDao
import com.beayeah.endalia.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    fun getUserByEmail(email: String): Cursor {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertAll(users: List<User>) {
        userDao.insertAll(users)
    }

    suspend fun cursorToUser(cursor: Cursor): User? = withContext(Dispatchers.IO) {
        return@withContext if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            User(id, email, password)
        } else {
            null
        }.also {
            cursor.close()
        }
    }
}