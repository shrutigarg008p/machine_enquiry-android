package com.machine.machine.ui.seller.screen

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.machine.machine.EventConstant.EventBus
import com.machine.machine.adapter.seller.sellerHome.SellerHomeVerticalAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.commonBase.SpacesItemDecoration
import com.machine.machine.constant.BaseConstants
import com.machine.machine.databinding.FragmentSellerHomeBinding
import com.machine.machine.model.common.DataField
import com.machine.machine.model.common.Resource
import com.machine.machine.offline.SharedPref
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory
import kotlin.collections.ArrayList


class SellerHomeFragment : BaseFragmentLoader<FragmentSellerHomeBinding>() {
    private lateinit var viewModel: PicsViewModel
    private lateinit var mBottomSheetBehavior1: BottomSheetBehavior<View>

    //  private lateinit var shopModel: PicsViewModel

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSellerHomeBinding.inflate(inflater, container, false)


    override fun onResume() {
        super.onResume()
        val factory = ViewModelProviderFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[PicsViewModel::class.java]
        getShopsData()
        if (!BaseConstants.isOneTime) {
            getRfqAndChat()
        }
    }


    override fun setup() {
        val bottomSheet: View = binding.bottomSheet1;
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet)
        mBottomSheetBehavior1.setHideable(true)
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN)

        init()
    }

    private fun init() {
        SharedPref.setShopId(null)
        setupViewModel()
        verticalAdapter()

        binding.shopFilter.setOnClickListener {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        binding.cross.setOnClickListener {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }


    private fun verticalAdapter() {
        val layoutManager = GridLayoutManager(
            context,
            2
        )
        binding.rvList.setHasFixedSize(true)
        binding.rvList.setLayoutManager(
            layoutManager
        )
        binding.rvList.addItemDecoration(SpacesItemDecoration(2, 40, false));


        /* binding.selectAll.setOnClickListener(View.OnClickListener {
             if (binding.selectAll.isChecked()) {
                 sellerHomeVerticalAdapter.selectAll()
             } else {
                 sellerHomeVerticalAdapter.unselectall()
             }
         })*/
    }


    private fun setupViewModel() {


//        getPictures()


        binding.btn.commonBtn.setOnClickListener {
           if(binding.subCategory.tag==null && binding.category.tag==null)
           {
               viewModel.getAllProduct(Integer.parseInt(binding.shops.tag.toString()),1,0);
               mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN)

           }
            else if(binding.subCategory.tag==null)
           {
               viewModel.getAllProduct(Integer.parseInt(binding.shops.tag.toString()),1,0);
               mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN)
           }
            else{
               viewModel.getAllProduct( Integer.parseInt(binding.shops.tag.toString()) , Integer.parseInt(binding.subCategory.tag.toString()) , 0, 1);
               mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN)

           }

        }

        binding.tvResetFilter.setOnClickListener{
           
            getShopsData()
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN)

        }

    }


/*    fun getShops() {
        viewModelScope.launch {
            updateAddressMLD.postValue(Resource.Loading())

            try {
                val response = appRepository.updateAddress(getData(body))
                updateAddressMLD.postValue(
                    ResUtil.handleResponse(
                        response,
                        getApplication<MyApplication>()
                    )
                )
            } catch (t: Throwable) {

                updateAddressMLD.postValue(
                    ResUtil.getErrorEvent(
                        t,
                        getApplication<MyApplication>()
                    )
                )

            }
        }
    }*/

    private fun getAllProductsResponse() {
        viewModel.productsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()

                    response.data?.let { productsList ->
                        if (productsList!= null){
                        var sellerHomeVerticalAdapter  : SellerHomeVerticalAdapter
                        sellerHomeVerticalAdapter = SellerHomeVerticalAdapter(productsList.data , viewModel , context)
                        binding.rvList.adapter = sellerHomeVerticalAdapter
                        }else{
                            dataNotFound()
                        }
                    }
                }
                is Resource.Error -> {
                    response.let { message ->

                        showError(message.errorCode)
                        //  binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }
                is Resource.Loading -> {
                    //  showProgess()
                }
            }
        })
    }




//    private fun getPictures() {
//        viewModel.picsData.observe(this, Observer { response ->
//            when (response) {
//                is Resource.Success -> {
//                    showContent()
//                    response.data?.let { picsResponse ->
//                        sellerHomeVerticalAdapter.differ.submitList(picsResponse)
//                        binding.rvList.adapter = sellerHomeVerticalAdapter
//                    }
//                }
//
//                is Resource.Error -> {
//                    response.let { message ->
//
//                        showError(message.errorCode)
//                        //  binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
//                    }
//
//                }
//
//                is Resource.Loading -> {
//                    //  showProgess()
//                }
//            }
//        })
//    }


    private fun getShopsData() {
        viewModel.shopsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    val objItem = ArrayList<DataField>()

                    response.data?.data.let {
                        if (it != null && it.size>0) {
                            SharedPref.setShopId(it.get(0).id.toString())
                            viewModel.getAllProduct(it.get(0).id.toString().toInt(),1,0);
                            getAllProductsResponse()
                            binding.selectShopName.setText("Shop : "+it.get(0).shopName.toString())
                            for (obj in it) {
                                objItem.add(DataField(obj.id.toString(), obj.shopName))
                            }
                        }else{
                            dataNotFound()
                        }
                        shopTypeDropdown(objItem)

                    }
                }

                is Resource.Error -> {
                    response.let { message ->

                        showError(message.errorCode)
                        //  binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    //  showProgess()
                }
            }
        })

    }

    fun getRfqAndChat(){
        viewModel.sellerChatCount.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()

                    response.data?.data.let {
                        if (it != null ) {
                            if (it.newMessageCount!=null && it.newMessageCount !=0){
                                EventBus.sellerChatBadge(it.newMessageCount!!)
                            }

                            if (it.newQuotation!=null && it.newQuotation!=0){

                                Handler(Looper.getMainLooper()).postDelayed({
                                    EventBus.sellerRfqBadge(it.newQuotation!!)
                                }, 1000)

                            }

                            BaseConstants.isOneTime = !BaseConstants.isOneTime
                        }
                    }
                }

                is Resource.Error -> {
                    response.let { message ->

                        showError(message.errorCode)
                        //  binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    //  showProgess()
                }
            }
        })
    }


    private fun getCategoryData(id: String) {
        viewModel.shopsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()

                    response.data?.data.let {
                        if (it != null) {
                            for (obj in it) {

                                if (obj.id.toString() == id) {
                                    val objItem = ArrayList<DataField>()
                                    for (obj1 in obj.categories) {
                                        objItem.add(DataField(obj1.id.toString(), obj1.title))

                                    }
                                    categoryTypeDropdown(objItem)
                                }
                            }
                        }


                    }
                }

                is Resource.Error -> {
                    response.let { message ->

                        showError(message.errorCode)
                        //  binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    //  showProgess()
                }
            }
        })
    }


    private fun getSubCategoryData(id: String, categoryid: String) {
        viewModel.shopsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()

                    response.data?.data.let {
                        if (it != null) {
                            for (obj in it) {

                                if (obj.id.toString() == id) {
                                    for (obj1 in obj.categories) {
                                        if (obj1.id.toString() == categoryid) {
                                            val objItem = ArrayList<DataField>()

                                            for (obj2 in obj1.children) {
                                                objItem.add(
                                                    DataField(
                                                        obj2.id.toString(),
                                                        obj2.title
                                                    )
                                                )

                                            }
                                            subCategoryTypeDropdown(objItem)
                                        }

                                    }

                                }
                            }
                        }


                    }
                }

                is Resource.Error -> {
                    response.let { message ->

                        showError(message.errorCode)
                        //  binding.rootLayout.errorSnack(message, Snackbar.LENGTH_LONG)
                    }

                }

                is Resource.Loading -> {
                    //  showProgess()
                }
            }
        })
    }


    private fun shopTypeDropdown(listItem: ArrayList<DataField>) {
        with(binding) {
            shops.setAdapter(requireContext(), listItem)

        }
        binding.shops.setOnItemClickListener { parent, view, position, id ->
            //  LogUtil.e("number:  ", DataHolder.getUserType()[position].id);
            val objItem = ArrayList<DataField>()
            binding.selectShopName.setText("Shop : "+listItem[position].value)
            binding.shops.tag = listItem[position].id
            SharedPref.setShopId(binding.shops.tag as String?)
            categoryTypeDropdown(objItem)
            subCategoryTypeDropdown(objItem)
            getCategoryData(listItem[position].id.toString())


        }
    }

    private fun categoryTypeDropdown(listItem: ArrayList<DataField>) {
        binding.category.setAdapter(requireContext(), listItem)
        binding.category.setOnItemClickListener { parent, view, position, id ->
            binding.category.tag = listItem[position].id
            val objItem = ArrayList<DataField>()
            subCategoryTypeDropdown(objItem)
            getSubCategoryData(binding.shops.tag.toString(), listItem[position].id.toString())

        }
    }

    private fun subCategoryTypeDropdown(listItem: ArrayList<DataField>) {
        binding.subCategory.setAdapter(requireContext(), listItem)
        binding.subCategory.setOnItemClickListener { parent, view, position, id ->
            binding.subCategory.tag = listItem[position].id

        }
    }

    override fun onRetrybtn() {
        viewModel.getAllProduct(0 , 0 , 0, 1);

    }



}
