package com.aavas.ratingapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.aavas.ratingreview.AppRating
import com.aavas.ratingreview.ratingdialog.buttons.CustomFeedbackButtonClickListener
import com.aavas.ratingreview.ratingdialog.buttons.RateDialogClickListener
import com.aavas.ratingreview.ratingdialog.preferences.MailSettings
import com.aavas.ratingreview.ratingdialog.preferences.RatingThreshold

class MainActivity : AppCompatActivity(),CustomFeedbackButtonClickListener,
    RateDialogClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onResetButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.reset(this)
        Toast.makeText(this, R.string.toast_reset, Toast.LENGTH_SHORT).show()
    }

    fun onGoogleInAppReviewExampleButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .useGoogleInAppReview()
            .setGoogleInAppReviewCompleteListener { successful ->
                toastLiveData.postValue("Google in-app review completed (successful: $successful)")
            }
            .setDebug(true)
            .showIfMeetsConditions()
    }

    fun onDefaultExampleButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .showIfMeetsConditions()
    }

    fun onCustomIconButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val iconDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_star_black, null)

        AppRating.Builder(this)
            .setDebug(true)
            .setIconDrawable(iconDrawable)
            .showIfMeetsConditions()
    }

    fun onMailFeedbackButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setMailSettingsForFeedbackDialog(
                MailSettings(
                    "info@your-app.de",
                    "Feedback Mail",
                    "This is an example text.\n\nYou could set some device infos here.",
                ),
            )
            .showIfMeetsConditions()
    }

    fun onCustomFeedbackButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setUseCustomFeedback(true)
            .setCustomFeedbackButtonClickListener { userFeedbackText ->
                toastLiveData.postValue("Feedback: $userFeedbackText")
            }
            .showIfMeetsConditions()
    }

    fun onShowNeverButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .showRateNeverButton()
            .showIfMeetsConditions()
    }

    fun onShowNeverButtonAfterThreeTimesClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .showRateNeverButtonAfterNTimes(countOfLaterButtonClicks = 3)
            .showIfMeetsConditions()
    }

    fun onShowOnThirdClickButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .showRateNeverButton()
            .setMinimumLaunchTimes(3)
            .setMinimumDays(0)
            .setMinimumLaunchTimesToShowAgain(5)
            .setMinimumDaysToShowAgain(0)
            .showIfMeetsConditions()
    }

    fun onRatingThresholdButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setRatingThreshold(RatingThreshold.FOUR_AND_A_HALF)
            .showIfMeetsConditions()
    }

    fun onFullStarRatingButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setShowOnlyFullStars(true)
            .showIfMeetsConditions()
    }

    fun onCustomTextsButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setRateNowButtonTextId(R.string.button_rate_now)
            .setRateLaterButtonTextId(R.string.button_rate_later)
            .showRateNeverButton(R.string.button_rate_never)
            .setTitleTextId(R.string.title_overview)
            .setMessageTextId(R.string.message_overview)
            .setConfirmButtonTextId(R.string.button_confirm)
            .setStoreRatingTitleTextId(R.string.title_store)
            .setStoreRatingMessageTextId(R.string.message_store)
            .setFeedbackTitleTextId(R.string.title_feedback)
            .setMailFeedbackMessageTextId(R.string.message_feedback)
            .setMailFeedbackButtonTextId(R.string.button_mail_feedback)
            .setNoFeedbackButtonTextId(R.string.button_no_feedback)
            .showIfMeetsConditions()
    }

    fun onCancelableButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setCancelable(true)
            .setDialogCancelListener { toastLiveData.postValue("Dialog was canceled.") }
            .showIfMeetsConditions()
    }

    /*fun onCustomThemeButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        AppRating.Builder(this)
            .setDebug(true)
            .setCustomTheme(R.style.AppTheme_CustomAlertDialog)
            .showIfMeetsConditions()
    }*/

    /*fun onJetpackComposeButtonClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val jetpackComposeIntent = Intent(this, ComposeExampleActivity::class.java)
        startActivity(jetpackComposeIntent)
    }*/

    companion object {
        // The livedata is used so that no context is given into the click listeners. (NotSerializableException)
        private val toastLiveData: MutableLiveData<String> = MutableLiveData()
    }

    override fun onClick(userFeedbackText: String) {
        Toast.makeText(this, "Feedback: $userFeedbackText", Toast.LENGTH_SHORT).show()
    }

    override fun onClick() {

    }
}