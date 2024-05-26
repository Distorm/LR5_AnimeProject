package com.example.anime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anime.adapter.AnimeAdapter
import com.example.anime.data.APIResponse
import com.example.anime.data.Anime
import com.example.anime.data.AnimeRepository
import com.example.anime.databinding.ActivityMainBinding
import com.example.anime.network.IapiResponse
import com.example.anime.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding : ActivityMainBinding
    private lateinit var animeAdapter: AnimeAdapter
    private var animeList = ArrayList<Anime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        connectToWebServices()

        this.binding.tvAnime.layoutManager = LinearLayoutManager(this)
        this.binding.tvAnime.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        this.animeAdapter = AnimeAdapter(this@MainActivity, AnimeRepository(this))
        this.binding.tvAnime.adapter = this.animeAdapter

        binding.favAnime.setOnClickListener {
            val intent = Intent(this@MainActivity, FavAnime::class.java)
            startActivity(intent)
        }
    }

    private fun connectToWebServices(){
        var animeAPI : IapiResponse = RetrofitInstance.retrofitService

        lifecycleScope.launch{
            val response : APIResponse<List<Anime>> = animeAPI.getAnimeList()

            val APIAnimeList : List<Anime> = response.data

            animeList.clear()
            animeList.addAll(APIAnimeList)
            animeAdapter.setAnimeList(animeList)

            Log.d(TAG, "Подключение к веб-сервису : Название аниме : ${animeList}")
        }
    }
}
