package br.com.henriqueoliveira.desafioandroidconcrete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.PullRequest
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.Repository
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.ServerResponse
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.RepositoryDataSource
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.Resource
import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.ResultListener

class RepositoryListViewModel(val repository: RepositoryDataSource) : ViewModel() {

    private var page = 0
    private var repositoriesLiveData: MutableList<Repository> = arrayListOf()
    private var pullRequestLiveData: MutableList<PullRequest> = arrayListOf()
    private val mutableStatePR: MutableLiveData<Resource<List<PullRequest>>> = MutableLiveData()
    private val mutableStateRepo: MutableLiveData<Resource<List<Repository>>> = MutableLiveData()

    var lastPageReached = false
    val statePR = mutableStatePR as LiveData<Resource<List<PullRequest>>>
    val state = mutableStateRepo as LiveData<Resource<List<Repository>>>
    var isLoading = MutableLiveData<Boolean>()

    init {
        isLoading.value = false
    }

    fun loadPullRequests(owner: String, repositoryName: String) {
        isLoading.postValue(true)
        repository.getPullRequests(owner, repositoryName, object : ResultListener<List<PullRequest>> {
            override fun onSuccess(data: List<PullRequest>?) {
                isLoading.postValue(false)
                data?.let {
                    if (it.isEmpty()) {
                        mutableStatePR.postValue(Resource.error("No Pull Requests to show", pullRequestLiveData))
                        return
                    }

                    pullRequestLiveData.addAll(it)
                    mutableStatePR.postValue(Resource.success(pullRequestLiveData))
                }
            }

            override fun onFailure(message: String) {
                isLoading.postValue(false)
                mutableStatePR.postValue(Resource.error(message))
            }
        })
    }

    fun loadRepositories() {
        isLoading.postValue(true)
        repository.getRepositories(page, object : ResultListener<ServerResponse> {
            override fun onSuccess(data: ServerResponse?) {
                isLoading.postValue(false)
                data?.items?.let {
                    if (it.isEmpty()) {
                        lastPageReached = true
                        mutableStateRepo.postValue(Resource.error("No more repositories to load", repositoriesLiveData))
                        return
                    }

                    repositoriesLiveData.addAll(it)
                    mutableStateRepo.postValue(Resource.success(repositoriesLiveData))
                }
            }

            override fun onFailure(message: String) {
                isLoading.postValue(false)
                mutableStateRepo.postValue(Resource.error(message))
            }
        })
    }

    fun refreshList() {
        page = 1
        loadRepositories()
    }

    fun loadNextPage() {
        if (!isLoading.value!! && !lastPageReached) {
            page++
            isLoading.postValue(true)
            loadRepositories()
        }
    }
}
