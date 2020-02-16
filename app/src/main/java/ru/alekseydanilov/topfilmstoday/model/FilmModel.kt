package ru.alekseydanilov.topfilmstoday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Модель данных для отдельного фильма из ТОПа
 */
@Entity(tableName = "film_models")
data class FilmModel(
    var overview: String,
    var original_language: String,
    var original_title: String,
    var video: String,
    var title: String,
    var poster_path: String,
    var backdrop_path: String,
    var release_date: String,
    var popularity: String,
    var vote_average: String,
    @PrimaryKey var id: String,
    var adult: String,
    var vote_count: String
)