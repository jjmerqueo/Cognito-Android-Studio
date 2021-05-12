/*
 * Developed by Keivan Kiyanfar on 10/7/18 10:35 PM
 * Last modified 10/7/18 10:35 PM
 * Copyright (c) 2018. All rights reserved.
 */
package de.cryptiot.indoorfarming

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler
import com.amazonaws.regions.Regions

class Cognition(private val appContext: Context) {
    // ############################################################# Information about Cognito Pool
    private var poolID = "us-east-1_6RDDl7AqV"
    private var clientID = "7cc6989481aq5m67d71iujouno"
    private var clientSecret = "1gnapnbdv58buar93ohjcekqp3juebv9sub5qu3s11n6l3hqf72f"
    private var awsRegion = Regions.US_EAST_1 // Place your Region

    // ############################################################# End of Information about Cognito Pool
    private val userPool: CognitoUserPool
    private val userAttributes // Used for adding attributes to the user
        : CognitoUserAttributes
    private var userPassword // Used for Login
        : String? = null

    fun signUpInBackground(userId: String?, password: String?) {
        userPool.signUpInBackground(userId, password, userAttributes, null, signUpCallback)
        //userPool.signUp(userId, password, this.userAttributes, null, signUpCallback);
    }

    private var signUpCallback: SignUpHandler = object : SignUpHandler {
        override fun onSuccess(
            cognitoUser: CognitoUser,
            userConfirmed: Boolean,
            cognitoUserCodeDeliveryDetails: CognitoUserCodeDeliveryDetails
        ) {
            // Sign-up was successful
            Log.d(ContentValues.TAG, "Sign-up success")
            Toast.makeText(appContext, "Sign-up success", Toast.LENGTH_LONG).show()
            // Check if this user (cognitoUser) needs to be confirmed
            if (!userConfirmed) {
                // This user must be confirmed and a confirmation code was sent to the user
                // cognitoUserCodeDeliveryDetails will indicate where the confirmation code was sent
                // Get the confirmation code from user
            } else {
                Toast.makeText(appContext, "Error: User Confirmed before", Toast.LENGTH_LONG).show()
                // The user has already been confirmed
            }
        }

        override fun onFailure(exception: Exception) {
            Toast.makeText(appContext, "Sign-up failed", Toast.LENGTH_LONG).show()
            Log.d(ContentValues.TAG, "Sign-up failed: $exception")
        }
    }

    fun confirmUser(userId: String?, code: String?) {
        val cognitoUser = userPool.getUser(userId)
        cognitoUser.confirmSignUpInBackground(code, false, confirmationCallback)
        //cognitoUser.confirmSignUp(code,false, confirmationCallback);
    }

    // Callback handler for confirmSignUp API
    private var confirmationCallback: GenericHandler = object : GenericHandler {
        override fun onSuccess() {
            // User was successfully confirmed
            Toast.makeText(appContext, "User Confirmed", Toast.LENGTH_LONG).show()
        }

        override fun onFailure(exception: Exception) {
            // User confirmation failed. Check exception for the cause.
        }
    }

    fun addAttribute(key: String?, value: String?) {
        userAttributes.addAttribute(key, value)
    }

    fun userLogin(userId: String?, password: String?) {
        val cognitoUser = userPool.getUser(userId)
        userPassword = password
        cognitoUser.getSessionInBackground(authenticationHandler)
    }

    // Callback handler for the sign-in process
    private var authenticationHandler: AuthenticationHandler = object : AuthenticationHandler {
        override fun authenticationChallenge(continuation: ChallengeContinuation) {}
        override fun onSuccess(userSession: CognitoUserSession, newDevice: CognitoDevice) {
            Toast.makeText(appContext, "Sign in success", Toast.LENGTH_LONG).show()
        }

        override fun getAuthenticationDetails(
            authenticationContinuation: AuthenticationContinuation,
            userId: String
        ) {
            // The API needs user sign-in credentials to continue
            val authenticationDetails = AuthenticationDetails(userId, userPassword, null)
            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails)
            // Allow the sign-in to continue
            authenticationContinuation.continueTask()
        }

        override fun getMFACode(multiFactorAuthenticationContinuation: MultiFactorAuthenticationContinuation) {
            // Multi-factor authentication is required; get the verification code from user
            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // Allow the sign-in process to continue
            //multiFactorAuthenticationContinuation.continueTask();
        }

        override fun onFailure(exception: Exception) {
            // Sign-in failed, check exception for the cause
            Toast.makeText(appContext, "Sign in Failure", Toast.LENGTH_LONG).show()
        }
    }

    init {
        userPool = CognitoUserPool(appContext, poolID, clientID, clientSecret, awsRegion)
        userAttributes = CognitoUserAttributes()
    }
}
