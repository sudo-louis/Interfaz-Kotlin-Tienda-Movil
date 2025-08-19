package com.example.tiendamovil

import android.content.Context

class Prefs(ctx: Context) {
    private val sp = ctx.getSharedPreferences(Const.PREFS, Context.MODE_PRIVATE)
    fun saveToken(t: String) = sp.edit().putString(Const.KEY_TOKEN, t).apply()
    fun getToken(): String? = sp.getString(Const.KEY_TOKEN, null)
    fun clear() = sp.edit().clear().apply()
}
