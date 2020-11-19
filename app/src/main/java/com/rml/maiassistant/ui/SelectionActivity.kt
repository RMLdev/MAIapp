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
                .replace(R.id.gs_fragment_container, DepartmentSelectionFragment())
                .addToBackStack(null).commit()
        }
    }
}