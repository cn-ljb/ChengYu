package com.ljb.chengyu.protocol

import android.annotation.SuppressLint
import android.net.Uri
import com.ljb.chengyu.ChengYuApp.Companion.mContext
import com.ljb.chengyu.common.DBConstant
import com.ljb.chengyu.db.DatabaseProvider
import com.ljb.chengyu.modle.ChengYu


@SuppressLint("StaticFieldLeak")
/**
 * Created by L on 2018/2/23.
 */
object ChengYuDAOProtocol {


    fun getChengYu(size: Int): MutableList<ChengYu> {
        val list = ArrayList<ChengYu>()
        val c = mContext.contentResolver.query(Uri.parse(DatabaseProvider.URI_CHENGYU),
                null,
                null,
                null,
                "RANDOM() limit $size")
        c?.let {
            while (c.moveToNext()) {
                val id = c.getInt(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_ID))
                val chengyu = c.getString(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_CHENGYU))
                val pingyin = c.getString(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_PINGYIN))
                val diangu = c.getString(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_DIANGU))
                val chuchu = c.getString(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_CHUCHU))
                val lizi = c.getString(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_LIZI))
                val spingyin = c.getString(c.getColumnIndex(DBConstant.TABLE_CHENGYU.COLUMN_SPINGYIN))
                list.add(ChengYu(id, chengyu, pingyin, diangu, chuchu, lizi, spingyin))
            }
            c.close()
        }
        return list
    }

}