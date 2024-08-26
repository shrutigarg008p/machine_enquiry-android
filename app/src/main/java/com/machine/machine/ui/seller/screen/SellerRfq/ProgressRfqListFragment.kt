package com.machine.machine.ui.seller.screen.SellerRfq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.adapter.seller.sellerRfq.SellerRfqListAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentProgressRfqListBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory

class ProgressRfqListFragment : BaseFragmentLoader<FragmentProgressRfqListBinding>() {
    private lateinit var viewModel: PicsViewModel
    lateinit var sellerRfqListAdapter: SellerRfqListAdapter

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProgressRfqListBinding.inflate(inflater, container, false)

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
        binding.progressRfqListRv.setHasFixedSize(true)
        binding.progressRfqListRv.layoutManager = layoutManager
       // sellerRfqListAdapter = SellerRfqListAdapter("progress")
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
                        binding.progressRfqListRv.adapter = sellerRfqListAdapter
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