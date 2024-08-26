package com.machine.machine.util

import androidx.annotation.LayoutRes

/**
 * Created by Amit Rawat on 2/24/2022.
 */
interface ViewType<out T> {

    @LayoutRes
    fun layoutId(): Int

    fun data(): T

    fun isUserInteractionEnabled() = true
}