package com.sarftec.lifelessons.application.splash

import android.content.Context
import android.graphics.Color
import javax.inject.Inject

class Splash @Inject constructor(context: Context) {

    private val quotes = listOf(
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

    private val fonts = listOf(
        Font(context, "anton_regular.ttf"),
        Font(context, "dancing_script.ttf", null),
        Font(context, "lobster_regular.ttf"),
        Font(context, "courgette_regular.ttf"),
        Font(context, "lato_bold.ttf", null),
        Font(context, "markscript_regular.ttf"),
        Font(context, "oleoscript_bold.ttf", null),
        Font(context, "patrickhand_regular.ttf"),
        Font(context, "righteous_regular.ttf"),
        Font(context, "ubuntu_bold.ttf"),
        Font(context, "bad_script.ttf")
    )
    private val background = listOf(
        (0xFF0E766C).toInt() to Color.WHITE,
        (0xFF322514).toInt() to Color.WHITE,
        (0xFFF0A07C).toInt() to (0xFF5B0567).toInt(),
        (0xFF1C1C1C).toInt() to Color.WHITE,
        (0xFFe5e5dc).toInt() to Color.BLACK,
        (0xFF292826).toInt() to (0xFFF9D342).toInt(),
        (0xFF161B21).toInt() to (0xFFF4A950).toInt(),
        (0xFF080A52).toInt() to (0xFFED2188).toInt()
    )

    fun fetchSplashItem(): SplashItem {
        return SplashItem(quotes.random(), background.random(), fonts.random())
    }
}