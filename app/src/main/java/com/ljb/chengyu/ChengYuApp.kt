package com.ljb.chengyu

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.ljb.chengyu.db.DatabaseOpenHelper

/**
 * Created by L on 2018/2/23.
 */
class ChengYuApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        initDB()
    }

    private fun initDB() {
        DatabaseOpenHelper.getInstance(this).writableDatabase
    }
}