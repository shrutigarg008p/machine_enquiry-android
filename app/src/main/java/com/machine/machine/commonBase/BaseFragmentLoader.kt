package com.machine.machine.commonBase


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.machine.machine.R
import com.machine.machine.constant.IDConst
import com.machine.machine.util.ResUtil
import com.machine.machine.util.hide
import com.machine.machine.util.show


abstract class BaseFragmentLoader<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    private var progessLayout: ViewGroup? = null
    private var parentViewGroup: ViewGroup? = null
    private var ErrorLayout: ViewGroup? = null
    private var ErrorTextTitle: TextView? = null
    private var ErrorTextDesc: TextView? = null
    private var ErrorImage: ImageView? = null
    private var ErrorBtn: Button? = null
/*    private var _visible: Boolean? = null
    val visible get() = _visible!!*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val parentView: View = inflater.inflate(R.layout.frag_loader, null)
        parentViewGroup = parentView.findViewById<View>(R.id.child_parent_ll) as LinearLayout
        progessLayout = parentView.findViewById(R.id.child_parent_ll_progress)
        ErrorLayout = parentView.findViewById(R.id.child_parent_ll_error)
        ErrorTextTitle = parentView.findViewById(R.id.child_parent_ll_error_text)
        ErrorTextDesc = parentView.findViewById(R.id.child_parent_ll_error_text_des)
        ErrorImage = parentView.findViewById(R.id.child_parent_ll_error_img)
        ErrorBtn = parentView.findViewById(R.id.child_parent_ll_error_btn)

        setOnclick()
        _binding = inflateViewBinding(inflater, container)
        val childView = binding.getRoot()
        /*  if (parentViewGroup?.getParent() != null) {
              (parentViewGroup?.getParent() as ViewGroup).removeView(parentViewGroup) // <- fix
          }*/
        parentViewGroup?.addView(childView)
        return parentView
    }

/*
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.e("visible","visibel")
        if (isVisibleToUser) {
            _visible = true;
        } else {
            _visible = false
            // fragment is no longer visible
        }
    }*/


    private fun setOnclick() {
        ErrorBtn?.setOnClickListener {
            onRetrybtn()
            showProgess()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgess()
        setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setup()
    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun onRetrybtn()

    fun showContent() {
        parentViewGroup?.show()
        progessLayout?.hide()
        ErrorLayout?.hide()

    }

    fun showProgess() {
        parentViewGroup?.hide()
        progessLayout?.show()
        ErrorLayout?.hide()
    }

    fun dataNotFound() {
        showError(IDConst.DATA_NOT_FOUND)
    }

    fun noInternet() {
        showError(IDConst.NO_INTERNET)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showError(errorCode: Int?) {
        parentViewGroup?.hide()
        progessLayout?.hide()
        ErrorLayout?.show()

        val errorResult = ResUtil.getErrorObj(errorCode, requireContext())
        errorResult.let {
            ErrorTextTitle?.text = it.title
            ErrorTextDesc?.text = it.desc
            ErrorImage?.setImageDrawable(
                getResources().getDrawable(
                    it.img, getActivity()?.getApplicationContext()
                        ?.getTheme()
                )
            )
        }
    }
}
