package com.machine.machine.ui .seller.screen.SellerRfq

import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.machine.machine.Quatation
import com.machine.machine.QuatationDetails
import com.machine.machine.adapter.seller.sellerRfq.SellerRfqListAdapter
import com.machine.machine.commonBase.BaseFragmentLoader
import com.machine.machine.databinding.FragmentNewRfqListBinding
import com.machine.machine.model.common.Resource
import com.machine.machine.model.rfq.Data
import com.machine.machine.network.RetrofitInstance
import com.machine.machine.viewmodel.PicsViewModel
import com.machine.machine.viewmodel.ViewModelProviderFactory
import com.machine.machine.viewmodel.user.ProductCategoryVM
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class NewRfqListFragment : BaseFragmentLoader<FragmentNewRfqListBinding>() {
    private lateinit var viewModel: PicsViewModel
    lateinit var sellerRfqListAdapter: SellerRfqListAdapter
    var datalist: ArrayList<Data> = ArrayList()
    var orderList = ArrayList<String>()

    //  val datalist = arrayListOf<Data>()

    private lateinit var productVM: ProductCategoryVM

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewRfqListBinding.inflate(inflater, container, false)

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
        binding.newRfqListRv.setHasFixedSize(true)
        binding.newRfqListRv.layoutManager = layoutManager
        //  sellerRfqListAdapter = SellerRfqListAdapter("new")
    }

    private fun setupViewModel() {
        val factory = ViewModelProviderFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[PicsViewModel::class.java]


        val factory1 = ViewModelProviderFactory(requireActivity().application)
        productVM = ViewModelProvider(this, factory1)[ProductCategoryVM::class.java]
        android.os.Handler().postDelayed({
//                                FragmentUtil.chattingPage(productVM.createChatList.value?.data?.data?.channelId?.toInt(),holder.binding.quoteRequestId.text.toString(),"order" ,holder.itemView.context as FragmentActivity)

            getPictures1()


        }, 2000)
    }


    private fun getPictures() {
        viewModel.picsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    response.data?.let { picsResponse ->
                        //    sellerRfqListAdapter.differ.submitList(picsResponse)
                        binding.newRfqListRv.adapter = sellerRfqListAdapter
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
        // getPictures1();
    }



    private fun getPictures1() {

        val call : retrofit2.Call<Quatation> = RetrofitInstance.ClientApi!!.rfqData1()

        call.enqueue(object : Callback<Quatation>{
            override fun onResponse(call: retrofit2.Call<Quatation>, response: Response<Quatation>
            ) {

                if( response.body()?.data!!.size >0)
                {
                    response.body()?.data?.forEach {
                        val call1 : retrofit2.Call<QuatationDetails> = RetrofitInstance.ClientApi!!.rfqDeatilsData1(it.orderNo.toString())
                        call1.enqueue(object:Callback<QuatationDetails>{
                            override fun onResponse(
                                call: retrofit2.Call<QuatationDetails>,
                                response1: Response<QuatationDetails>
                            ) {
                                response1.body()!!.data?.let { it1 -> datalist.add(it1) }
                                showContent()
                                var list:List<Data> =datalist.sortedWith(compareByDescending { it.date })
                                sellerRfqListAdapter = SellerRfqListAdapter("new",list,viewModel,productVM,context )
                                binding.newRfqListRv.adapter = sellerRfqListAdapter
                                sellerRfqListAdapter.notifyDataSetChanged()
                            }

                            override fun onFailure(
                                call: retrofit2.Call<QuatationDetails>,
                                t: Throwable
                            ) {
                                TODO("Not yet implemented")
                            }


                        })


                    }

                }
                else{
                    dataNotFound()
                }

            }

            override fun onFailure(call: retrofit2.Call<Quatation>, t: Throwable) {
                dataNotFound()
            }

        })


     /*   viewModel.QutationData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    showContent()
                    var i=0
                    response.data?.let { Q ->
                        if(Q.data.size>0)
                        {
                            Q.data.forEach {
                                if(orderList.size==Q.data.size)
                                {

                                }
                                else{
                                    if(orderList.contains(it.orderNo.toString()))
                                    {

                                    }
                                    else{
                                        orderList.add(it.orderNo.toString());
                                        viewModel.getrfqdetails(it.orderNo.toString());
                                    }


                                }
                            }
                        }
                        else{
                            orderList.clear();
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
        getData()
*/



    }




    private fun getData() {
        //datalist.clear()
        viewModel.QutationDetailsData.observe(this, Observer {response1 ->
            showContent()

            when(response1)
            {
                is Resource.Success->{
                    response1.data?.let { Que->
                        Que.data?.let {itt->
                            if(datalist.contains(itt))
                            {

                            }
                            else
                            {
                                datalist.add(itt)
                            }

                        };

                    }
                }


            }



            var datalist1: MutableList<Data> = mutableListOf()
            datalist1.clear()
            var i=0;
            datalist.forEach { data->

                if(orderList.size==i)
                {
                    Log.e("in if",i.toString())
                }
                else
                {
                    Log.e("in else ",i.toString())
                    datalist1.add(data)
                }
                i=i+1;
            }
         /*   sellerRfqListAdapter = SellerRfqListAdapter("new",datalist1.toMutableList(),viewModel,productVM,context )
            binding.newRfqListRv.adapter = sellerRfqListAdapter
            sellerRfqListAdapter.notifyDataSetChanged()*/
        })
        // getPictures1();
    }


    override fun onRetrybtn() {
        viewModel.getRFQDATA()
        getPictures1()
    }
}