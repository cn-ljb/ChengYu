package com.ljb.chengyu.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.ljb.chengyu.common.DBConstant
import com.ljb.chengyu.common.DBConstant.Companion.DATABASE_NAME
import com.ljb.chengyu.common.DBConstant.Companion.DATABASE_VERSION
import com.ljb.chengyu.log.WBCheLog
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by L on 2018/2/22.
 */
class DatabaseOpenHelper private constructor(private val context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //单例
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: DatabaseOpenHelper? = null

        fun getInstance(c: Context): DatabaseOpenHelper {
            if (instance != null) return instance!!
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = DatabaseOpenHelper(c)
                    instance!!
                }
            }
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        WBCheLog.i("database onCreate")
        createTables(db)
        initTableChengYuData(db)
    }

    private fun initTableChengYuData(db: SQLiteDatabase) {
        val inputStream = context.assets.open("insertCY.txt")
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var str = bufferedReader.readLine()
        var i = 0
        while (!TextUtils.isEmpty(str)) {
            i++
            db.execSQL(str)
            WBCheLog.i("init table chengyu data :: $i：$str")
            str = bufferedReader.readLine()
        }

        //删除不满足4字的成语记录
        db.execSQL("delete from ${DBConstant.TABLE_CHENGYU.TABLE_NAME} where length(${DBConstant.TABLE_CHENGYU.COLUMN_CHENGYU})!=4")
        WBCheLog.i("del  chengyu data : length != 4")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        WBCheLog.i("database onUpgrade")
    }

    private fun createTables(db: SQLiteDatabase) {
        DatabaseColumnsHelper.getTableNames().map { createTable(db, it) }
    }

    private fun createTable(db: SQLiteDatabase, tableName: String) {
        val columns = DatabaseColumnsHelper.getCreateTableSql(tableName)
        db.execSQL("create table if not exists $tableName$columns")
        WBCheLog.i("create table $tableName")
    }
}