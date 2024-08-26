package com.machine.machine.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.machine.machine.R
import com.machine.machine.constant.IDConst
import com.machine.machine.model.UserDTO
import com.machine.machine.model.common.DataBundle
import com.machine.machine.ui.EmptyFragment
import com.machine.machine.ui.user.screen.CartFragment
import com.machine.machine.ui.user.screen.UHomeFragment
import com.machine.machine.ui.user.screen.categoryShop.UShopDetailFragment
import com.machine.machine.ui.user.screen.chat.UChattingFragment
import com.machine.machine.ui.user.screen.product.detail.UProductDetailFragment
import com.machine.machine.ui.user.screen.setting.UAccountFragment

/**
 * Created by Amit Rawat on 4/4/2022.
 */
object FragmentUtil {

    fun getVisibleFragment(activity: FragmentActivity): Fragment {
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment.isVisible) return fragment
        }
        return EmptyFragment()
    }

    fun add(fragment: Fragment, activity: FragmentActivity?) {
        val FRAGMENT_TAG = "add"
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            add(R.id.flFragment, fragment, FRAGMENT_TAG).addToBackStack("TAG")
            commit()
        }

    }

    fun replace(fragment: Fragment, activity: FragmentActivity?) {
        val FRAGMENT_TAG = "single"
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment, fragment, fragment.tag)
            commit()
        }

    }

    fun sendData(obj: DataBundle, fragment: Fragment): Fragment {
        val bundle = Bundle()
        bundle.putSerializable(IDConst.INTENT_DATA, obj)
        fragment.arguments = bundle
        return fragment

    }

    fun getData(bundle: Bundle): DataBundle {
        return bundle.getSerializable(IDConst.INTENT_DATA) as DataBundle
    }

    fun sendID(id: Long?, fragment: Fragment): Fragment {
        val bundle = Bundle()
        bundle.putLong(IDConst.INTENT_ID, id ?: 0)

      fragment.arguments = bundle
        return fragment

    }

    fun sendID(id: Int?, fragment: Fragment): Fragment {
        val bundle = Bundle()
        bundle.putInt(IDConst.INTENT_ID, id ?: 0)

        fragment.arguments = bundle
        return fragment

    }
    fun sendID(url: Int?,productId: String?,type: String?, fragment: Fragment): Fragment {
        val bundle = Bundle()
        bundle.putInt(IDConst.INTENT_ID, url ?: 0)
        bundle.putString("productid", productId ?: "")
        bundle.putString("type", type ?: "")

        fragment.arguments = bundle
        return fragment

    }

    fun sendUser(user: UserDTO, fragment: Fragment): Fragment {
        val bundle = Bundle()
        bundle.putString("name", user.name)
        bundle.putString("email", user.email)
        bundle.putString("phone", user.phone)
        bundle.putString("profile", user.profilePic)

        fragment.arguments = bundle
        return fragment

    }
    /* getIdLong(requireArguments()) in fragment use*/
    fun getIDLong(bundle: Bundle): Long {

        return bundle.getLong(IDConst.INTENT_ID)
    }

    fun getIdInt(bundle: Bundle): Int {
        return bundle.getInt(IDConst.INTENT_ID)
    }

    fun getProductId(bundle: Bundle): String? {
        return bundle.getString("productid")
    }
    fun getType(bundle: Bundle): String? {
        return bundle.getString("type")
    }


    fun getNameString(bundle: Bundle): String {
        return bundle.getString("name")!!
    }
    fun getemailString(bundle: Bundle): String {
        return bundle.getString("email")!!
    }
    fun getphoneString(bundle: Bundle): String {
        return bundle.getString("phone")!!
    }
    fun getprofileString(bundle: Bundle): String {
        return bundle.getString("profile")!!
    }

    fun shopDetailPage(ShopId: Long, context: FragmentActivity) {
        add(
            sendID(ShopId, UShopDetailFragment()),
            context,
        )
    }

    fun productDetailPage(productId: Long?, context: FragmentActivity) {
        add(
            sendID(productId, UProductDetailFragment()),
            context,
        )
    }
     fun chattingPage(chatID: Int?,productId: String?,type:String? ,context: FragmentActivity) {
        add(
            sendID(chatID,productId,type,UChattingFragment()),
            context,
        )
    }

    fun EditFragment(user: UserDTO, context: FragmentActivity) {
        add(
            sendUser(user, UAccountFragment()),
            context,
        )
    }


    fun cartPage(context: FragmentActivity) {
        add(
            CartFragment(),
            context
        )
    }

    fun homePage(context: FragmentActivity) {
        add(
            UHomeFragment(),
            context
        )
    }
}