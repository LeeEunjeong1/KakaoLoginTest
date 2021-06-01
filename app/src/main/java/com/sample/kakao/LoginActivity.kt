package com.sample.kakao

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.sample.kakao.databinding.ActivityMainBinding



private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 공통 콜백 구성
        binding.login.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    Log.i(TAG, "loginWithKaKaoTalk $token $error")
                    updateLoginInfo()
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    Log.i(TAG, "loginWithKakaoAccount $token $error")
                    updateLoginInfo()
                }
            }
        }
        updateLoginInfo()
    }

    private fun updateLoginInfo() {
        UserApiClient.instance.me { user, error ->
            user?.let {
                Log.i(
                    TAG,
                    "updateLoginInfo: ${user.id} ${user.kakaoAccount?.email} ${user.kakaoAccount?.profile?.nickname} ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                binding.nickname.text = user.kakaoAccount?.profile?.nickname
                Glide.with(this).load(user.kakaoAccount?.profile?.thumbnailImageUrl).circleCrop()
                    .into(binding.profile)
                binding.login.visibility = View.GONE
                binding.logout.visibility = View.VISIBLE
            }
            error?.let {
                binding.nickname.text = null
                binding.profile.setImageBitmap((null))
                binding.login.visibility = View.VISIBLE
                binding.logout.visibility = View.GONE
            }
        }
    }
}
