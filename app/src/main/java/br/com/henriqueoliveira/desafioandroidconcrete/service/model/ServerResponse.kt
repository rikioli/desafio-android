package br.com.henriqueoliveira.desafioandroidconcrete.service.model

import com.google.gson.annotations.SerializedName

class ServerResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("items") val items: List<Repository>
)