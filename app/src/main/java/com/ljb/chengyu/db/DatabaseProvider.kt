package com.ljb.chengyu.db

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.ljb.chengyu.common.DBConstant
import java.util.*

/**
 * Created by L on 2018/2/22.
 */
class DatabaseProvider : ContentProvider() {

    /**
     * 对外的Uri访问标识
     * */
    companion object {
        // 成语表URI匹配码
        val CODE_TYPE_CHENGYU = 1
        val URI_CHENGYU = "content://" + DBConstant.DATABASE_AUTHORITY + "/" + DBConstant.TABLE_CHENGYU.TABLE_NAME
    }


    private val mUriMatcher by lazy { UriMatcher(UriMatcher.NO_MATCH) }


    init {
        registerUri()
    }

    /**
     * 注册Uri
     * */
    private fun registerUri() {
        mUriMatcher.addURI(DBConstant.DATABASE_AUTHORITY, DBConstant.TABLE_CHENGYU.TABLE_NAME, CODE_TYPE_CHENGYU)
    }


    /**
     * 通过Uri返回表名
     */
    override fun getType(uri: Uri): String {
        return when (mUriMatcher.match(uri)) {
            CODE_TYPE_CHENGYU -> DBConstant.TABLE_CHENGYU.TABLE_NAME
            else -> throw IllegalArgumentException("URI:$uri is not matched")
        }
    }

    override fun onCreate(): Boolean = true

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table = getType(uri)
        var id: Long = -1L
        try {
            val db = DatabaseOpenHelper.getInstance(context).writableDatabase
            db.beginTransaction()
            try {
                id = db.insertOrThrow(table, null, values)
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
            if (id == -1L) {
                return null
            }
            context!!.contentResolver.notifyChange(uri, null)
            return ContentUris.withAppendedId(uri, id)
        } catch (e: SQLiteException) {
            return null
        }

    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val table = getType(uri)
        try {
            val qb = SQLiteQueryBuilder()
            qb.tables = table
            val db = DatabaseOpenHelper.getInstance(context).writableDatabase
            val cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        } catch (e: SQLiteException) {
            return null
        }
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val table = getType(uri)
        try {
            var count = 0
            val db = DatabaseOpenHelper.getInstance(context).writableDatabase
            db.beginTransaction()
            try {
                count = db.update(table, values, selection, selectionArgs)
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
            context!!.contentResolver.notifyChange(uri, null)
            return count
        } catch (e: SQLiteException) {
            return 0
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val table = getType(uri)
        try {
            var count = 0
            val db = DatabaseOpenHelper.getInstance(context).writableDatabase
            db.beginTransaction()
            try {
                count = db.delete(table, selection, selectionArgs)
                db.setTransactionSuccessful()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.endTransaction()
            }
            context!!.contentResolver.notifyChange(uri, null)
            return count
        } catch (e: SQLiteException) {
            return 0
        }
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        val table = getType(uri)
        var id: Long = -1L
        var numValues = 0
        try {
            val db = DatabaseOpenHelper.getInstance(context).writableDatabase
            db.beginTransaction()
            try {
                numValues = values.size
                for (i in 0 until numValues) {
                    id = db.insertOrThrow(table, null, values[i])
                }
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
            if (id == -1L) {
                return -1
            }
        } catch (e: SQLiteException) {
            return -1
        }
        return numValues
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>?): Array<ContentProviderResult> {
        try {
            val db = DatabaseOpenHelper.getInstance(context).writableDatabase
            db.beginTransaction()
            try {
                val results = super.applyBatch(operations)
                db.setTransactionSuccessful()
                return results
            } finally {
                db.endTransaction()
            }
        } catch (e: SQLiteException) {
            throw OperationApplicationException()
        }
    }

}