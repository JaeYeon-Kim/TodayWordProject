package com.kjy.todaywordproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews() {
        // 뷰페이저 화면 전환 이벤트를 주는 기능 추가
        viewPager.setPageTransformer { page, position ->
            // 가운데를 중심으로 중앙에 위치 할때는 화면 전체가 보이게되고 좌우로 (중심에서 벗어날수록) 텍스트가 사라지는
            // 투명도 효과를 줌.
            when {
                // 절대 위치
                position.absoluteValue >= 1.0F -> {
                    page.alpha = 0F
                }
                // 중앙
                position == 0F -> {
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue
                }
            }
        }
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
            // 명언 앱을 불러오기 전에 프로그래스바를 통해 로딩하고 있다는 것을 띄워줌.
            progressBar.visibility = View.GONE
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
        val adapter = QuotePagerAdapter(quotes = quotes, isNameRevealed = isNameRevealed)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount / 2, false)
    }
}