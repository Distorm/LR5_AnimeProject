package com.example.anime

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anime.adapter.AnimeAdapter
import com.example.anime.adapter.FavAnimeAdapter
import com.example.anime.data.Anime
import com.example.anime.model.Anime

class MainActivity : AppCompatActivity() {

    private lateinit var animeAdapter: AnimeAdapter
    private lateinit var favAnimeAdapter: FavAnimeAdapter
    private lateinit var animeList: List<Anime>
    private lateinit var favAnimeList: MutableList<Anime>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация данных аниме
        animeList = listOf(
            // Добавьте сюда свои данные аниме
        )

        favAnimeList = mutableListOf()

        // Инициализация адаптеров
        animeAdapter = AnimeAdapter(animeList)
        favAnimeAdapter = FavAnimeAdapter(favAnimeList)

        // Настройка RecyclerView для списка аниме
        val recyclerView: RecyclerView = findViewById(R.id.tvAnime)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = animeAdapter

        // Обработчик нажатия на изображение избранных аниме
        findViewById<ImageView>(R.id.favAnime).setOnClickListener {
            startActivity(Intent(this, FavAnimeActivity::class.java))
        }
    }
}
