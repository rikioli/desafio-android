package br.com.henriqueoliveira.desafioandroidconcrete.service.repository

import br.com.henriqueoliveira.desafioandroidconcrete.R
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.PullRequest
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.ServerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryDataSource(var gitHubService: GitHubService) {

    fun getRepositories(page: Int, listener: ResultListener<ServerResponse>) {

        gitHubService.getRepositoryList("java", page, "starts")
                .enqueue(object : Callback<ServerResponse> {
                    override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                        if(response.isSuccessful)
                            listener.onSuccess(response.body())
                        else
                            listener.onFailure("An unexpected error has occurred")
                    }

                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        listener.onFailure(t.message ?: "unknown err")
                    }
                })
    }

    fun getPullRequests(owner: String, repositoryName: String, listener: ResultListener<List<PullRequest>>) {

        gitHubService.getPullRequests(owner, repositoryName)
                .enqueue(object : Callback<List<PullRequest>> {
                    override fun onResponse(call: Call<List<PullRequest>>, response: Response<List<PullRequest>>) {
                        if (response.isSuccessful)
                            listener.onSuccess(response.body())
                    }

                    override fun onFailure(call: Call<List<PullRequest>>, t: Throwable) {
                        listener.onFailure(t.message ?: "unknown err")
                    }
                })
    }
}