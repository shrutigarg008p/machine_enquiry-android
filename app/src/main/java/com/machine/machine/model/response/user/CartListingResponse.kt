package com.machine.machine.model.response.user

import com.google.gson.annotations.SerializedName
import com.machine.machine.model.CommonDTO

/**
 * Created by Amit Rawat on 4/13/2022.
 */
data class CartListingResponse(
    override val status: Int,
    override val message: String,
    var data: CardObj

) : CommonDTO

data class CardObj(

    @SerializedName("items") var items: ArrayList<CartItem>,
    @SerializedName("cart_items") var cartItems: Int,
    @SerializedName("total_sum") var totalSum: String,
    @SerializedName("currency") var currency: String,
    @SerializedName("primary_address") var primaryAddress: PrimaryAddress?
)

class CartItem(

    @SerializedName("id") var id: Long,
    @SerializedName("title") var title: String,
    @SerializedName("short_description") var shortDescription: String,
    @SerializedName("price") var price: String,
    @SerializedName("currency") var currency: String,
    @SerializedName("price_type") var priceType: String,
    @SerializedName("image") var image: String,
    @SerializedName("seller") var seller: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("total_amount") var totalAmount: String,
    @SerializedName("bid") var bid: String = ""

    /*for local use*/
    /*  var addQuanitiy: Boolean = false,
      var removeQuanitiy: Boolean = false,
      var deleteCart: Boolean = false*/
)


data class PrimaryAddress(

    @SerializedName("id") var id: Int,
    @SerializedName("user_id") var userId: Int,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("address_1") var address1: String,
    @SerializedName("address_2") var address2: String,
    @SerializedName("country") var country: String,
    @SerializedName("state") var state: String,
    @SerializedName("city") var city: String,
    @SerializedName("zip") var zip: String,
    @SerializedName("is_primary") var isPrimary: Int,
    @SerializedName("latitude") var latitude: String,
    @SerializedName("longitude") var longitude: String,
    @SerializedName("deleted_at") var deletedAt: String,
    @SerializedName("created_at") var createdAt: String,
    @SerializedName("updated_at") var updatedAt: String

)


