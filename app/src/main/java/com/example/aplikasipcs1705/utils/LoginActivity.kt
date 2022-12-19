package com.example.aplikasipcs1705.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplikasipcs1705.MainActivity
import com.example.aplikasipcs1705.R
import com.example.aplikasipcs1705.api.BaseRetrofit
import com.example.aplikasipcs1705.response.LoginResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    companion object{
        lateinit var  sessionManager :SessionManager
        private lateinit var context: Context
    }
    private val api by lazy { BaseRetrofit().endpoint }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        val loginStatus = sessionManager.getBoolean("LOGIN_STATUS")
        if (loginStatus) {
            val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(moveIntent)
            finish()
        }

        val btnLogin = findViewById(R.id.btnLogin) as Button
        val txtEmail = findViewById(R.id.txtEmail) as TextInputEditText
        val txtPassword = findViewById(R.id.txtPassword) as TextInputEditText

        btnLogin.setOnClickListener{

            api.login(txtEmail.text.toString(),txtPassword.text.toString()).enqueue(object :
                retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: retrofit2.Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    Log.e("LoginData", response.toString())
                    val correct = response.body()!!.success

                    if (correct){
                        val token = response.body()!!.data.token

                    sessionManager.saveString("TOKEN", "Bearer" + token)
                    sessionManager.saveBoolean("LOGIN_STATUS", true)
                    sessionManager.saveString(
                        "ADMIN_ID",
                        response.body()!!.data.admin.id.toString()
                    )

                    val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(moveIntent)
                    finish()
                } else{
                    Toast.makeText(applicationContext,"User dan password salah", Toast.LENGTH_LONG).show()
                }
            }
                    override fun onFailure(call: retrofit2.Call<LoginResponse>,t: Throwable) {
                Log.e("LoginError",t.toString())
            }
        })
    }
}
}