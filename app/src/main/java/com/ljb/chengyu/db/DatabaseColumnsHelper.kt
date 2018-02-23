package com.ljb.chengyu.db

import com.ljb.chengyu.common.DBConstant
import java.util.*

/**
 * Created by L on 2018/2/22.
 */
object DatabaseColumnsHelper {

    private val DATA_TYPE_TEXT = "TEXT"
    private val DATA_TYPE_LONG = "LONG"
    private val DATA_TYPE_INTEGER = "INTEGER"

    private val mTables = HashMap<String, HashMap<String, String>>()

    init {
        initTableColumnsChengYu()
    }

    private fun initTableColumnsChengYu() {
        val tableUserColumns = HashMap<String, String>()
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_ID, "integer primary key autoincrement")
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_CHENGYU, DATA_TYPE_TEXT)
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_PINGYIN, DATA_TYPE_TEXT)
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_DIANGU, DATA_TYPE_TEXT)
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_CHUCHU, DATA_TYPE_TEXT)
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_LIZI, DATA_TYPE_TEXT)
        tableUserColumns.put(DBConstant.TABLE_CHENGYU.COLUMN_SPINGYIN, DATA_TYPE_TEXT)
        mTables.put(DBConstant.TABLE_CHENGYU.TABLE_NAME, tableUserColumns)
    }


    /**
     * 生成建表的sql语句的一部分
     * @param tableName
     * *
     * @return
     */
    fun getCreateTableSql(tableName: String): String {
        val tabColumns = mTables[tableName]
        val iterator = tabColumns!!.entries.iterator()
        val sql = StringBuilder()
        sql.append(" (")
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val `val` = entry.value
            sql.append(" ").append(key).append(" ").append(`val`).append(", ")
        }
        sql.delete(sql.length - ", ".length, sql.length)
        sql.append(");")
        return sql.toString()
    }

    /**
     * 表的所有列名
     * @param table
     * *
     * @return
     */
    fun getColumns(table: String): Array<String> {
        val tabColumns = mTables[table]
        return tabColumns!!.keys.toTypedArray()
    }

    /**
     * 所有表名
     * */
    fun getTableNames(): MutableSet<String> {
        return mTables.keys
    }

}