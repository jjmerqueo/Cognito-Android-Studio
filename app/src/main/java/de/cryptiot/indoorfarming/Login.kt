/*
 * Developed by Keivan Kiyanfar on 10/9/18 11:38 PM
 * Last modified 10/9/18 11:38 PM
 * Copyright (c) 2018. All rights reserved.
 */
package de.cryptiot.indoorfarming

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Login : AppCompatActivity() {
    // ############################################################# View Components
    lateinit var txtNotAccount: TextView
    //TextView txtForgetPass;     // For retrieving password
    lateinit var btnLogin: Button
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    // ############################################################# End View Components

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = "Login"
        initViewComponents()
    }

    private fun initViewComponents() {
        txtNotAccount = findViewById(R.id.txtNotAccount)
        //txtForgetPass= findViewById(R.id.txtForgetPass);
        btnLogin = findViewById(R.id.btnLogin)
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        txtNotAccount.setOnClickListener {
            startActivity(
                Intent(
                    this@Login,
                    Signup::class.java
                )
            )
        }
        btnLogin.setOnClickListener {
            val authentication = Cognition(applicationContext)
            authentication.userLogin(
                etUsername.text.toString().replace(" ", ""),
                etPassword.text.toString()
            )
        }
    }
}
