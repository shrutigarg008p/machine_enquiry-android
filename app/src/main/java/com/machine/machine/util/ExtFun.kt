package com.machine.machine.util

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.machine.machine.R
import com.machine.machine.commonBase.BaseAdapter
import com.machine.machine.constant.BaseConstants
import java.text.SimpleDateFormat
import java.util.*


fun ViewGroup.hide() {
    this.visibility = View.GONE
}

fun ViewGroup.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun ImageView.passwordOn() {
    this.setImageResource(R.drawable.password_visibility_on);
}

fun ImageView.passwordOf() {
    this.setImageResource(R.drawable.password_visibility_off);
}

fun EditText.passwordShow() {
    this.transformationMethod = HideReturnsTransformationMethod.getInstance();
}

fun EditText.passwordHide() {
    this.transformationMethod = PasswordTransformationMethod.getInstance();
}

fun ImageView.loadUrl(url: String?) {
    if (url != null) {
        val urlstatic = GlideUrl(
            url, LazyHeaders.Builder()
                .addHeader("User-Agent", "your-user-agent")
                .build()
        )
        Glide.with(this)
            .load(urlstatic).placeholder(R.color.light_grey).error(R.color.light_grey)
            .dontAnimate()
            .into(this)
    }

}


fun ImageView.loadRoundUrl(url: String) {
    val urldata = GlideUrl(
        url, LazyHeaders.Builder()
            .addHeader("User-Agent", "your-user-agent")
            .build()
    )
    var requestOptions = RequestOptions()
    requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(24))
    Glide.with(this)
        .load(urldata).apply(requestOptions)
        .into(this)
}

fun EditText.disable() {
    this.isFocusable = false
    this.isEnabled = false
    this.isCursorVisible = false
    //this.keyListener = null

}

fun EditText.enable() {
    this.isFocusable = true
    this.isEnabled = true
    this.isCursorVisible = true
}


fun RecyclerView.addLinearAdapter(
    context: Context?, _adapter: RecyclerView.Adapter<BaseAdapter>
) {

    this.setHasFixedSize(true)
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    this.adapter = _adapter
}

fun RecyclerView.addHorizontalAdapter(
    context: Context?, _adapter: RecyclerView.Adapter<BaseAdapter>
) {
    this.setHasFixedSize(true)
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    this.adapter = _adapter
}

fun RecyclerView.addGridVerticalAdapter(
    context: Context?, spanCount: Int, _adapter: RecyclerView.Adapter<BaseAdapter>
) {
    this.setHasFixedSize(true)

    this.layoutManager = GridLayoutManager(context, spanCount, LinearLayoutManager.VERTICAL, false)
    this.adapter = _adapter
}


fun String.removeWhitespaces() = replace(" ", "")

fun String.digitsOnly(): String {
    val regex = Regex("[^0-9]")
    return regex.replace(this, "")
}

fun String.alphaNumericOnly(): String {
    val regex = Regex("[^A-Za-z0-9 ]")
    return regex.replace(this, "")
}

fun TextView.setData(value: String?) {
    if (value != null) {
        this.text = value
    } else {
        this.text = BaseConstants.EMPTY
    }
}

fun TextView.setData(value: Int?) {
    if (value != null) {
        this.text = value.toString()
    } else {
        this.text = BaseConstants.EMPTY
    }
}

fun LinearProgressIndicator.setData(value: Int?) {
    if (value != null) {
        this.progress = value
    } else {
        this.progress = 0
    }
}

fun Button.changeFavBtn(active: Boolean, context: Context?) {
    if (active) {
        this.show()
        this.setBackgroundColor(context?.getColor(R.color.appLight)!!)

    } else {
        this.show()
        this.setBackgroundColor(context?.getColor(R.color.dark_grey)!!)
    }

}


fun getDateInFormat(dateStr: String,input:String,output:String): String {
    return try {
        val inputFormat = SimpleDateFormat(input)
        inputFormat.timeZone = TimeZone.getTimeZone("GMT")
        val outputFormat = SimpleDateFormat(output)
        val parsedDate: Date = inputFormat.parse(dateStr)

        outputFormat.format(parsedDate)
    }catch (e:Exception){
        return  "-"
    }
}




