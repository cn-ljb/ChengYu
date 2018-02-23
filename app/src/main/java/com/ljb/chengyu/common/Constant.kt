package com.ljb.chengyu.common

import android.provider.BaseColumns

/**
 * Created by L on 2018/2/22.
 */
class DBConstant {
    companion object {
        val DATABASE_AUTHORITY = "com.ljb.chengyu.db.auth"
        val DATABASE_NAME = "db_chengyu"
        val DATABASE_VERSION = 1
    }

    class TABLE_CHENGYU {
        companion object {
            val TABLE_NAME = "table_chengyu"
            val COLUMN_ID = BaseColumns._ID
            val COLUMN_CHENGYU = "ChengYu"
            val COLUMN_PINGYIN = "PingYin"
            val COLUMN_DIANGU = "DianGu"
            val COLUMN_CHUCHU = "ChuChu"
            val COLUMN_LIZI = "LiZi"
            val COLUMN_SPINGYIN = "SPingYin"
        }
    }
}