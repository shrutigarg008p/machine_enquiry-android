package com.machine.machine.ui.seller.screen.SellerOrder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.adapter.seller.sellerOrder.SellerOrderDetailAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentSellerOrderDetailsBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory

class SellerOrderDetailsFragment : BaseFragmentLoader<FragmentSellerOrderDetailsBinding>() {
    private lateinit var viewModel: PicsViewModel
    lateinit var sellerOrderDetailAdapter: SellerOrderDetailAdapter

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSellerOrderDetailsBinding.inflate(inflater, container, false)

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
            binding.orderDetailRv.setHasFixedSize(true)
            binding.orderDetailRv.layoutManager = layoutManager
            sellerOrderDetailAdapter = SellerOrderDetailAdapter()
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
                            sellerOrderDetailAdapter.differ.submitList(picsResponse)
                            binding.orderDetailRv.adapter = sellerOrderDetailAdapter
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