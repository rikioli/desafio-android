package br.com.henriqueoliveira.desafioandroidconcrete.di

import br.com.henriqueoliveira.desafioandroidconcrete.service.repository.GitHubService
import org.koin.dsl.module.module

val remoteTestModule = module(definition = {
    // provided web components
    single { createOkHttpClient() }
    // Fill property
    single { createWebService<GitHubService>(get(), "http://localhost:8080/") }
})
