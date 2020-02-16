package ru.alekseydanilov.topfilmstoday.model

/**
 * Модель данных для результата всех вильмов из ТОПа
 */
data class ResultsModel(
    var page: Int,
    var total_pages: String,
    var results: List<FilmModel>,
    var total_results: String
)
