package br.com.henriqueoliveira.desafioandroidconcrete.repository

import android.content.ComponentName
import android.view.View
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import br.com.henriqueoliveira.desafioandroidconcrete.R
import br.com.henriqueoliveira.desafioandroidconcrete.view.adapter.RepositoriesAdapter
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.PullRequestActivity
import org.hamcrest.CoreMatchers

fun repoListActivityRobot(func: RepoListActivityRobot.() -> Unit) = RepoListActivityRobot().apply(func)

class RepoListActivityRobot {


    fun repositoryClick(position: Int) {

        onView(ViewMatchers.withId(R.id.recyclerRepositories))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RepositoriesAdapter.ViewHolder>(position, ViewActions.click()))

    }
}

class RepoListActivityResult {

    fun redirectToNextActivity() {
        Intents.intended(IntentMatchers.hasComponent(ComponentName(InstrumentationRegistry.getTargetContext(), PullRequestActivity::class.java)))
    }
}