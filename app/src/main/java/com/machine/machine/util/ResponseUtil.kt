package com.machine.machine.util

import android.content.Context
import android.util.Log
import com.google.gson.JsonSyntaxException
import com.machine.machine.R
import com.machine.machine.constant.IDConst
import com.machine.machine.model.common.ErrorResult
import com.machine.machine.model.common.Resource
import com.machine.machine.ui.MyApplication
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Created by Amit Rawat on 2/28/2022.
 */
object ResUtil {

    fun <T : Any> handleResponse(
        response: Response<T>,
        context: MyApplication
    ): Resource<T> {

        if (Utils.hasInternetConnection(context)) {
            if (response.isSuccessful) {
                if (response.code() == 200) {
                    response.body()?.let { resultResponse ->
                        return Resource.Success(resultResponse)
                    }
                }
                return Resource.Error(
                    context.getString(
                        R.string.Something_went_wrong
                    ), IDConst.SOMETHING_WENT_WRONG
                )
            }
            response.errorBody()?.let {
                try {
                    val errorString = it.string()
                    val errorObject = JSONObject(errorString)
                    return Resource.Error(
                        if (errorObject.has(IDConst.JSON_MESSAGE)) errorObject.getString(IDConst.JSON_MESSAGE) else context.getString(
                            R.string.Something_went_wrong
                        ), IDConst.SOMETHING_WENT_WRONG
                    )

                } catch (ignored: JsonSyntaxException) {
                    return Resource.Error(
                        context.getString(
                            R.string.Something_went_wrong
                        ), IDConst.SOMETHING_WENT_WRONG
                    )
                }
            }

            return Resource.Error(
                context.getString(
                    R.string.Something_went_wrong
                ), IDConst.SOMETHING_WENT_WRONG
            )
        } else {

            return Resource.Error(
                context.getString(
                    R.string.no_internet_connection_desc
                ), IDConst.NO_INTERNET
            )

        }

    }

    fun <T : Any> getErrorEvent(t: Throwable, context: Context): Resource<T> {
        Log.e("error", t.printStackTrace().toString())
        val msg: String
        when (t) {
            is IOException -> {

                return Resource.Error(
                    context.getString(
                        R.string.network_failure
                    ), IDConst.SOMETHING_WENT_WRONG
                )

            }
            is HttpException -> {
                return Resource.Error(
                    context.getString(
                        R.string.network_failure
                    ), IDConst.SOMETHING_WENT_WRONG
                )
            }
            else -> {
                return Resource.Error(
                    context.getString(
                        R.string.network_failure
                    ), IDConst.SOMETHING_WENT_WRONG
                )


            }
        }

        // return Resource.Error(msg, IDConst.SERVER_MAINTENANCE)
    }

    fun getErrorObj(errorCode: Int?, context: Context): ErrorResult {
        when (errorCode) {
            IDConst.NO_INTERNET ->
                return ErrorResult(
                    title = context.getString(R.string.no_connection),
                    desc = context.getString(R.string.no_internet_connection_desc),
                    img = R.drawable.nowifi
                )

            IDConst.EMPTY_CART -> return ErrorResult(
                title = context.getString(R.string.Empty_Cart_title),
                desc = context.getString(R.string.Empty_Cart_desc),
                img = R.drawable.shopping_cart
            )
            IDConst.DATA_NOT_FOUND -> return ErrorResult(
                title = context.getString(R.string.no_data_found),
                desc = "",
                img = R.drawable.no_data
            )
            IDConst.SERVER_MAINTENANCE -> return ErrorResult(
                title = context.getString(R.string.under_maintenance),
                desc = context.getString(R.string.under_maintenance_desc),
                img = R.drawable.maintenance
            )
            IDConst.SOMETHING_WENT_WRONG -> return ErrorResult(
                title = context.getString(R.string.server_error),
                desc = context.getString(R.string.something_went_wrong),
                img = R.drawable.maintenance
            )
            else -> { // Note the block
                return ErrorResult(
                    title = context.getString(R.string.server_error),
                    desc = context.getString(R.string.something_went_wrong),
                    img = R.drawable.maintenance

                )
            }
        }
    }
}