package br.com.henriqueoliveira.desafioandroidconcrete.repository

import android.app.Activity
import android.app.Instrumentation
import android.content.ComponentName
import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import br.com.henriqueoliveira.desafioandroidconcrete.di.androidModule
import br.com.henriqueoliveira.desafioandroidconcrete.di.remoteTestModule
import br.com.henriqueoliveira.desafioandroidconcrete.utils.MockServer
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.PullRequestActivity
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.RepoListActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import java.net.HttpURLConnection


class RepoListActivityTest : KoinTest {

    private val mockServer = MockServer()

    @get:Rule
    private val rule = IntentsTestRule(RepoListActivity::class.java, true, false)

    @Before
    fun setUp() {

        StandAloneContext.stopKoin()
        StandAloneContext.startKoin(listOf(androidModule, remoteTestModule))

        mockServer.startTestCase()
        rule.launchActivity(Intent())
        mockServer.context = rule.activity
    }

    @After
    fun tearDown() {
        rule.finishActivity()
        mockServer.endTestCase()
        StandAloneContext.stopKoin()
    }

    @Test
    fun whenRepositoryIsClicked_shouldRedirectToNextActivity() {

        mockServer.enqueue(HttpURLConnection.HTTP_OK, "repository_list_first_page.json")

        Intents.intending(IntentMatchers.hasComponent(ComponentName(InstrumentationRegistry.getTargetContext(), PullRequestActivity::class.java)))
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))

        repoListActivityRobot {
            repositoryClick(0)
        }

        RepoListActivityResult().redirectToNextActivity()
    }

//    @Test
//    fun test_serverError() {
//        mockServer.enqueue(HttpURLConnection.HTTP_INTERNAL_ERROR)
//
//        Thread.sleep(500)
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText("An unexpected error has occurred")))
//    }
}