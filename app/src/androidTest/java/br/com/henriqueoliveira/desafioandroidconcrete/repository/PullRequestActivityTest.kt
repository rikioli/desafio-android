package br.com.henriqueoliveira.desafioandroidconcrete.repository

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.henriqueoliveira.desafioandroidconcrete.R
import br.com.henriqueoliveira.desafioandroidconcrete.di.androidModule
import br.com.henriqueoliveira.desafioandroidconcrete.di.remoteTestModule
import br.com.henriqueoliveira.desafioandroidconcrete.utils.MockServer
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.PullRequestActivity
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.PullRequestActivity.Companion.EXTRA_REPOSITORY_NAME
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.PullRequestActivity.Companion.EXTRA_REPOSITORY_OWNER
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.test.KoinTest
import java.net.HttpURLConnection


class PullRequestActivityTest : KoinTest {

    private val mockServer = MockServer()

    @get:Rule
    private val rule = IntentsTestRule(PullRequestActivity::class.java, true, false)


    @Before
    fun setUp() {

        StandAloneContext.stopKoin()
        StandAloneContext.startKoin(listOf(androidModule, remoteTestModule))

        mockServer.startTestCase()
        val intent = Intent()
        intent.putExtra(EXTRA_REPOSITORY_NAME, "Java")
        intent.putExtra(EXTRA_REPOSITORY_OWNER, "TheAlgorithms")

        rule.launchActivity(intent)

        mockServer.context = rule.activity
        mockServer.enqueue(HttpURLConnection.HTTP_OK, "pull_request_list.json")
    }

    @After
    fun tearDown() {
        rule.finishActivity()
        mockServer.endTestCase()
        StandAloneContext.stopKoin()
    }


    @Test
    fun whenPullRequestsLoad_shouldDisplayRepositoryName() {
        onView(CoreMatchers.allOf<View>(withId(R.id.titlePR), withText("Acyclic Visitor pattern #734")))
                .check(matches(isDisplayed()))
    }

    @Test
    fun whenPullRequestsLoad_shouldDisplayPRCount() {

        Thread.sleep(500)
        onView(CoreMatchers.allOf<View>(withId(R.id.status), withText("5 opened / 0 closed")))
                .check(matches(isDisplayed()))
    }
}