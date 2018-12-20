package br.com.henriqueoliveira.desafioandroidconcrete.view.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.henriqueoliveira.desafioandroidconcrete.R
import br.com.henriqueoliveira.desafioandroidconcrete.helpers.showSnack
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.Repository
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.Resource
import br.com.henriqueoliveira.desafioandroidconcrete.view.EndlessRecyclerViewScrollListener
import br.com.henriqueoliveira.desafioandroidconcrete.view.adapter.RepositoriesAdapter
import br.com.henriqueoliveira.desafioandroidconcrete.viewmodel.RepositoryListViewModel
import kotlinx.android.synthetic.main.activity_repo_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepoListActivity : BaseActivity() {

    val repoViewModel by viewModel<RepositoryListViewModel>()
    private lateinit var adapter: RepositoriesAdapter
    lateinit var layoutManager: LinearLayoutManager

    companion object {
        var idlingResourceLast = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        setupToolbar(toolbar, R.string.app_name, -1, false)

        repoViewModel.loadRepositories()
        configRecycler()
        handleViewModel()
        setListeners()
    }

    private fun configRecycler() {
        adapter = RepositoriesAdapter(arrayListOf()) { repository -> onRepositoryItemClick(repository) }
        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerRepositories.layoutManager = layoutManager
        recyclerRepositories.adapter = adapter
    }

    private fun setListeners() {

        recyclerRepositories.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                repoViewModel.loadNextPage()
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            repoViewModel.refreshList()
        }
    }

    private fun handleViewModel() {

        repoViewModel.isLoading.observe(this, Observer { isLoading ->
            isLoading?.let { swipeRefreshLayout.isRefreshing = it }
        })

        repoViewModel.state.observe(this, Observer { stateRepo ->
            when (stateRepo.status) {

                Resource.RequestStatus.SUCCESS -> {
                    stateRepo.data?.let {
                        updateAdapter(it)
                        idlingResourceLast = 0
                    }
                }
                Resource.RequestStatus.ERROR -> {
                    stateRepo.data?.let { adapter.updateList(it) }
                    stateRepo.message?.let { showSnack(it) }
                    idlingResourceLast = 0
                }
                Resource.RequestStatus.LOADING -> swipeRefreshLayout.isRefreshing = true
            }
        })

        recyclerRepositories.addOnScrollListener(object : EndlessRecyclerViewScrollListener(LinearLayoutManager(this)) {
            override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                repoViewModel.loadNextPage()
            }
        })
    }

    fun updateAdapter(items: List<Repository>) {
        adapter.updateList(items)
    }

    private fun onRepositoryItemClick(repository: Repository) =
            startActivity(PullRequestActivity.newIntent(repository = repository, context = this))
}
