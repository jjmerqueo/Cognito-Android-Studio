/*
 * Developed by Keivan Kiyanfar on 10/9/18 9:48 PM
 * Last modified 10/9/18 9:48 PM
 * Copyright (c) 2018. All rights reserved.
 */
package de.cryptiot.indoorfarming

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class Signup : AppCompatActivity() {
    // ############################################################# View Components
    lateinit var etUsername: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etPass: EditText
    lateinit var etRepeatPass: EditText
    lateinit var etConfCode: EditText
    lateinit var btnSignUp: Button
    lateinit var btnVerify: Button
    // ############################################################# End View Components
    // ############################################################# Cognito connection
    var authentication: Cognition? = null
    private var userId: String? = null

    // ############################################################# End Cognito connection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        authentication = Cognition(applicationContext)
        initViewComponents()
    }

    private fun initViewComponents() {
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etPass = findViewById(R.id.etPass)
        etRepeatPass = findViewById(R.id.etRepeatPass)
        etConfCode = findViewById(R.id.etConfCode)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnVerify = findViewById(R.id.btnVerify)
        btnSignUp.setOnClickListener {
            if (etPass.text.toString().endsWith(etRepeatPass.text.toString())) {
                userId = etUsername.text.toString().replace(" ", "")
                authentication!!.addAttribute("name", userId)
                authentication!!.addAttribute(
                    "phone_number",
                    etMobile.text.toString().replace(" ", "")
                )
                authentication!!.addAttribute(
                    "email",
                    etEmail.text.toString().replace(" ", "")
                )
                authentication!!.signUpInBackground(userId, etPass.text.toString())
            }
        }
        btnVerify.setOnClickListener {
            authentication!!.confirmUser(userId, etConfCode.text.toString().replace(" ", ""))
            //finish();
        }
    }
}
