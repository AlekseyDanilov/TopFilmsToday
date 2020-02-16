package ru.alekseydanilov.topfilmstoday.workroom

import androidx.room.*
import ru.alekseydanilov.topfilmstoday.model.FilmModel

@Dao
interface FilmDao {

    @Query("SELECT * FROM film_models")
    fun getAll(): MutableList<FilmModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(filmModelList: List<FilmModel>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filmModel: FilmModel)

    @Query("SELECT * FROM film_models WHERE id LIKE :id")
    fun findById(id: String): FilmModel

    @Query("DELETE FROM film_models")
    fun deleteAllTable()

    @Delete
    fun delete(filmModel: FilmModel)

    @Update
    fun update(vararg filmModel: FilmModel)
}