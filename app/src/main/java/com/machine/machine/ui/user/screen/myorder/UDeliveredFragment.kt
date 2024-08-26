package com.machine.machine.ui.user.screen.myorder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.adapter.user.order.UMyOrderAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentShopcategoryTabviewChildBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory


/**
 * Created by Amit Rawat on 2/28/2022.
 */
class UDeliveredFragment :
    BaseFragmentLoader<FragmentShopcategoryTabviewChildBinding>() {
    lateinit var userHomeVerticalAdapter: UMyOrderAdapter
    private lateinit var viewModel: PicsViewModel
    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentShopcategoryTabviewChildBinding.inflate(inflater, container, false)


    override fun setup() {

        verticalAdapter()
    }


    private fun verticalAdapter() {
        val layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.include.rvList.setHasFixedSize(true)
        //binding.include.rvList.addItemDecoration(SpacesItemDecoration(50))
        binding.include.rvList.setLayoutManager(
            layoutManager
        )
        userHomeVerticalAdapter = UMyOrderAdapter()
        setupViewModel()
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
                        userHomeVerticalAdapter.differ.submitList(picsResponse)
                        binding.include.rvList.adapter = userHomeVerticalAdapter
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
                else -> {}
            }
        })
    }


    override fun onRetrybtn() {
        viewModel.getPictures()
        getPictures()
    }

}