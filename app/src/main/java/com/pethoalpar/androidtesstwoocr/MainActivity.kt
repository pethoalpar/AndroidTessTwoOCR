package com.pethoalpar.androidtesstwoocr

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.scanlibrary.ScanConstants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_tess.setOnClickListener {
            startActivity(Intent(this, TesseractActivity::class.java))
        }

        btn_opencv.setOnClickListener {
            startActivity(Intent(this, OpencvActivity::class.java))
        }
    }
}
