package com.example.game.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.game.R
import com.example.game.utils.ScreenUtils

class CommonFragmentDialog : DialogFragment() {

    private val DEFAULT_WIDTH_PERCENT = 0.75f //默认弹窗占屏幕宽度

    private lateinit var tv_content: TextView
    private lateinit var mBuilder: Builder
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val window = dialog!!.window
        window.requestFeature(Window.FEATURE_NO_TITLE)
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
        dialog?.setCanceledOnTouchOutside(false)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            (ScreenUtils.getScreenSize(context).x * DEFAULT_WIDTH_PERCENT).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)
        window.setWindowAnimations(R.style.TKCommonBottomDialogAnimation)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.dialog_common_notitle, null)
        initViewAndListener(view)
        return view
    }

    private fun initViewAndListener(view: View) {
        val tv: TextView = view.findViewById(R.id.tv_cancel)
        tv.setOnClickListener {
            dismissAllowingStateLoss()
            if (mCall != null) {
                mCall("0")
            }
        }

        val tv2: TextView = view.findViewById(R.id.tv_confirm)
        tv2.setOnClickListener {
            dismissAllowingStateLoss()
            if (mCall != null) {
                mCall("1")
            }
        }

        tv_content = view.findViewById(R.id.tv_content)
        tv_content.text = mBuilder.title
        mCall = mBuilder.bCall

    }

    fun setTitle(msg: String) {
        tv_content.text = msg
    }

    override fun show(
        manager: FragmentManager,
        tag: String?
    ) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            //同一实例使用不同的tag会异常,这里捕获一下
        }
    }

    private fun setBuilder(b: Builder) {
        mBuilder = b
    }

    lateinit var mCall: (res: String) -> Unit


    class Builder {

        var title = ""
        lateinit var bCall: (res: String) -> Unit
        fun setTitle(msg: String): Builder {
            title = msg
            return this

        }

        fun create(): CommonFragmentDialog {
            val dialog = CommonFragmentDialog()
            dialog.setBuilder(this)
            return dialog
        }

        fun setListener(call: (res: String) -> Unit): Builder {
            bCall = call
            return this
        }
    }
}