package com.hatenablog.gikoha.doaccalculator

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)


        calculateButton.setOnClickListener {
            // hide input method window
            var im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            // get numbers from TextField
            var height: Double = bodyHeightField.text.toString().toDouble()
            var weight: Double = bodyWeightField.text.toString().toDouble()
            var age: Double = ageField.text.toString().toDouble()
            var crea: Double = sCrField.text.toString().toDouble()
            var egfr: Double = 194 * age.pow(-0.287) * crea.pow(-1.094)             // new formula from Am J Kidney Disease in press
            var bsa: Double = 0.007184 * weight.pow(0.425) * height.pow(0.725)      // The DuBois formula
            if (femaleButton.isChecked) {
                egfr *= 0.739
            }
            var gfr: Double = egfr * bsa
            CcrField.text = "%4.1f".format(gfr)
            BSAField.text = "%4.1f".format(bsa)
            eGFRField.text = "%4.1f".format(egfr)

            // 腎障害ステージ
            var stage: String = ""
            when {
                egfr >= 90 -> stage = getString(R.string.normal)
                egfr >= 60 -> stage = getString(R.string.mild_decreased)      //軽度腎障害
                egfr >= 30 -> stage = getString(R.string.moderate_decreased)  //"中等度腎障害"
                egfr >= 15 -> stage = getString(R.string.severe_decreased)    // "高度腎障害"
                else -> stage = getString(R.string.esrd)                    //"末期腎不全"
            }
            renalStage.text = stage

            // プラザキサ
            when {
                gfr >= 50 && age < 70 -> stage = "75mg 4C / 2xMA"
                gfr >= 30 || age >= 70 -> stage = "110mg 2C / 2xMA"
                else -> stage = "非推奨"
            }
            prazaxaField.text = stage

            // イグザレルト
            when {
                gfr >= 50 -> stage = "15mg 1T / 1xM"
                gfr >= 30 -> stage = "10mg 1T / 1xM"
                gfr >= 15 -> stage = "10mg 1T / 1xM 慎重投与"
                else -> stage = "非推奨"
            }
            xareltoField.text = stage

            // エリキュース
            var check: Int = 0
            if (age >= 80) check++
            if (weight <= 60) check++
            if (crea >= 1.5) check++

            when {
                gfr < 15 -> stage = "非推奨"
                check >= 2 -> stage = "10mg 2T / 2xMA"
                else -> stage = "15mg 2T / 2xMA"
            }
            eliquisField.text = stage

            // リクシアナ
            when {
                weight >= 60 && gfr >= 50 -> stage = "60mg 1T / 1xM"
                gfr >= 30 -> stage = "30mg 1T / 1xM"
                gfr >= 15 -> stage = "30mg 1T / 1xM 慎重投与"
                else -> stage = "非推奨"
            }
            lixianaField.text = stage
        }
    }

}
