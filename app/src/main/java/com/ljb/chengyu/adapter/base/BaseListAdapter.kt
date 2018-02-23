package com.yimu.store.adapter.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by L on 2016/8/11.
 */
abstract class BaseListAdapter<T>(var mContext: Context, var mData: MutableList<T>) : BaseAdapter() {

    override fun getCount(): Int = mData.size

    override fun getItem(position: Int): T = mData[position]

    override fun getItemId(position: Int): Long = position.toLong()

    abstract override fun getView(position: Int, convertView: View?, parent: ViewGroup): View

}
