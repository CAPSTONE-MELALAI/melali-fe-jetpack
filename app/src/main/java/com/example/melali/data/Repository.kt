package com.example.melali.data

import android.content.SharedPreferences
import android.util.Log
import com.example.melali.model.request.LoginRequest
import com.example.melali.model.request.RegisterRequest
import com.example.melali.model.response.LoginResponse
import com.example.melali.model.response.RegisterResponse
import com.example.melali.model.response.ResponseWrapper
import com.example.melali.model.response.SingleDestinationResponse
import com.example.melali.model.response.UserResponse
import com.example.melali.util.getResponse
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.plugin
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class Repository @Inject constructor(
    private val client: HttpClient,
    private val pref: SharedPreferences
) {
    fun saveToken(token: String) = pref
        .edit()
        .putString("token", token)
        .apply()

    fun isLogin() = (pref.getString("token", "") ?: "").isNotEmpty()

    fun saveUserToLocal(user: UserResponse) = pref.edit().putString("user", Gson().toJson(user))

    fun getUserFromLocal(): UserResponse? = try {
        val user = pref.getString("user", "") ?: ""
        Gson().fromJson(user, UserResponse::class.java)
    }catch (e: Exception){
        Log.e("ERROR", e.toString())
        null
    }

    suspend fun getUserByEmail(
        email: String,
        onSuccess: (ResponseWrapper<UserResponse>) -> Unit,
        onFailed: (Exception) -> Unit
    ) = getResponse<ResponseWrapper<UserResponse>>(onSuccess, onFailed){
        client.get("https://capstone-melali.et.r.appspot.com/user/$email")
    }

    fun setTokenManually(token: String) {
        client
            .plugin(Auth)
            .bearer { this.loadTokens { BearerTokens(token, "") } }
    }

    fun saveEmail(email: String) = pref.edit().putString("email", email).apply()

    fun getEmail() = pref.getString("email", "") ?: ""

    suspend fun login(
        body: LoginRequest,
        onSuccess: (ResponseWrapper<LoginResponse>) -> Unit,
        onFailed: (Exception) -> Unit
    ) = getResponse<ResponseWrapper<LoginResponse>>(
        onSuccess = onSuccess,
        onFailed = onFailed
    ) {
        client.post("https://capstone-melali.et.r.appspot.com/auth/login/") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun register(
        body: RegisterRequest,
        onSuccess: (ResponseWrapper<RegisterResponse>) -> Unit,
        onFailed: (Exception) -> Unit
    ) = getResponse<ResponseWrapper<RegisterResponse>>(
        onSuccess = onSuccess,
        onFailed = onFailed
    ) {
        client.post("https://capstone-melali.et.r.appspot.com/auth/signup/") {
            setBody(body)
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getAllDestination(
        onSuccess: (ResponseWrapper<List<SingleDestinationResponse>>) -> Unit,
        onFailed: (Exception) -> Unit
    ) = getResponse<ResponseWrapper<List<SingleDestinationResponse>>>(
        onSuccess, onFailed
    ){
        client.get("https://capstone-melali.et.r.appspot.com/destinations/")
    }
}