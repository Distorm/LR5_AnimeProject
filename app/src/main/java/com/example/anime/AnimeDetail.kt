package com.example.anime

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.anime.data.Anime
import com.example.anime.data.AnimeRepository
import com.example.anime.network.IapiResponse
import com.example.anime.network.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnimeDetail : AppCompatActivity() {
    private lateinit var apiService: IapiResponse
    private lateinit var animeRepository: AnimeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        apiService = RetrofitInstance.retrofitService
        animeRepository = AnimeRepository(application)

        val malId = intent.getIntExtra("mal_Id", -1)
        Log.d("AnimeDetail", "Получен malId: $malId")
        if (malId != -1) {
            // Получить детальную информацию об аниме, используя malId
            lifecycleScope.launch {
                val anime = fetchAnimeDetail(malId)
                anime?.let {
                    Log.d("AnimeDetail", "Получено аниме: $it")
                    updateUI(it)
                } ?: kotlin.run {
                    Log.e("AnimeDetail", "Детали аниме недоступны")
                    // Обработка ошибки: детали аниме недоступны
                }
            }
        } else {
            Log.e("AnimeDetail", "malId не передан")
            // Обработка ошибки: malId не передан
        }



        val addFavIcon = findViewById<ImageView>(R.id.addFav)
        addFavIcon.setOnClickListener {
            addAnimeToFavorites()
        }
    }

    private suspend fun fetchAnimeDetail(malId: Int): Anime? {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAnimeDetail(malId)
                response.data
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    private fun updateUI(anime: Anime) {
        findViewById<TextView>(R.id.animeTitle).text = anime.title
        findViewById<TextView>(R.id.animeDetailEp).text = anime.episodes.toString()
        findViewById<TextView>(R.id.animeDetailYear).text = anime.year?.toString() ?: "N/A"
        findViewById<TextView>(R.id.animeDetailType).text = anime.type
        findViewById<TextView>(R.id.animeDetailSynopsis).text = anime.synopsis
        val imageView = findViewById<ImageView>(R.id.animePic)
        Picasso.get().load(anime.imageURL).into(imageView)
    }

    private fun addAnimeToFavorites() {
        val malId = intent.getIntExtra("mal_Id", -1)
        if (malId != -1) {
            lifecycleScope.launch {
                try {
                    // Получить детали аниме из API
                    val anime = fetchAnimeDetail(malId)
                    anime?.let {
                        // Вставить аниме в избранное
                        animeRepository.insertAnime(it)
                        Toast.makeText(
                            applicationContext,
                            "Добавлено в избранное",
                            Toast.LENGTH_SHORT
                        ).show()
                    } ?: kotlin.run {
                        Log.e("AnimeDetail", "Детали аниме не найдены")
                        // Обработка ошибки: детали аниме не найдены
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Обработка ошибки: не удалось получить детали аниме из API
                }
            }
        } else {
            Log.e("AnimeDetail", "malId не передан")
            // Обработка ошибки: malId не передан
        }
    }


}
