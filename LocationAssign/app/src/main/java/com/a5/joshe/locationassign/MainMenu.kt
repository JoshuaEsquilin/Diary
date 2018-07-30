package com.a5.joshe.locationassign

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  Main Menu allowing the user to either make a new entry or look at all entries
//

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun newEntry(view: View) {
        val i = Intent(applicationContext, NewEntry::class.java)
        startActivity(i)
    }

    fun allEntries(view: View) {
        val i = Intent(applicationContext, AllEntries::class.java)
        startActivity(i)
    }
}
