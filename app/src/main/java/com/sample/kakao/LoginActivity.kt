package com.sample.kakao

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //로그인 공통 콜백 구성
        val callback: ((OAuthToken?, Throwable?) -> Unit) = { token, error ->
            if (error != null) { //Login Fail
                Log.e(TAG, "Kakao Login Failed :", error)
            } else if (token != null) { //Login Success
                startMainActivity()
            }
        }
        findViewById<ImageView>(R.id.login).setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니라면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}