package br.com.henriqueoliveira.desafioandroidconcrete.service.model

import com.google.gson.annotations.SerializedName

data class PullRequest(
    @SerializedName("url") val url: String,
    @SerializedName("title") val title: String,
    @SerializedName("user") val user: User,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("body") val body: String,
    @SerializedName("state") val state: String
)