package br.com.henriqueoliveira.desafioandroidconcrete

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.henriqueoliveira.desafioandroidconcrete.di.androidModule
import br.com.henriqueoliveira.desafioandroidconcrete.di.remoteTestModule
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.Repository
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.ServerResponse
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.User
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.GitHubService
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.RepositoryDataSource
import br.com.henriqueoliveira.desafioandroidconcrete.view.ui.RepoListActivity
import br.com.henriqueoliveira.desafioandroidconcrete.viewmodel.RepositoryListViewModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls

class RepositoryListTest : KoinTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val gitHubService: GitHubService = mock(GitHubService::class.java)
    private val repository: RepositoryDataSource = mock(RepositoryDataSource::class.java)
    private val viewmodel : RepositoryListViewModel = mock(RepositoryListViewModel::class.java)
    private val activity: RepoListActivity = mock(RepoListActivity::class.java)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        StandAloneContext.startKoin(listOf(androidModule, remoteTestModule))
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun searchGitHubRepos() {

        viewmodel.loadRepositories()
        verify(viewmodel, Mockito.times(1)).loadRepositories()
    }

    @Test
    fun fetchRepositories() {

        whenever(gitHubService.getRepositoryList(any(), any(), any()))
                .thenReturn(Calls.response(getMockResponse()))

        repository.getRepositories(com.nhaarman.mockito_kotlin.any(), com.nhaarman.mockito_kotlin.any())

    }

    private fun getMockResponse(): ServerResponse {
        val owner = User("user_login", "avatar_url")
        val repository = Repository("any_name", "any_full_name", "any_description", owner, 2487, 5487)
        val items = ArrayList<Repository>()
        items.add(repository)

        return ServerResponse(1223, items)
    }
}