package com.sarftec.lifelessons.application.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.sarftec.lifelessons.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val binding by lazy {
     ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun canShowInterstitial(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val item = SplashManager(this).getItem()
        binding.splashMainText.apply {
            setTextColor(item.textColor)
            text = item.title
            item.typeface?.let {
                typeface = it
            }
        }
        binding.splashBottomText.apply {
            setTextColor(item.textColor)
            text = item.subtitle
            item.typeface?.let {
                typeface = it
            }
        }
        binding.splashImageLayout.setBackgroundColor(item.backgroundColor)
        statusColor(item.backgroundColor)
        lifecycleScope.launchWhenCreated {
            delay(3500L)
            navigateTo(MainActivity::class.java, true)

        }
    }

    override fun onBackPressed() {
        finish()
    }

    private class SplashManager(private val context: Context) {

        val fonts = arrayOf("bebas.otf", "comfortaa.ttf", "dephion.otf", "limerock.ttf")

        val message = arrayOf(
            "You can never plan the future by the past" to "Wisdom Lessons",
            "Speak when you are angry -- and you will make the best speech you'll ever regret" to "Anger Lessons",
            "The greatest difficulty always comes right before the birth of a dream" to "Difficulty Lessons",
            "Divorce is the price people pay for playing with matches" to "Divorce Lessons",
            "Education is not the filling of a pail, but the lighting of a fire" to "Education Lessons",
            "Failure is the just the opportunity to begin again, this time more intelligently" to "Failure Lessons",
            "I try not to borrow, first you borrow then you beg" to "Finance Lessons",
            "There is no revenge so complete as forgiveness" to "Forgiveness Lessons",
            "You will fail at 100% of the goals you don't set" to "Goal Setting Lessons",
            "Behind every successful man lies a pack of haters!" to "Haters Lessons",
            "The rich invest in time, the poor invest in money" to "Investment Lessons"
        )

        class Item(
            val typeface: Typeface?,
            val title: String,
            val subtitle: String,
            val textColor: Int,
            val backgroundColor: Int
        )

        fun getItem(): Item {
            val split = f23727b.random().split("@")
            val typeface = Typeface.createFromAsset(context.assets, "fonts/" + fonts.random())
            val pair = message.random()
            return Item(
                typeface = typeface,
                title = "“${pair.first}”",
                subtitle = "-${pair.second}-",
                textColor = m19660d(split[1]),
                backgroundColor = m19660d(split[0])
            )
        }

        /**********************************************/
        /* renamed from: a */

        /*
         var f23726a = intArrayOf(
             R.color.color11,
             R.color.color12,
             R.color.color13,
             R.color.color14,
             R.color.color15,
             R.color.color16,
             R.color.color17,
             R.color.color18,
             R.color.color27
         )
         */


        /* renamed from: b */
        var f23727b = arrayOf(
            "#fdcd00@#26231c",
            "#1c1b21@#ffffff",
            "#3D155F@#DF678C",
            "#4831D4@#CCF381",
            "#317773@#E2D1F9",
            "#121c37@#ffa937",
            "#79bbca@#39324b",
            "#ffadb1@#202f34",
            "#373a3c@#e3b94d",
            "#e38285@#fbfdea",
            "#eebb2c@#6c2c4e",
            "#170e35@#94daef"
        )

        /* renamed from: c */
        fun m19659c(str: String): String {
            return if (str.contains("#")) str else "#$str"
        }

        /* renamed from: d */
        fun m19660d(str: String): Int {
            return Color.parseColor(m19659c(str))
        }
    }
}