package com.example.example

import com.google.gson.annotations.SerializedName


data class Additional(

    @SerializedName("quotes") var quotes: Quotes? = Quotes(),
    @SerializedName("orders") var orders: Orders? = Orders(),
    @SerializedName("products") var products: Products? = Products()

)