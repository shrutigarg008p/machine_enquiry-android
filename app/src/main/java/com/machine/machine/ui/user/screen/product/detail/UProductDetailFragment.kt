package com.machine.machine.ui.user.screen.product.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.commonBase.BaseFragramentLoaderVM
import com.machine.machine.databinding.FragmentUProductDetailBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.response.user.AdditionalInfo
import com.machine.machine.model.response.user.ProductDetail
import com.machine.machine.util.*
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ProductCategoryVM
import com.machine.imageslider.FlipperView


/**
 * Created by Amit Rawat on 2/28/2022.
 */

class UProductDetailFragment : BaseFragramentLoaderVM<FragmentUProductDetailBinding>() {
    private lateinit var productVM: ProductCategoryVM
    lateinit var additionalInfoAdapter: AdditionalInfoAdapter
    private var additionalInfoList: ArrayList<AdditionalInfo> = arrayListOf()
    private var productId: Long = 0
    private var sellerId: Long = 0

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUProductDetailBinding.inflate(inflater, container, false)

    override fun appHeader() {
        productId = FragmentUtil.getIDLong(requireArguments())
        EventBus.hideActionBar()
    }

    override fun viewModelObj() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        productVM = ViewModelProvider(this, factory)[ProductCategoryVM::class.java]
        productVM.getProductDetailApi(productId)
    }

    override fun onRetrybtn() {
        productVM.getProductDetailApi(productId)
    }

    override fun viewModelObserver() {
        addFavouriteVM()
        productDetailVM()
        addCartItemVM()
    }

    override fun viewClick() {

        binding.pdClose.setOnClickListener {
            EventBus.onBack()
        }

        binding.fav.btn.setOnClickListener {
            productVM.addFavouriteProduct(productId)
        }

        binding.addToCart.setOnClickListener {
            productVM.addCardItem(productId, 1)
        }

        binding.goToCart.setOnClickListener {
            FragmentUtil.cartPage(context as FragmentActivity)
        }
        binding.chat.setOnClickListener {
            createChatList()
            // EventBus.chatPage()

        }

    }

    override fun setup() {
        additionalInfoAdapter = AdditionalInfoAdapter(additionalInfoList)
        binding.include.rvList.addLinearAdapter(activity, additionalInfoAdapter)

    }


    override fun onDestroyView() {
        EventBus.showActionBar()
        super.onDestroyView()
    }

    private fun productDetailVM() {
        productVM.productDetailVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1 && response.data != null) {

                            setUi(response.data)
                            additionalInfoAdapter.addData(response.data.additionalInfo)
                            sellerId = response.data.sellerId!!
                            if (response.data.additionalImage.isNotEmpty()) {
                                getSlider(response.data.additionalImage)
                            }

                            binding.fav.btn.changeFavBtn(
                                response.data.isFavourite ?: false,
                                activity
                            )

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

    private fun createChatList() {
        var value: Int = sellerId.toInt()
        var sellerIdList: ArrayList<Int> = ArrayList()
        sellerIdList.add(value)

        productVM.addChatRequest(sellerIdList)
        productVM.createChatList.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            progressBtnFavourite(false)
                            FragmentUtil.chattingPage(response.data?.channelId,productId.toString(),"product" ,context as FragmentActivity)
                        } else {
                            progressBtnFavourite( false)
                        }
                    }
                }

                is Resource.Error -> {
                    progressBtnFavourite( false)
                    response.message?.let { message ->
                        context?.toast(message)
                    }
                }

                is Resource.Loading -> {
                    progressBtnFavourite(true)
                }
            }
        }
    }

    private fun addFavouriteVM() {
        productVM.addFavouriteProductVM.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        if (response.status == 1) {
                            progressBtnFavourite(false)
                            response.let { it ->
                                context?.toast(it.message)
                                it.data?.let { it1 ->

                                    binding.fav.btn.changeFavBtn(
                                        it1.isFavourite ?: false, activity
                                    )
                                }
                            }
                        } else {
                            progressBtnFavourite(false)
                        }
                    }
                }

                is Resource.Error -> {
                    progressBtnFavourite(false)
                    response.message?.let { message ->
                        context?.toast(message)
                    }

                }

                is Resource.Loading -> {
                    progressBtnFavourite(true)
                }
            }
        }
    }

    private fun addCartItemVM() {
        productVM.addCartItemLD.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { response ->
                        progressBtnCart(false)
                        if (response.status == 1) {
                            response.let { it ->
                                context?.toast(it.message)
                                EventBus.setBadge(response.data.cartItems.toString())
                            }
                            showCard(false)
                        }
                    }
                }

                is Resource.Error -> {
                    progressBtnCart(false)
                    response.message?.let { message ->
                        context?.toast(message)
                    }

                }

                is Resource.Loading -> {
                    progressBtnCart(true)
                }
            }
        }
    }

    private fun showCard(show: Boolean) {
        if (show) {
            binding.addToCartRL.show()
            binding.goToCart.hide()
        } else {
            binding.addToCartRL.hide()
            binding.goToCart.show()
        }
    }

    private fun progressBtnFavourite(show: Boolean) {
        if (show) {
            binding.fav.btn.hide()
            binding.fav.progress.show()
        } else {
            binding.fav.btn.show()
            binding.fav.progress.hide()
        }
    }

    private fun progressBtnCart(show: Boolean) {
        if (show) {
            binding.addToCart.hide()
            binding.cartpb.show()
        } else {
            binding.addToCart.show()
            binding.cartpb.hide()
        }
    }


    private fun setUi(detail: ProductDetail) {
        binding.title.setData(detail.title)
        binding.currency.setData(detail.currency)
        binding.price.setData(detail.price)
        binding.description.setData(detail.description)
        if (detail.isAddCart) {
            showCard(false)
        } else {
            showCard(true)
        }

    }


    private fun getSlider(banner: ArrayList<String>) {
        val flipperLayout = binding.flipperLayout
        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in banner.indices) {
            val view = FlipperView(requireContext())
            view.setDescription("Cool" + (i + 1)).hideDesciption()
                //.setDescriptionBackgroundColor(Color.TRANSPARENT)
                .resetDescriptionTextView().setImageScaleType(ImageView.ScaleType.FIT_XY)
                .setImage(banner[i]) { flipperImageView, image ->

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