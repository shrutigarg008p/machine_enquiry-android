package com.machine.machine.ui.seller.screen.SellerOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.adapter.seller.sellerOrder.SellerOrderListAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentCancelledOrderListBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory

class CancelledOrderListFragment : BaseFragmentLoader<FragmentCancelledOrderListBinding>() {
    private lateinit var viewModel: PicsViewModel
    lateinit var sellerOrderListAdapter: SellerOrderListAdapter

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCancelledOrderListBinding.inflate(inflater, container, false)

    override fun setup() {
        init()
    }

    private fun init() {
        newOrderListAdapter()
        setupViewModel()
    }

    private fun newOrderListAdapter() {
        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.cancelledOrderListRv.setHasFixedSize(true)
        binding.cancelledOrderListRv.layoutManager = layoutManager
        sellerOrderListAdapter = SellerOrderListAdapter("cancelled")
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
                        sellerOrderListAdapter.differ.submitList(picsResponse)
                        binding.cancelledOrderListRv.adapter = sellerOrderListAdapter
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