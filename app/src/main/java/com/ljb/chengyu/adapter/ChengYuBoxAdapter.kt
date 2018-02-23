package com.ljb.chengyu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ljb.chengyu.R
import com.ljb.chengyu.modle.ChengYuChar
import com.yimu.store.adapter.base.BaseListAdapter

/**
 * Created by L on 2018/2/23.
 */
class ChengYuBoxAdapter(c: Context, data: MutableList<ChengYuChar>) : BaseListAdapter<ChengYuChar>(c, data) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)
        val itemView = View.inflate(mContext, R.layout.item_chengyu, null)
        val tv_text = itemView.findViewById<TextView>(R.id.tv_text)
        tv_text.text = item.char

        if (item.isChecked) {
            tv_text.setTextColor(mContext.resources.getColor(R.color.colorFFA042))
            tv_text.setBackgroundResource(R.drawable.shape_answer_bg)
        } else {
            tv_text.setTextColor(mContext.resources.getColor(R.color.white))
            tv_text.setBackgroundResource(R.drawable.shape_box_item_bg)
        }


        if (item.isAnswered) {
            tv_text.visibility = View.INVISIBLE
        } else {
            tv_text.visibility = View.VISIBLE
        }

        return itemView
    }
}