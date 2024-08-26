package com.machine.machine.ui.user.screen

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.adapter.user.home.UFavouriteHoriAdapter
import com.machine.machine.adapter.user.home.UShopCatVertAdapter
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUHomeBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.Banner
import com.machine.machine.model.response.user.FavouriteShop
import com.machine.machine.model.response.user.HomeCategory
import com.machine.machine.offline.SharedPref
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.HomeVM
import com.machine.imageslider.FlipperView
import com.machine.machine.constant.BaseConstants
import java.util.*


class UHomeFragment : BaseFragramentLoaderVM<FragmentUHomeBinding>() {

    private lateinit var homeviewModel: HomeVM
    private lateinit var uStoreFavHoriAdapter: UFavouriteHoriAdapter
    private lateinit var UShopCatVertAdapter: UShopCatVertAdapter
    private var favouriteList: ArrayList<FavouriteShop> = ArrayList()
    private var categoryList: ArrayList<HomeCategory> = ArrayList()

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUHomeBinding.inflate(inflater, container, false)

    override fun appHeader() {
        LogUtil.e("language", Locale.getDefault().language)
        LogUtil.e("token", SharedPref.getToken())

    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        homeviewModel = ViewModelProvider(this, factory)[HomeVM::class.java]
        homeviewModel.getHomeData()
        if (!BaseConstants.isOneTime){
            Handler(Looper.getMainLooper()).postDelayed({
                homeviewModel.getCustomerChatData()
            }, 1000)
        }
    }

    override fun viewModelObserver() {
        homeviewModel.homeData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.data != null && response.status == 1) {
                            getSlider(response.data.banners)
                            UShopCatVertAdapter.addData(response.data.categories)

                            EventBus.setBadge(response.data.cartCount.toString())
                            if (response.data.favouriteShops.isNotEmpty()) {
                                binding.favView.show()
                                uStoreFavHoriAdapter.addData(response.data.favouriteShops)
                            } else {
                                binding.favView.hide()
                            }

                        } else {
                            dataNotFound()

                        }
                    }
                }

                is Resource.Error -> {
                    showError(response.errorCode)

                }

                is Resource.Loading -> {
                    showProgess()
                }
            }
        }
        homeviewModel.customerMsgCountData.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.data != null && response.status == 1) {
                          if (response.data?.newMessageCount!=null && response.data?.newMessageCount!=0){
                              EventBus.CustomerChatBadge(response.data?.newMessageCount!!)
                          }

                        } else {
                            dataNotFound()

                        }
                    }
                }

                is Resource.Error -> {
                    showError(response.errorCode)

                }

                is Resource.Loading -> {
                    showProgess()
                }
            }
        }
    }

    override fun viewClick() {
        binding.refreshLayout.setOnRefreshListener {
            homeviewModel.getHomeData()
            binding.refreshLayout.isRefreshing = false
        }

        binding.viewAll.setOnClickListener {
            EventBus.favouritePage()
        }
    }


    override fun setup() {
        favouriteAdapter()
        categoryAdapter()
    }

    override fun onRetrybtn() {
        homeviewModel.getHomeData()
    }

    private fun favouriteAdapter() {
        uStoreFavHoriAdapter = UFavouriteHoriAdapter(favouriteList)
        binding.rvHoriPics.addHorizontalAdapter(context, uStoreFavHoriAdapter)

    }

    private fun categoryAdapter() {
        UShopCatVertAdapter = UShopCatVertAdapter(categoryList)
        binding.rvVerticalPics.addLinearAdapter(context, UShopCatVertAdapter)
    }


    private fun getSlider(banner: ArrayList<Banner>) {
        val flipperLayout = binding.flipperLayout
        flipperLayout.removeAllFlipperViews();
        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in banner.indices) {
            val view = FlipperView(requireContext())
            view.setDescription("Cool" + (i + 1)).hideDesciption()
                //.setDescriptionBackgroundColor(Color.TRANSPARENT)
                .resetDescriptionTextView().setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setImage(banner[i].image) { flipperImageView, image ->

                    flipperImageView.loadRoundUrl(image as String)
                }
            view.setOnFlipperClickListener(object : FlipperView.OnFlipperClickListener {
                override fun onFlipperClick(flipperView: FlipperView) {
                    // Toast.makeText(this@MainActivity, "Here " + (flipperLayout.currentPagePosition + 1), Toast.LENGTH_SHORT).show()
                }
            })
            flipperViewList.add(view)
        }

        flipperLayout.addFlipperViewList(flipperViewList)
        flipperLayout.showInnerPagerIndicator()
        flipperLayout.showCircleIndicator()
        flipperLayout.removeAutoCycle()

    }


}