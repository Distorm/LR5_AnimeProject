package com.example.anime

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anime.adapter.FavAnimeAdapter
import com.example.anime.data.AnimeRepository
import com.example.anime.databinding.ActivityFavAnimeBinding
import com.google.android.material.snackbar.Snackbar

class FavAnime : AppCompatActivity() {

    private lateinit var binding: ActivityFavAnimeBinding
    private lateinit var animeRepository: AnimeRepository
    private lateinit var favAnimeAdapter: FavAnimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavAnimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        animeRepository = AnimeRepository(application)

        favAnimeAdapter = FavAnimeAdapter(this, animeRepository)
        binding.tvFav.layoutManager = LinearLayoutManager(this)
        binding.tvFav.adapter = favAnimeAdapter

        loadAllAnime()
    }

    private fun loadAllAnime() {
        animeRepository.getAllAnime()?.observe(this) { allAnimeList ->
            allAnimeList?.let {
                favAnimeAdapter.setAnimeList(it)
            }
        }
    }

    private val swipeCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            favAnimeAdapter.deleteItem(position)
            Snackbar.make(binding.root, "Аниме удалено из избранного", Snackbar.LENGTH_LONG)
                .setAction("Отменить") {
                    // Реализуйте действие отмены, если необходимо
                }.show()
        }
    }

}
