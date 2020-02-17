package com.android.apps.components.view.captcha

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import com.android.apps.R
import com.android.apps.extensions.SimpleAction
import com.android.apps.extensions.toast
import kotlinx.android.synthetic.main.layout_captcha_view.view.*
import kotlin.random.Random

class CaptchaView: FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var onSuccess: SimpleAction? = null

    private val firstNumber by lazy {
        Random.nextInt(1, 10)
    }

    private val secondNumber by lazy {
        Random.nextInt(1, 10)
    }

    init {
        initialize()
    }

    private fun initialize() {
        View.inflate(context, R.layout.layout_captcha_view, this)
        text_math.text = "$firstNumber + $secondNumber = ?"

        input_result.setOnEditorActionListener { _, keyCode, _ ->
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                button_submit.performClick()
                true
            } else {
                false
            }
        }

        button_submit.setOnClickListener {
            try {
                val result = input_result.text.toString().toInt()
                if (result == firstNumber + secondNumber) {
                    onSuccess?.invoke()
                    return@setOnClickListener
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            context.toast(context.getString(R.string.text_wrong_result))
        }
    }


    fun registerOnSuccessListener(action: SimpleAction) {
        this.onSuccess = action
    }


}