package com.machine.machine.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivitySplashBinding
import com.machine.machine.offline.SharedPref
import com.machine.machine.ui.seller.SellerMainActivity
import com.machine.machine.ui.user.UMainActivity
import com.machine.machine.util.ProgressDrawable
import com.machine.machine.util.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val SPLASH_SCREEN_TIME_OUT_CONST: Long = 5000

    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivitySplashBinding.inflate(layoutInflater)


    override fun setup() {
        setLoader()
        startAnime()
        // setTimer()
    }


    private fun setTimer() {
        GlobalScope.launch {
            delay(SPLASH_SCREEN_TIME_OUT_CONST)
            /*if (SharedPref.getToken() != null && SharedPref.getUserType() != null) {

                if (SharedPref.getUserType() == IDConst.CUSTOMER) {
                    UserHome()
                } else {
                    SellerHome()
                }

            } else {
                Utils.getLogin(applicationContext)
            }*/
              Utils.getLogin(applicationContext)

        }

    }

    fun startAnime() {
        val animation = ObjectAnimator.ofInt(binding.splash.progressBar, "progress", 0, 100)
        animation.duration = SPLASH_SCREEN_TIME_OUT_CONST
        animation.interpolator = DecelerateInterpolator()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator?) {}
            override fun onAnimationEnd(animator: Animator?) {
                gotoScreen()
            }

            override fun onAnimationCancel(animator: Animator?) {}
            override fun onAnimationRepeat(animator: Animator?) {}
        })
        animation.start()
    }

    private fun gotoScreen() {
        if (SharedPref.getToken() != null && SharedPref.getUserType() != null) {
            Log.d("Check", "gotoScreen: " +SharedPref.getUserType())

            if (SharedPref.getUserType() == IDConst.CUSTOMER) {
                UserHome()
            } else {
               // UserHome()
                SellerHome()
            }

        } else {
            Utils.getLogin(applicationContext)
        }
    }


    fun setLoader() {
        val fillColor = ContextCompat.getColor(this, com.machine.machine.R.color.appDark)
        val separatorColor = ContextCompat.getColor(this, com.machine.machine.R.color.white)
        val d: Drawable = ProgressDrawable(separatorColor, fillColor)
        binding.splash.progressBar.setProgressDrawable(d)
    }


    private fun UserHome() {
        val intent = Intent(this, UMainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun SellerHome() {
        val intent = Intent(this, SellerMainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun viewModelObj() {

    }


}