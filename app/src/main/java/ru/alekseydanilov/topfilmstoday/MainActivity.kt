package ru.alekseydanilov.topfilmstoday

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import ru.alekseydanilov.topfilmstoday.model.FilmModel
import ru.alekseydanilov.topfilmstoday.popup.CustomPopUp
import ru.alekseydanilov.topfilmstoday.retrofit.RetrofitFactory.movieController
import ru.alekseydanilov.topfilmstoday.workroom.AppDatabase
import ru.alekseydanilov.topfilmstoday.workroom.FilmDao
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), OnRefreshListener, CoroutineScope {

    var listFilms: MutableList<FilmModel> = ArrayList()
    var customPopUp: CustomPopUp = CustomPopUp()
    var apiKey: String? = null
    private var dataBase: AppDatabase? = null
    private var filmDao: FilmDao? = null
    var databaseSize: Int = 0
    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main.immediate + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "ТОП-10 фильмов"

        dataBase = AppDatabase.getAppDataBase(context = this)
        filmDao = dataBase?.filmDao()

        swipeContainer.setOnRefreshListener(this)

        apiKey = getApiKeyFromProperties()

        recyclerListFilms.layoutManager = LinearLayoutManager(this)
        recyclerListFilms.adapter = FilmRecyclerAdapter(listFilms, this)

        // Получаем список фильмов из БД
        GlobalScope.launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                databaseSize = dataBase!!.filmDao().getAll().size
            }
            if (databaseSize == 0) {
                initFilmList()
            } else {
                withContext(Dispatchers.IO) {
                    listFilms.addAll(dataBase!!.filmDao().getAll())
                }
                recyclerListFilms.adapter!!.notifyDataSetChanged()
            }
        }
    }

    /**
     * Метод для инициализации списка фильмов
     */
    private fun initFilmList() {
        progressBarHolder.visibility = View.VISIBLE
        if (apiKey != null) {
            GlobalScope.launch(coroutineContext) {
                val topMoviesRequest = movieController.getTopMoviesAsync(apiKey!!, "ru-RU")
                try {
                    val response = withContext(Dispatchers.IO) { topMoviesRequest.await() }
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        val popularMovies = movieResponse?.results
                        // Заполняем список 10 первыми фильмами (как требуется в задаче)
                        for (i in 0..9) {
                            listFilms.add(popularMovies!![i])
                        }
                        withContext(Dispatchers.IO) { dataBase?.filmDao()?.insertAll(listFilms) }
                        recyclerListFilms.adapter?.notifyDataSetChanged()
                        progressBarHolder.visibility = View.GONE
                        swipeContainer.isRefreshing = false

                    } else {
                        Log.e("MainActivity ", response.errorBody().toString())
                        customPopUp.showPopUp(
                            layoutInflater, mainLinear,
                            "Возникла ошибка при получении фильмов"
                        )
                        progressBarHolder.visibility = View.GONE
                        swipeContainer.isRefreshing = false
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message)
                    customPopUp.showPopUp(
                        layoutInflater, mainLinear,
                        "Возникла ошибка сети при получении фильмов. Проверьте доступ в интернет на Вашем устройстве"
                    )
                    progressBarHolder.visibility = View.GONE
                    swipeContainer.isRefreshing = false
                }
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.e("MainActivity", "Ошибка получения ключа")
            }
            customPopUp.showPopUp(
                layoutInflater, mainLinear,
                "Не удалось получить список фильмов. Инициализируйте ключ в assets/app.properties"
            )
            progressBarHolder.visibility = View.GONE
            swipeContainer.isRefreshing = false
        }
    }

    /**
     * Метод для обновления БД
     */
    private fun updateFilmInfo() {
        GlobalScope.launch(coroutineContext) {
            withContext(Dispatchers.IO) {
                dataBase!!.filmDao().deleteAllTable()
            }
            listFilms.clear()
            initFilmList()
        }
    }

    /**
     * Метод для получения приватного ключа
     */
    private fun getApiKeyFromProperties(): String? {
        return try {
            val properties = Properties()
            properties.load(assets.open("app.properties"))
            properties.getProperty("API_KEY")
        } catch (e: IOException) {
            if (BuildConfig.DEBUG) {
                Log.e("MainActivity", "Ошибка получения ключа", e)
            }
            null
        }
    }

    /**
     * Swipe to refresh - метод для обновления информации
     */
    override fun onRefresh() {
        updateFilmInfo()
    }

    /**
     * Переопределяем onDestroy, чтобы при повороте экрана у нас отменялись текущие задачи
     */
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
