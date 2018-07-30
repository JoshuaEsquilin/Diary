package com.a5.joshe.locationassign

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pass_login.*

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  PassLogin checks to see if the user is registered, and if not then the 1st password
//               they type will be their password.  Then they must confirm it and that will be the
//               password they use to access the app.

class PassLogin : AppCompatActivity() {

    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var registered: Boolean = false

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_login)
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        editor = sharedPref!!.edit()
        editPassword.setText(sharedPref!!.getString("pass", ""))
    }

    fun login(view: View) {

        val pass: String = editPassword.text.toString()
        val i = Intent(applicationContext, MainMenu::class.java)
        val cPass: String = sharedPref!!.getString("pass", "")

        registered = sharedPref!!.getBoolean("Registered", false)

        if (!registered) {
            editor!!.putString("pass", pass)
            editor!!.putBoolean("Registered", true)
            editor!!.apply()
            editPassword.setText("")
            Toast.makeText(this@PassLogin, "The Password is now registered.  Please re-enter password to login", Toast.LENGTH_SHORT).show()
            registered = true
        } else {
            if (pass == cPass) {
                startActivity(i)
                finish()
            } else {
                Toast.makeText(this@PassLogin, "The Password entered is incorrect", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
