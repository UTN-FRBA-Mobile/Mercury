package ar.edu.utn.frba.mobile.a2019c1.mercury

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun canCreateScheduleWithNameAndNoClients(){
        onView(withId(R.id.schedule_name)).perform(replaceText("ScheduleName"))

        onView(withId(R.id.fab)).perform(click())
        onView(withText("ScheduleName")).check(matches(isDisplayed()))
    }
}