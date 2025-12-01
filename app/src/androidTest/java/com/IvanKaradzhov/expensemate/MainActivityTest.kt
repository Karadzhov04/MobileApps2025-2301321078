package com.IvanKaradzhov.expensemate

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.IvanKaradzhov.expensemate.ui.main.MainActivity
import com.IvanKaradzhov.expensemate.ui.add.AddExpenseActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setup() {
        Intents.init()
        ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun fabOpensAddExpenseActivity() {
        onView(withId(R.id.fabAdd)).perform(click())
        Intents.intended(hasComponent(AddExpenseActivity::class.java.name))
    }
}
