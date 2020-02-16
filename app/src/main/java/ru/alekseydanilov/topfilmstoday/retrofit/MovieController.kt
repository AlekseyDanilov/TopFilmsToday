package ru.alekseydanilov.topfilmstoday.retrofit

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.alekseydanilov.topfilmstoday.model.ResultsModel

/**
 * Контроллер для работы с api
 */
interface MovieController {
    /**
     * Метод для получения фильмов из ТОПа
     *
     * @param api_key  - уникальный ключ
     * @param language - локаль
     * @return - возвращает модель, содержащую информацию по фильмам
     */
    @GET("3/movie/popular")
    fun getTopMoviesAsync(@Query("api_key") api_key: String, @Query("language") language: String): Deferred<Response<ResultsModel>>
}