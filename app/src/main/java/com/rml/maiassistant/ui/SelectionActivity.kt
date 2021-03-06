package com.rml.maiassistant.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rml.maiassistant.R

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager.beginTransaction()
                .add(R.id.gs_fragment_container, DepartmentSelectionFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            super.onBackPressed()
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }
}