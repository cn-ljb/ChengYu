package com.ljb.chengyu.act

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.ljb.chengyu.R
import com.ljb.chengyu.adapter.ChengYuBoxAdapter
import com.ljb.chengyu.modle.ChengYu
import com.ljb.chengyu.modle.ChengYuChar
import com.ljb.chengyu.protocol.ChengYuDAOProtocol
import com.ljb.chengyu.widget.PageStateLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.layout_game.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by L on 2018/2/22.
 */
class GameActivity : Activity() {

    /**
     * 双击隐藏结果第一次时间戳
     * */
    private var mFirstResultDown: Long = 0

    /**
     * 隐藏答案集
     * */
    private var mResultStr = ""

    /**
     * 被你猜游戏的成语总数
     * */
    private val mSize = 10

    /**
     * 本次游戏用到的成语
     * */
    private val mResultCYList by lazy { ArrayList<ChengYu>() }

    /**
     * 用户选择的< box索引,字符>
     * */
    private val mOneAnswerCYMap = LinkedHashMap<Int, String>()


    private val mBoxAdapter by lazy { ChengYuBoxAdapter(this, mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        initView()
        initData()
    }

    private fun initView() {
        val contentView = View.inflate(this, R.layout.layout_game, null)
        layout_page.setContentView(contentView)
        gv_box.adapter = mBoxAdapter
        gv_box.setOnItemClickListener { _, _, position, _ ->
            answerChengYu(position)
        }

        tv_clear_answer.setOnClickListener {
            claerAnswer()
        }

        tv_remind.setOnClickListener { showRemind() }

        view_get_result.setOnClickListener {
            getResult()
        }
    }


    private fun initData() {
        getDataFromDB()
    }

    private fun getDataFromDB() {
        Observable.create<MutableList<ChengYu>> {
            try {
                val list = ChengYuDAOProtocol.getChengYu(mSize)
                it.onNext(list)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }.map { splitChengYu(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    layout_page.setPageState(PageStateLayout.STATE_SUCCEED)
                    initPage(it)
                }, {
                    layout_page.setPageState(PageStateLayout.STATE_ERROR)
                })

    }

    private fun splitChengYu(list: MutableList<ChengYu>): MutableList<ChengYuChar> {
        mResultCYList.clear()
        mResultCYList.addAll(list)

        mResultStr = String.format(getString(R.string.chengyu_result), list.size)
        val splitList = ArrayList<ChengYuChar>()
        for (item in list) {
            mResultStr += "${item.chengYu}\n"
            item.chengYu.map { splitList.add(ChengYuChar(it.toString())) }
        }
        Collections.shuffle(splitList)
        return splitList
    }

    private fun initPage(list: MutableList<ChengYuChar>) {
        mBoxAdapter.mData.clear()
        mBoxAdapter.mData.addAll(list)
        mBoxAdapter.notifyDataSetChanged()
    }


    /**
     * 选词
     * */
    private fun answerChengYu(position: Int) {

        if (mBoxAdapter.mData[position].isAnswered) return

        if (mOneAnswerCYMap.size >= 4) return

        mBoxAdapter.mData[position].isChecked = true
        mBoxAdapter.notifyDataSetChanged()

        val char = mBoxAdapter.mData[position].char
        mOneAnswerCYMap.put(position, char)

        when (mOneAnswerCYMap.size) {
            1 -> tv_answer1.text = char
            2 -> tv_answer2.text = char
            3 -> tv_answer3.text = char
            4 -> tv_answer4.text = char
        }

        if (mOneAnswerCYMap.size < 4) return

        var answerCY = ""
        mOneAnswerCYMap.map { answerCY += it.value }

        var resultIndex = -1

        for ((index, cy) in mResultCYList.withIndex()) {
            if (cy.chengYu == answerCY) {
                //回答成功
                resultIndex = index
                mOneAnswerCYMap.map { mBoxAdapter.mData[it.key].isAnswered = true }
                mBoxAdapter.notifyDataSetChanged()
                claerAnswer()
                break
            }
        }

        if (resultIndex == -1) {
            //回答错误
            answerError()
        } else {
            mResultCYList.removeAt(resultIndex)
        }

    }

    /**
     * 没有答对
     * */
    private fun answerError() {
        tv_answer1.setBackgroundResource(R.drawable.shape_answer_error_bg)
        tv_answer2.setBackgroundResource(R.drawable.shape_answer_error_bg)
        tv_answer3.setBackgroundResource(R.drawable.shape_answer_error_bg)
        tv_answer4.setBackgroundResource(R.drawable.shape_answer_error_bg)
    }


    /**
     * 清除本次答题
     * */
    private fun claerAnswer() {
        mOneAnswerCYMap.map { mBoxAdapter.mData[it.key].isChecked = false }
        mBoxAdapter.notifyDataSetChanged()
        tv_answer1.text = ""
        tv_answer2.text = ""
        tv_answer3.text = ""
        tv_answer4.text = ""
        tv_answer1.setBackgroundResource(R.drawable.shape_answer_bg)
        tv_answer2.setBackgroundResource(R.drawable.shape_answer_bg)
        tv_answer3.setBackgroundResource(R.drawable.shape_answer_bg)
        tv_answer4.setBackgroundResource(R.drawable.shape_answer_bg)
        mOneAnswerCYMap.clear()
    }


    /***
     * 提示
     */
    private fun showRemind() {
        var remindStr = ""
        for (cy in mResultCYList) {
            if (!cy.dianGu.isEmpty()) {
                remindStr = cy.dianGu
                break
            } else if (!cy.chuChu.isEmpty()) {
                remindStr = cy.chuChu
                break
            }
        }
        tv_remind_result.text = if (remindStr.isEmpty()) resources.getString(R.string.unknow_remind) else remindStr
    }


    private fun getResult() {
        if (layout_page.getPageState() == PageStateLayout.STATE_SUCCEED && mBoxAdapter.mData.size > 0) {
            if (mFirstResultDown == 0L) {
                mFirstResultDown = System.currentTimeMillis()
            } else {
                if (System.currentTimeMillis() - mFirstResultDown < 500) {
                    tv_remind_result.text = mResultStr
                } else {
                    mFirstResultDown = System.currentTimeMillis()
                }
            }
        }
    }

}