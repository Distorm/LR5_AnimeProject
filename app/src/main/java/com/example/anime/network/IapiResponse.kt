package com.example.anime.network

import com.example.anime.data.APIResponse
import com.example.anime.data.Anime
import retrofit2.http.GET
import retrofit2.http.Path

interface IapiResponse {

    @GET("anime")
    suspend fun getAnimeList(): APIResponse<List<Anime>>


    @GET("anime/{mal_id}")
    suspend fun getAnimeDetail(@Path("mal_id") malId: Int): APIResponse<Anime>
}