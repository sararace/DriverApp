package com.example.driverapp.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import com.example.driverapp.R
import com.example.driverapp.databinding.ConfirmationAlertButtonBinding

class ConfirmationAlert(
    context: Context,
    themeResId: Int = 0
) : Dialog(context, themeResId) {

    init {
        setContentView(R.layout.confirmation_alert)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    class Builder(
        private val context: Context,
        private val themeResId: Int = 0
    ) {
        private var title: String = ""
        private var subtitle: String = ""
        private val buttons = mutableListOf<AlertButton>()

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setTitle(@StringRes title: Int): Builder {
            this.title = context.getString(title)
            return this
        }

        fun setSubtitle(subtitle: String): Builder {
            this.subtitle = subtitle
            return this
        }

        fun setSubtitle(@StringRes subtitle: Int): Builder {
            this.subtitle = context.getString(subtitle)
            return this
        }


        fun addButton(item: AlertButton): Builder {
            buttons.add(item)
            return this
        }

        fun setButtons(items: List<AlertButton>): Builder {
            buttons.addAll(items)
            return this
        }

        fun create(): ConfirmationAlert {
            val alert = ConfirmationAlert(context, themeResId)
            alert.findViewById<TextView>(R.id.title).text = title
            alert.findViewById<TextView>(R.id.subtitle).text = subtitle
            for (button in buttons) {
                val buttonView = ConfirmationAlertButtonBinding.inflate(
                    LayoutInflater.from(context),
                    alert.findViewById<LinearLayout>(R.id.buttons_container),
                    true
                ).root
                buttonView.text = button.textValue
                button.buttonType.background?.let {
                    buttonView.background = AppCompatResources.getDrawable(context, it)
                }
                buttonView.setTextColor(context.getColor(button.buttonType.textColor))
                buttonView.setOnClickListener {
                    button.onClick()
                    alert.dismiss()
                }
            }
            return alert
        }
    }

    data class AlertButton(
        val textValue: String,
        val buttonType: ButtonType,
        val onClick: () -> Unit = {}
    )

    enum class ButtonType(
        @DrawableRes
        val background: Int?,
        @ColorRes
        val textColor: Int
    ) {
        PRIMARY(R.drawable.navy_pill, R.color.white),
        SECONDARY(null, R.color.navy)
    }
}