package ru.alekseydanilov.topfilmstoday

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import ru.alekseydanilov.topfilmstoday.model.FilmModel
import ru.alekseydanilov.topfilmstoday.workroom.AppDatabase
import ru.alekseydanilov.topfilmstoday.workroom.FilmDao
import java.io.IOException

@RunWith(JUnit4::class)
class FilmReadWriteTest {
    private lateinit var filmDao: FilmDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().getContext()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        filmDao = db.filmDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val filmModel = FilmModel(
            "This film is amazing!", "EN",
            "TestFilm", "pathVideo", "TestFilm", "pathPoster",
            "path", "17.02.2020", "1000", "1000",
            "1", "18+", "9.9"
        )
        filmDao.insert(filmModel)
        val todoItem = filmDao.findById(filmModel.id)
        assertThat(todoItem, equalTo(filmModel))
    }
}