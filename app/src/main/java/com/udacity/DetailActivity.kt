package com.udacity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.MainActivity.Companion.DOWNLOAD_STATUS
import com.udacity.MainActivity.Companion.FILE_NAME
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)


            val fileNameDescription = intent.getStringExtra(FILE_NAME)
            filename_description_tv.text = fileNameDescription
            val downloadStatus = intent.getStringExtra(DOWNLOAD_STATUS)
            status_tv.text = downloadStatus

            if(TextUtils.equals(downloadStatus, getResources().getString(R.string.download_success) )){
                status_tv.setTextColor(Color.GREEN)
            }else{
                status_tv.setTextColor(Color.RED)
            }



        ok_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            startActivity(intent)
        }
        }
    }

