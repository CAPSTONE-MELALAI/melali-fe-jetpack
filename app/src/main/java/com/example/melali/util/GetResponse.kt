package com.example.melali.util

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> getResponse(
    onSuccess:(res: T) -> Unit,
    onFailed:(e: Exception) -> Unit,
    crossinline block: suspend () -> HttpResponse
) {
    try {
        val res = block()

        if (
            res.status.value.toString().startsWith("4")
            || res.status.value.toString().startsWith("5")
        ) {
            onFailed(Exception("Gagal melakukan operasi, coba lagi nanti."))
        }

        val resBody = res.body<T>()
        onSuccess(resBody)
    } catch (e: Exception) {
        Log.e("GET RESPONSE ERROR", e.message.toString())
        onFailed(e)
    }
}