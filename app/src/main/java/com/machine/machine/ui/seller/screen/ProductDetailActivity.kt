package com.machine.machine.ui.seller.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.R
import com.machine.machine.commonBase.BaseActivity
import com.machine.machine.constant.IDConst
import com.machine.machine.databinding.ActivityProductDetailBinding
import com.machine.machine.model.requestBodies.RegisterPageIntent
import com.machine.machine.model.response.seller.ProductsDto
import com.machine.machine.model.response.user.AdditionalInfo
import com.machine.machine.ui.user.screen.product.detail.AdditionalInfoAdapter
import com.machine.machine.util.addLinearAdapter
import com.machine.machine.util.loadUrl
import com.machine.machine.util.setData

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {
    private lateinit var productsDto: ProductsDto
    lateinit var additionalInfoAdapter: AdditionalInfoAdapter
    private var additionalInfoList: ArrayList<AdditionalInfo> = arrayListOf()


    private fun getData() {
        val intent: Intent = intent
        intent.getParcelableExtra<ProductsDto>(IDConst.INTENT_PRODUCT_DETAIL_DATA).let {
            productsDto = it ?: ProductsDto()
        }
    }

    override fun inflateLayout(layoutInflater: LayoutInflater)= ActivityProductDetailBinding.inflate(layoutInflater)

    override fun viewModelObj() {
        getData()
    }

    override fun setup() {
        binding.title.setData(productsDto.title)
        binding.currency.setData(productsDto.currency)
        binding.price.setData(productsDto.price)
        binding.description.setData(productsDto.short_description)
        binding.productImage.loadUrl(productsDto.image)
        binding.quantityValue.setData(productsDto.qty)

        binding.pdClose.setOnClickListener {
           onBackPressed()
       }
    }




}