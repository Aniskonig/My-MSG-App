package com.medanis.mymsgapplication

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@SuppressLint("Registered")
class isSameDay : MainActivity() {
    lateinit var timeNOW : String




    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun timeNow() : String{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current= LocalDateTime.now()
            try {
                val formatter= DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                val answer= LocalDateTime.parse(current.format(formatter)).toString()
                timeNOW=answer
            }
            catch (@SuppressLint("NewApi") e: DateTimeParseException) {
                val formatter= DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                timeNOW=current.format(formatter).toString()
            }
        } else {
            val date= Date()
            val formatter= SimpleDateFormat("yyyyMMddHHmmss")
            val answer: String=formatter.format(date)
            Log.d("answer",answer)
            timeNOW=answer
        }
        return timeNOW
    }

}