package com.kjy.todaywordproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    // 데이터를 가져옴
    private fun initData() {
        // remoteConfig 변수 설정 후 비동기로 가져옴
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            // 기본 변경 시간인 12시간을 단축
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        // 작업을 완료했을 경우
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            // it은 현재 task 반환
            if(it.isSuccessful) {
                // quotes 파라미터 key값을 가져와서 json 형식 파싱
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, isNameRevealed)
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for(index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }

        return jsonList.map {
            Quote(quote = it.getString("quote"),
                  name = it.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        viewPager.adapter = QuotePagerAdapter(quotes = quotes, isNameRevealed = isNameRevealed)
    }
}