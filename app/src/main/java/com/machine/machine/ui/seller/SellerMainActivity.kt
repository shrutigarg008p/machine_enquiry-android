package com.machine.machine.ui.seller

import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import com.amitrawat.postevents.PostEvent
import com.google.android.material.badge.BadgeDrawable
import com.machine.machine.EventConstant.EventConstants
import com.machine.machine.EventConstant.EventPage
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.BaseConstants
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivitySellerMainBinding
import com.machine.machine.ui.seller.screen.SellerHomeFragment
import com.machine.machine.ui.seller.screen.SellerOrder.SellerOrderTabsFragment
import com.machine.machine.ui.seller.screen.SellerRfq.SellerRfqTabsFragment
import com.machine.machine.ui.user.screen.NotificationsFragment
import com.machine.machine.ui.user.screen.chat.UChatFragment
import com.machine.machine.ui.user.screen.setting.USettingFragment
import com.machine.machine.util.*

class SellerMainActivity : BaseActivity<ActivitySellerMainBinding>() {
    private val _oldTitle: ArrayList<String> = ArrayList()
    override fun inflateLayout(layoutInflater: LayoutInflater) =
        ActivitySellerMainBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        getSubscribeData()
    }

    private fun setHeaderHome() {

        setSupportActionBar(binding.bar.toolbar)
        appBarIcon()
        //  binding.bar.headerNotification.show()
        binding.bar.frameLayout1.hide()
        binding.bar.let {
            it.headerNotification.setOnClickListener {
                Utils.addReplaceFragment(
                    NotificationsFragment(),
                    (this as FragmentActivity),
                    IDConst.ADD_FRAGMENT
                )

            }


        }
    }

    override fun setup() {
        setHeaderHome()

        val firstFragment = SellerHomeFragment()
        val secondFragment = SellerRfqTabsFragment()
        val thirdFragment = SellerOrderTabsFragment()
        val fourthFragment = UChatFragment()

        val fifthFragment = USettingFragment()
        Utils.addReplaceFragment(
            firstFragment,
            this,
            IDConst.REPLACE_FRAGMENT
        )
        binding.sellerBottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.seller_home -> {
                    clearAllTitle()
                    Utils.addReplaceFragment(firstFragment, this, IDConst.REPLACE_FRAGMENT)
                    appBarIcon()
                    showActionBar()
                }
                R.id.seller_rfq ->{
                    clearAllTitle()
                    appBarTitleName(Utils.getString(R.string.request_for_quote))
                    Utils.addReplaceFragment(SellerRfqTabsFragment(), (this as FragmentActivity), IDConst.REPLACE_FRAGMENT)
                    sellerRemoveRfqBadge()
                }
                R.id.seller_my_orders ->{
                    clearAllTitle()
                    Utils.addReplaceFragment(thirdFragment, this, IDConst.REPLACE_FRAGMENT)
                    appBarTitleName(Utils.getString(R.string.my_orders))
                }

                R.id.seller_chat -> {
                    clearAllTitle()
                    appBarTitleName(Utils.getString(R.string.chat))
                    Utils.addReplaceFragment(fourthFragment, this, IDConst.REPLACE_FRAGMENT)
                    sellerRemoveChatBadge()
                }
                R.id.seller_setting -> {

                    clearAllTitle()
                    Utils.addReplaceFragment(fifthFragment, this, IDConst.REPLACE_FRAGMENT)
                    hideActionBar()
                }
            }
            true
        }
    }

    private fun getSubscribeData() {
        PostEvent.subscribe(EventPage.MAIN_ACTIVITY, this) {
            it.runAndConsume {
                when (it.id) {
                    EventConstants.SHOW_BOTTOM -> showBottomNav()
                    EventConstants.Show_ACTION_BAR_TITLE_HIDE_BOTTOM_NAV -> actionbarHideBottom(it.value as String)
                    EventConstants.HIDE_ACTION_BAR_TITLE_SHOW_BOTTOM_NAV -> hideActionBarShowBottomNav()
                    EventConstants.SELLER_CHAT_BADGE -> sellerAddChatBadge(it.value as Int)
                    EventConstants.SELLER_RFQ_BADGE -> sellerAddRfqBadge(it.value as Int)
                }
            }
        }
    }

    private fun actionbarHideBottom(name: String) {
        hideBottomNav()
        appBarTitleName(name)
    }

    private fun hideActionBarShowBottomNav() {
        showBottomNav()
        hideActionBar()
    }

    private fun hideBottomNav() {
        binding.sellerBottomNavigationView.hide()
    }

    private fun showBottomNav() {
        binding.sellerBottomNavigationView.show()
    }

    private fun sellerAddChatBadge(value: Int) {
        val badge: BadgeDrawable = binding.sellerBottomNavigationView.getOrCreateBadge(
            R.id.seller_chat)
        badge.number = value
        badge.isVisible = true
    }
    private fun sellerRemoveChatBadge() {
        val badge: BadgeDrawable = binding.sellerBottomNavigationView.getOrCreateBadge(
            R.id.seller_chat)
        badge.number = 0
        badge.isVisible = false
    }

    private fun sellerAddRfqBadge(value: Int) {
        val badge: BadgeDrawable = binding.sellerBottomNavigationView.getOrCreateBadge(
            R.id.seller_rfq)
        badge.number = value
        badge.isVisible = true
    }
    private fun sellerRemoveRfqBadge() {
        val badge: BadgeDrawable = binding.sellerBottomNavigationView.getOrCreateBadge(
            R.id.seller_rfq)
        badge.number = 0
        badge.isVisible = false
    }

    fun appBarTitleName(title: String?) {

        if (title != null) {
            _oldTitle.add(title)
            showActionBar()
            binding.bar.headerName.text = title
            binding.bar.headerTitleBackRl.show()
            binding.bar.headerAppIconRl.hide()
            //   binding.bar.headerNotification.show()
            binding.bar.frameLayout1.hide()

            if (title==getString(R.string.notification)){
                binding.bar.headerNotification.hide()
            }
        }

    }
    private fun appBarIcon() {
        binding.bar.headerAppIconRl.show()
        binding.bar.headerTitleBackRl.hide()
    }

    private fun clearAllTitle() {
        _oldTitle.clear()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun showActionBar() {
        supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        BaseConstants.isOneTime = false
    }
}