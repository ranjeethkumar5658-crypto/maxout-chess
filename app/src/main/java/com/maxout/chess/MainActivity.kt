
package com.maxout.chess

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(15, 20, 30)
        window.navigationBarColor = Color.BLACK

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(30, 30, 30, 30)
        layout.setBackgroundColor(Color.rgb(15, 20, 30))

        val title = TextView(this)
        title.text = "♟ MAXOUT CHESS"
        title.textSize = 32f
        title.setTextColor(Color.rgb(255, 190, 60))
        title.gravity = Gravity.CENTER

        val subtitle = TextView(this)
        subtitle.text = "THINK • PLAY • IMPROVE"
        subtitle.textSize = 14f
        subtitle.setTextColor(Color.WHITE)
        subtitle.gravity = Gravity.CENTER

        layout.addView(title)
        layout.addView(subtitle)

        fun addMenu(text: String) {
            val button = Button(this)
            button.text = text
            button.textSize = 17f
button.setTextColor(Color.BLACK)
button.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(255, 190, 60))


            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                65
            )
            params.setMargins(0, 15, 0, 5)

            layout.addView(button, params)
        }

        addMenu("PLAY VS COMPUTER")
        addMenu("TWO PLAYER")
        addMenu("CHESS PUZZLES")
        addMenu("CHESS OPENINGS")
        addMenu("GAME ANALYSIS")

        setContentView(layout)
    }
}
