package ru.alekseydanilov.topfilmstoday.popup

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager.BadTokenException
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import ru.alekseydanilov.topfilmstoday.R

class CustomPopUp : PopupWindow() {

    /**
     * Метод для отображения окна popUp с ошибкой
     *
     * @param activity - activity, в которой мы работаем
     * @param parent   - view представление, внутри которого мы отображаем popUP
     */
    fun showPopUp(activity: Activity, parent: View, message: String) {
        val layoutInflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val customView: View = layoutInflater.inflate(R.layout.popup_layout, null)

        val image = customView.findViewById<ImageView>(R.id.imagePopUp)
        val closeBtn = customView.findViewById<ImageView>(R.id.close_popup_btn)
        val headerText = customView.findViewById<TextView>(R.id.header_popup)

        /*
         * Определям размер popup
         */
        val popupWindow = PopupWindow(
            customView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isFocusable = true
        popupWindow.elevation = 15f
        popupWindow.update()

        // Отображаем окно popup
        try {
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0)
        } catch (ignored: BadTokenException) {
        }

        image.setImageResource(R.drawable.error)
        headerText.text = "Ошибка!\n $message"

        // Кнопка закрытия popUp
        closeBtn.setOnClickListener { v: View? -> popupWindow.dismiss() }
    }
}