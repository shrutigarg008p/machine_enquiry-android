package com.machine.machine.ui.seller.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.adapter.seller.sellerOrderAndRfq.SellerOrderAdapter
import com.machine.machine.adapter.seller.sellerOrderAndRfq.SellerRfqAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentSellerOrderBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory

class SellerOrderFragment : BaseFragmentLoader<FragmentSellerOrderBinding>() {
    private lateinit var viewModel: PicsViewModel
    lateinit var sellerNewOrderAdapter: SellerOrderAdapter
    lateinit var sellerNewRfqAdapter: SellerRfqAdapter

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSellerOrderBinding.inflate(inflater, container, false)

    override fun setup() {
        init()
    }

    private fun init() {
        newOrderAdapter()
        newRfqAdapter()
        setupViewModel()
    }

    private fun newOrderAdapter() {
        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.sellerNewOrderRv.setHasFixedSize(true)
        binding.sellerNewOrderRv.layoutManager = layoutManager
        sellerNewOrderAdapter = SellerOrderAdapter()
    }

    private fun newRfqAdapter() {
        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.sellerNewRfqRv.setHasFixedSize(true)
        binding.sellerNewRfqRv.layoutManager = layoutManager
        sellerNewRfqAdapter = SellerRfqAdapter()
    }

    private fun setupViewModel() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[PicsViewModel::class.java]
        getPictures()
    }

    private fun getPictures() {
        viewModel.picsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { picsResponse ->
                        // new order adapter
                        sellerNewOrderAdapter.differ.submitList(picsResponse)
                        binding.sellerNewOrderRv.adapter = sellerNewOrderAdapter
                        // new rfq adapter
                        sellerNewRfqAdapter.differ.submitList(picsResponse)
                        binding.sellerNewRfqRv.adapter = sellerNewRfqAdapter
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


    override fun onRetrybtn() {
        viewModel.getPictures()
        getPictures()
    }

}