package com.machine.machine.ui.user


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.amitrawat.postevents.PostEvent
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityUMainBinding
import com.machine.machine.model.UserDTO
import com.machine.machine.network.RetrofitInstance
import com.machine.machine.offline.SharedPref
import com.machine.machine.ui.common.LoginActivity
import com.machine.machine.ui.maps.MapLocationActivity
import com.machine.machine.ui.user.screen.NotificationsFragment
import com.machine.machine.ui.user.screen.UHomeFragment
import com.machine.machine.ui.user.screen.chat.UChatFragment
import com.machine.machine.ui.user.screen.favourite.UFavTabFragment
import com.machine.machine.ui.user.screen.myorder.UMyOrderTabFragment
import com.machine.machine.ui.user.screen.setting.USettingFragment
import com.machine.machine.util.*
import kotlinx.android.synthetic.main.activity_otp_valiator.*
import kotlinx.android.synthetic.main.custom_badge_layout.view.*


/**
 * Created by Amit Rawat on 2/24/2022.
 */

//FragmentManager.OnBackStackChangedListener
class UMainActivity : BaseActivity<ActivityUMainBinding>() {
    private var count: Int = 0
    private var UserDTO: UserDTO? = null
    private var profileUrl: String? = null
    private val _oldTitle: ArrayList<String> = ArrayList()
    var notificationsBadge : View?  = null
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivityUMainBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        getSubscribeData()
    }

    override fun setup() {
        val lan: String = getCurrentLanguage()
        SharedPref.language(lan)
        UserDTO = SharedPref.getUser()
        profileUrl = UserDTO?.profilePic
        setHeaderHome()

        val homeFragment = UHomeFragment()
        val favFragment = UFavTabFragment()
        val myOrderFragment = UMyOrderTabFragment()
        val chatFragment = UChatFragment()
        val settingFragment = USettingFragment()


        addFragment(homeFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    appIconBar()
                    clearAllTitle()
                    addFragment(homeFragment)

                }
                R.id.navigation_favourites -> {
                    clearAllTitle()
                    addFragment(favFragment)
                    appBarTitleName(Utils.getString(R.string.favourite))
                }

                R.id.navigation_myorders -> {
                    clearAllTitle()
                    addFragment(myOrderFragment)
                    appBarCart(Utils.getString(R.string.my_orders))
                }
                R.id.navigation_chat -> {
                    clearAllTitle()
                    addFragment(chatFragment)
                    appBarTitleName(Utils.getString(R.string.chat))

                    /*val badge: BadgeDrawable = binding.bottomNavigationView.getOrCreateBadge(
                        R.id.navigation_chat)
                    badge.number = 3
                    badge.isVisible = true*/

                    customerRemoveChatBadge()
                }

                R.id.navigation_setting -> {
                    hideActionBar()
                    clearAllTitle()
                    addFragment(settingFragment)
                }
            }
            true
        }
        checkLocationPermission()
    }

    override fun onBackPressed() {

        onbackRemovedTitle()
        Log.e("selected id: ", supportFragmentManager.backStackEntryCount.toString())
        Log.e("count", count.toString())
        val selectedId: Int = supportFragmentManager.backStackEntryCount
        if (selectedId == 1 && count < 1) {
            setItem(R.id.navigation_home)
            count++
        } else {
            super.onBackPressed()
        }


        /* if (count < 1 && supportFragmentManager.backStackEntryCount == 0) {
             if (count == 0) {
                 setItem(R.id.navigation_home)
                 count++
             }

         } else {
             super.onBackPressed()
         }*/
    }

    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.MAIN_ACTIVITY, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.SHOW_APP_LOGO -> appIconBar()
                    EventConstants.HIDE_BOTTOM -> hideBottomNav()
                    EventConstants.SHOW_BOTTOM -> showBottomNav()
                    EventConstants.HIDE_ACTION_BAR -> hideActionBar()
                    EventConstants.SHOW_ACTION_BAR -> showActionBar()
                    EventConstants.ACTION_BAR_TITLE -> appBarTitleName(it.value as String)
                    EventConstants.ACTION_BAR_CART -> appBarCart(it.value as String)
                    EventConstants.Show_ACTION_BAR_TITLE_HIDE_BOTTOM_NAV -> actionbarHideBottom(it.value as String)
                    EventConstants.HIDE_ACTION_BAR_TITLE_SHOW_BOTTOM_NAV -> hideActionBarShowBottomNav()
                    EventConstants.LOG_OUT -> logOut()
                    EventConstants.TOKEN_EXPIRED -> tokenExpired()
                    EventConstants.LANGAUGE_CHANGE -> alertDialogLanguage()
                    EventConstants.ADD_BADGE -> addBadge(it.value as String)
                    EventConstants.FAVOURITE_PAGE -> favouritePage()
                    EventConstants.CHAT_PAGE -> chatPage()
                    EventConstants.ON_BACK -> onBack()
                    EventConstants.CUSTOMER_CHAT_BADGE -> customerAddChatBadge(it.value as Int)
                }
            }
        }
    }

    private fun favouritePage() {
        setItem(R.id.navigation_favourites)
        clearAllTitle()
        addFragment(UFavTabFragment())
        appBarTitleName(Utils.getString(R.string.favourite))
    }

    private fun chatPage() {
        setItem(R.id.navigation_chat)
        clearAllTitle()
        addFragment(UChatFragment())
        appBarTitleName(Utils.getString(R.string.chat))
        customerRemoveChatBadge()
    }
    private fun customerAddChatBadge(value: Int) {
        val badge: BadgeDrawable = binding.bottomNavigationView.getOrCreateBadge(
            R.id.navigation_chat)
        badge.number = value
        badge.isVisible = true
    }
    private fun customerRemoveChatBadge() {
        val badge: BadgeDrawable = binding.bottomNavigationView.getOrCreateBadge(
            R.id.navigation_chat)
        badge.number = 0
        badge.isVisible = false
    }
    private fun addBadge(value: String) {
        binding.bar.cartbtnBadge.setData(value)
    }

    private fun hideAndShowNotificationIcon() {
      binding.bar.headerNotification.hide()
    }

    private fun hideActionBarShowBottomNav() {
        showBottomNav()
        hideActionBar()
    }

    private fun setLan(value: String, dialog: AlertDialog?) {
        RetrofitInstance.clean()
        SharedPref.language(value)
        Utils.dismissAlert(dialog)
        setLanguage(value)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }


    private fun actionbarHideBottom(name: String) {
        hideBottomNav()
        appBarTitleName(name)
    }

    private fun tokenExpired() {
        toast(getString(R.string.token_expired))
        logOut()
    }

    private fun logOut() {

        Utils.clearAllData()
        val intent = Intent(this.applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    private fun addFragment(fragment: Fragment) {
        //To delete all entries from back stack immediately one by one.
        //To delete all entries from back stack immediately one by one.
        val backStackEntry = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackEntry) {
            supportFragmentManager.popBackStackImmediate()
        }
        FragmentUtil.replace(fragment, this)
    }

    private fun setItem(id: Int) {
        binding.bottomNavigationView.selectedItemId = id
    }

    private fun hideBottomNav() {

        binding.bottomNavigationView.hide()
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.show()
    }


    private fun setHeaderHome() {
        setSupportActionBar(binding.bar.toolbar)
        appIconBar()
        binding.bar.let {
            it.headerNotification.setOnClickListener {
                Utils.addReplaceFragment(
                    NotificationsFragment(),
                    (this as FragmentActivity),
                    IDConst.ADD_FRAGMENT
                )

            }

            it.headerBack.setOnClickListener {

                onBack()
            }
            it.headerImage.loadUrl(profileUrl)
            it.cartbtn.setOnClickListener {
                FragmentUtil.cartPage(this)


            }
        }
    }


    private fun clearAllTitle() {
        _oldTitle.clear()
    }

    private fun onBack() {

        onbackRemovedTitle()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun appIconBar() {
        showActionBar()
        binding.bar.headerTitleBackRl.hide()
        binding.bar.headerAppIconRl.show()
       // binding.bar.frameLayout1.hide()
       // binding.bar.headerNotification.show()
    }

    fun appBarTitleName(title: String?) {

        if (title != null) {
            _oldTitle.add(title)
            showActionBar()
            binding.bar.headerName.text = title
            binding.bar.headerTitleBackRl.show()
            binding.bar.headerAppIconRl.hide()
         //   binding.bar.headerNotification.show()
         //   binding.bar.frameLayout1.hide()

            if (title==getString(R.string.notification)){
                binding.bar.headerNotification.hide()
            }
        }

    }
    private fun appBarCart(title: String?) {
        if (title != null) {
            _oldTitle.add(title)
            showActionBar()
            binding.bar.headerName.text = title
            binding.bar.headerTitleBackRl.show()
            binding.bar.headerAppIconRl.hide()
            binding.bar.headerNotification.hide()
            binding.bar.frameLayout1.show()
        }
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun showActionBar() {
        supportActionBar?.show()
    }

    private fun onbackRemovedTitle() {
        checkFragmentVisible()
        if (_oldTitle.isNotEmpty()) {
            _oldTitle.removeAt(_oldTitle.size - 1)
            if (_oldTitle.isNotEmpty()) {
                binding.bar.headerName.text = _oldTitle[_oldTitle.size - 1]
            }
        }
    }


    private fun getCheckedLang(): Int {
        val lan: String = getCurrentLanguage()
        if (lan == IDConst.ENGLISH_CODE) {
            return 0
        } else if (lan == IDConst.ARABIC_CODE) {
            return 1
        }
        return 0
    }

    private fun alertDialogLanguage() {
        var alert: AlertDialog? = null
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialog.setTitle(getString(R.string.Language))
        val items = arrayOf(getString(R.string.English), getString(R.string.Arabic))
        val checkedItem = getCheckedLang()

        alertDialog.setSingleChoiceItems(
            items, checkedItem
        ) { dialog, which ->
            when (which) {
                0 -> {
                    if (checkedItem != 0) {
                        setLan(IDConst.ENGLISH_CODE, alert)
                    } else {
                        Utils.dismissAlert(alert)
                    }
                }
                1 -> {
                    if (checkedItem != 1) {
                        setLan(IDConst.ARABIC_CODE, alert)
                    } else {
                        Utils.dismissAlert(alert)
                    }
                }
            }
        }
        alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    private fun checkFragmentVisible() {
        try {
            val fragment =
                supportFragmentManager.fragments[supportFragmentManager.fragments.size - 2]

            if (fragment is UHomeFragment) {
                appIconBar()
            } else if (fragment is USettingFragment) {
                hideActionBarShowBottomNav()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK") { dialogInterface, i -> //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MapLocationActivity.MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MapLocationActivity.MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MapLocationActivity.MY_PERMISSIONS_REQUEST_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                        val mGpsLocationClient: LocationManager =
                              getSystemService(LOCATION_SERVICE) as LocationManager
                        mGpsLocationClient.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0L ,
                            0f ,
                            LocationListener { location ->
                               Log.d("location>>",""+location.latitude +"  <<"+location.longitude)
                            }

                        )
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()

                }
                return
            }
        }
    }


    override fun onDestroy() {
        EventBus.unRegister(EventPage.MAIN_ACTIVITY)
        BaseConstants.isOneTime = false
        super.onDestroy()
    }
}