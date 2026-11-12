
package com.maxout.chess

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*
import android.view.ViewGroup

class MainActivity : Activity() {

    private lateinit var board: GridLayout
    private lateinit var status: TextView
    private lateinit var squares: Array<Array<TextView>>

    private val pieces = arrayOf(
        arrayOf("♜","♞","♝","♛","♚","♝","♞","♜"),
        arrayOf("♟","♟","♟","♟","♟","♟","♟","♟"),
        arrayOf("","","","","","","",""),
        arrayOf("","","","","","","",""),
        arrayOf("","","","","","","",""),
        arrayOf("","","","","","","",""),
        arrayOf("♙","♙","♙","♙","♙","♙","♙","♙"),
        arrayOf("♖","♘","♗","♕","♔","♗","♘","♖")
    )

    private var whiteTurn = true
    private var selectedRow = -1
    private var selectedCol = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setPadding(12, 20, 12, 12)
        root.setBackgroundColor(Color.rgb(15, 20, 32))

        val title = TextView(this)
        title.text = "♟ MAXOUT CHESS"
        title.textSize = 27f
        title.setTextColor(Color.rgb(255, 190, 55))
        title.gravity = Gravity.CENTER
        root.addView(title)

        status = TextView(this)
        status.text = "WHITE'S TURN"
        status.textSize = 18f
        status.setTextColor(Color.WHITE)
        status.gravity = Gravity.CENTER
        status.setPadding(5, 18, 5, 18)
        root.addView(status)

        board = GridLayout(this)
        board.rowCount = 8
        board.columnCount = 8

        val boardParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        )
        root.addView(board, boardParams)

        squares = Array(8) { r ->
            Array(8) { c ->
                val cell = TextView(this)
                cell.gravity = Gravity.CENTER
                cell.textSize = 29f
                cell.setTextColor(Color.BLACK)

                val params = GridLayout.LayoutParams(
                    GridLayout.spec(r, 1f),
                    GridLayout.spec(c, 1f)
                )
                params.width = 0
                params.height = 0
                params.setMargins(1, 1, 1, 1)

                board.addView(cell, params)

                cell.setOnClickListener {
                    onSquareClick(r, c)
                }
                cell
            }
        }

        val reset = Button(this)
        reset.text = "NEW GAME"
        reset.setOnClickListener {
            resetGame()
        }
        root.addView(reset)

        setContentView(root)
        drawBoard()
    }

    private fun drawBoard() {
        for (r in 0..7) {
            for (c in 0..7) {
                val cell = squares[r][c]

                cell.text = pieces[r][c]

                val light = Color.rgb(238, 238, 210)
                val dark = Color.rgb(118, 150, 86)

                cell.setBackgroundColor(
                    if ((r + c) % 2 == 0) light else dark
                )

                if (r == selectedRow && c == selectedCol) {
                    cell.setBackgroundColor(Color.YELLOW)
                }
            }
        }

        status.text = if (whiteTurn)
            "WHITE'S TURN"
        else
            "BLACK'S TURN"
    }

    private fun onSquareClick(r: Int, c: Int) {
        val piece = pieces[r][c]

        if (selectedRow == -1) {
            if (piece.isEmpty()) return

            val isWhite = piece in "♙♖♘♗♕♔"

            if (isWhite != whiteTurn) return

            selectedRow = r
            selectedCol = c
        } else {
            if (r == selectedRow && c == selectedCol) {
                selectedRow = -1
                selectedCol = -1
            } else if (isValidMove(
                    selectedRow, selectedCol, r, c
                )) {
                val moving = pieces[selectedRow][selectedCol]

                pieces[r][c] = moving
                pieces[selectedRow][selectedCol] = ""

                if (moving == "♙" && r == 0)
                    pieces[r][c] = "♕"

                if (moving == "♟" && r == 7)
                    pieces[r][c] = "♛"

                whiteTurn = !whiteTurn
                selectedRow = -1
                selectedCol = -1
            } else {
                if (piece.isNotEmpty()) {
                    val isWhite = piece in "♙♖♘♗♕♔"
                    if (isWhite == whiteTurn) {
                        selectedRow = r
                        selectedCol = c
                    }
                }
            }
        }

        drawBoard()
    }

    private fun isValidMove(
        sr: Int, sc: Int, er: Int, ec: Int
    ): Boolean {

        if (sr == er && sc == ec) return false

        val p = pieces[sr][sc]
        val target = pieces[er][ec]

        val white = p in "♙♖♘♗♕♔"

        if (target.isNotEmpty()) {
            if ((target in "♙♖♘♗♕♔") == white)
                return false
        }

        val dr = er - sr
        val dc = ec - sc

        val ar = kotlin.math.abs(dr)
        val ac = kotlin.math.abs(dc)

        fun clearPath(): Boolean {
            val stepR = dr.compareTo(0)
            val stepC = dc.compareTo(0)

            var r = sr + stepR
            var c = sc + stepC

            while (r != er || c != ec) {
                if (pieces[r][c].isNotEmpty())
                    return false
                r += stepR
                c += stepC
            }
            return true
        }

        return when (p) {
            "♙" -> {
                if (dc == 0 && target.isEmpty()) {
                    (dr == -1 && ar == 1) ||
                    (sr == 6 && dr == -2 &&
                     pieces[5][sc].isEmpty())
                } else {
                    ar == 1 && ac == 1 && dr == -1 &&
                    target.isNotEmpty()
                }
            }

            "♟" -> {
                if (dc == 0 && target.isEmpty()) {
                    (dr == 1 && ar == 1) ||
                    (sr == 1 && dr == 2 &&
                     pieces[2][sc].isEmpty())
                } else {
                    ar == 1 && ac == 1 && dr == 1 &&
                    target.isNotEmpty()
                }
            }

            "♖", "♜" ->
                (dr == 0 || dc == 0) && clearPath()

            "♗", "♝" ->
                ar == ac && clearPath()

            "♕", "♛" ->
                (dr == 0 || dc == 0 || ar == ac) &&
                clearPath()

            "♘", "♞" ->
                (ar == 2 && ac == 1) ||
                (ar == 1 && ac == 2)

            "♔", "♚" ->
                ar <= 1 && ac <= 1

            else -> false
        }
    }

    private fun resetGame() {
        val initial = arrayOf(
            arrayOf("♜","♞","♝","♛","♚","♝","♞","♜"),
            arrayOf("♟","♟","♟","♟","♟","♟","♟","♟"),
            arrayOf("","","","","","","",""),
            arrayOf("","","","","","","",""),
            arrayOf("","","","","","","",""),
            arrayOf("","","","","","","",""),
            arrayOf("♙","♙","♙","♙","♙","♙","♙","♙"),
            arrayOf("♖","♘","♗","♕","♔","♗","♘","♖")
        )

        for (r in 0..7)
            for (c in 0..7)
                pieces[r][c] = initial[r][c]

        whiteTurn = true
        selectedRow = -1
        selectedCol = -1
        drawBoard()
    }
}
