package com.machine.machine.ui.seller.screen.SellerRfq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.adapter.seller.sellerRfq.SellerRfqListAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentClosedRfqListBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory

class ClosedRfqListFragment : BaseFragmentLoader<FragmentClosedRfqListBinding>() {
        private lateinit var viewModel: PicsViewModel
        lateinit var sellerRfqListAdapter: SellerRfqListAdapter

        override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentClosedRfqListBinding.inflate(inflater, container, false)

        override fun setup() {
            init()
        }

        private fun init() {
            newRfqAdapter()
            setupViewModel()
        }

        private fun newRfqAdapter() {
            val layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            binding.closedRfqListRv.setHasFixedSize(true)
            binding.closedRfqListRv.layoutManager = layoutManager
       //     sellerRfqListAdapter = SellerRfqListAdapter("closed")
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
                          //  sellerRfqListAdapter.differ.submitList(picsResponse)
                            binding.closedRfqListRv.adapter = sellerRfqListAdapter
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