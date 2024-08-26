package com.machine.machine.EventConstant

import androidx.core.app.ActivityCompat
import com.amitrawat.postevents.ConsumableEvent
import com.amitrawat.postevents.PostEvent


object EventBus {

    fun unRegister(subject: String) {
        PostEvent.unregister(subject)
    }

    fun showBottom() =
        PostEvent.publish(
            EventPage.MAIN_ACTIVITY,
            ConsumableEvent(id = EventConstants.SHOW_BOTTOM)
        )

    fun hideActionBar() =
        PostEvent.publish(
            EventPage.MAIN_ACTIVITY,
            ConsumableEvent(id = EventConstants.HIDE_ACTION_BAR)
        )

    fun showActionBar() =
        PostEvent.publish(
            EventPage.MAIN_ACTIVITY,
            ConsumableEvent(id = EventConstants.SHOW_ACTION_BAR)
        )

    fun actionBarTitle(title: String) =
        PostEvent.publish(
            EventPage.MAIN_ACTIVITY,
            ConsumableEvent(id = EventConstants.ACTION_BAR_TITLE, value = title)
        )

    fun actionBarWithCart(title: String) =
        PostEvent.publish(
            EventPage.MAIN_ACTIVITY,
            ConsumableEvent(id = EventConstants.ACTION_BAR_CART,value = title)
        )

    fun showActionBarTitleHideBottomNav(title: String) = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.Show_ACTION_BAR_TITLE_HIDE_BOTTOM_NAV, value = title)
    )



    fun hideActionBarTitleShowBottomNav() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.HIDE_ACTION_BAR_TITLE_SHOW_BOTTOM_NAV)
    )

    fun logOut() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.LOG_OUT)
    )

    fun tokenExpired() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.LOG_OUT)
     /*   EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.TOKEN_EXPIRED)*/
    )

    fun setLanguage() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.LANGAUGE_CHANGE)
    )


    fun onBack() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY, ConsumableEvent(id = EventConstants.ON_BACK)
    )

    fun showAppLogo() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY, ConsumableEvent(id = EventConstants.SHOW_APP_LOGO)
    )

    fun addRemoveFavourite(eventItem: EventItem) = PostEvent.publish(
        EventPage.SHOP_CATEGORY_TAB,
        ConsumableEvent(id = EventConstants.ADD_REMOVE_FAVOURITE, value = eventItem)
    )

    fun removedShopFav(eventItem: EventItem) = PostEvent.publish(
        EventPage.FAVOURITE_ACTIVITY,
        ConsumableEvent(id = EventConstants.REMOVED_SHOP, value = eventItem)
    )

    fun removedProductFav(eventItem: EventItem) = PostEvent.publish(
        EventPage.FAVOURITE_ACTIVITY,
        ConsumableEvent(id = EventConstants.REMOVED_PRODUCT, value = eventItem)
    )

    fun addRemoveProductFav(eventItem: EventItem) = PostEvent.publish(
        EventPage.PRODUCT_CATEGORY_TAB,
        ConsumableEvent(id = EventConstants.ADD_REMOVE_FAVOURITE, value = eventItem)
    )

    fun addCartQuanity(eventItem: EventItem) = PostEvent.publish(
        EventPage.CART_ACTIVITY,
        ConsumableEvent(id = EventConstants.ADD_QUANTITY_CART, value = eventItem)
    )

    fun removeCartQuanity(eventItem: EventItem) = PostEvent.publish(
        EventPage.CART_ACTIVITY,
        ConsumableEvent(id = EventConstants.REMOVE_QUANTITY_CART, value = eventItem)
    )

    fun deleteCart(eventItem: EventItem) = PostEvent.publish(
        EventPage.CART_ACTIVITY,
        ConsumableEvent(id = EventConstants.DELETE_CART, value = eventItem)
    )

    fun onBackCart() = PostEvent.publish(
        EventPage.CART_ACTIVITY,
        ConsumableEvent(id = EventConstants.ON_BACK)
    )

    fun setBadge(value: String) = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.ADD_BADGE, value = value)
    )

    fun favouritePage() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.FAVOURITE_PAGE)
    )

    fun chatPage() = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.CHAT_PAGE)
    )

    fun CustomerChatBadge(value: Int) = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.CUSTOMER_CHAT_BADGE, value = value)
    )

    fun sellerChatBadge(value: Int) = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.SELLER_CHAT_BADGE, value = value)
    )

    fun sellerRfqBadge(value: Int) = PostEvent.publish(
        EventPage.MAIN_ACTIVITY,
        ConsumableEvent(id = EventConstants.SELLER_RFQ_BADGE, value = value)
    )

    fun deleteAddress(eventItem: EventItem) = PostEvent.publish(
        EventPage.ADDRESS_ACTIVITY,
        ConsumableEvent(id = EventConstants.DELETE_ADDRESS, value = eventItem)
    )

    fun EditAddress(eventItem: EventItem) = PostEvent.publish(
        EventPage.ADDRESS_ACTIVITY,
        ConsumableEvent(id = EventConstants.EDIT_ADDRESS, value = eventItem)
    )

    fun setPrimaryAddress(eventItem: EventItem) = PostEvent.publish(
        EventPage.ADDRESS_ACTIVITY,
        ConsumableEvent(id = EventConstants.SET_PRIMARY_ADDRESS, value = eventItem)
    )


}