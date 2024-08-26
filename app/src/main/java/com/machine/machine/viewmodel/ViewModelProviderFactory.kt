package com.machine.machine.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.machine.machine.repository.AppRepository
import com.machine.machine.viewmodel.user.*
import com.machine.machine.viewmodel.user.setting.SettingVM

class ViewModelProviderFactory(
    val app: Application,
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AppRepository()
        if (modelClass.isAssignableFrom(PicsViewModel::class.java)) {
            return PicsViewModel(app, repository) as T
        }

        if (modelClass.isAssignableFrom(LoginVM::class.java)) {
            return LoginVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(OTPSendVM::class.java)) {
            return OTPSendVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(RegisterVM::class.java)) {
            return RegisterVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(ForgetVM::class.java)) {
            return ForgetVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(SettingVM::class.java)) {
            return SettingVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(HomeVM::class.java)) {
            return HomeVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(FavouriteVM::class.java)) {
            return FavouriteVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(ShopCategoryVM::class.java)) {
            return ShopCategoryVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(ShopDetailVM::class.java)) {
            return ShopDetailVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(ProductCategoryVM::class.java)) {
            return ProductCategoryVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(CardItemVM::class.java)) {
            return CardItemVM(app, repository) as T
        }
        if (modelClass.isAssignableFrom(AddressVM::class.java)) {
            return AddressVM(app, repository) as T
        }

        if (modelClass.isAssignableFrom(ChattingVM::class.java)) {
            return ChattingVM(app, repository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}