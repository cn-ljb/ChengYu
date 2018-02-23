package com.ljb.chengyu.act

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ljb.chengyu.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }


    private fun initView() {
        btn_start_game.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}
