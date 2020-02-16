package ru.alekseydanilov.topfilmstoday

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.recyclerview_films.view.*
import ru.alekseydanilov.topfilmstoday.model.FilmModel

class FilmRecyclerAdapter(val filmsList: MutableList<FilmModel>, val context: Context) :
    RecyclerView.Adapter<FilmRecyclerAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return filmsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recyclerview_films,
                parent,
                false
            )
        )
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val filmTitle = view.film_title
        val filmDate = view.film_date
        val filmPopularity = view.film_popularity
        val filmPoster = view.film_poster

        var showMore: Button = view.show_more

        val recyclerSecondLayout = view.recycler_second_layout

        val originalVote = view.original_vote
        val originalName = view.original_name
        val originalLang = view.original_lang
        val filmDescription = view.film_description
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val film: FilmModel = filmsList[position]

        holder.filmTitle.text = film.title

        Glide.with(context)
            .load(context.getString(R.string.image_url, film.poster_path))
            .centerCrop()
            .dontAnimate()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .skipMemoryCache(true)
            .into(holder.filmPoster)

        holder.filmDate.text = "Дата выхода: ${film.release_date}"
        holder.filmPopularity.text = "Популярность: ${film.popularity}"

        holder.originalVote.text = "Зрительский рейтинг: ${film.vote_average}/10"
        holder.originalName.text = "Оригинальное название: ${film.original_title}"
        holder.originalLang.text = "Оригинальный язык фильма: ${film.original_language}"
        holder.filmDescription.text = "Описание фильма:\n ${film.overview}"

        holder.recyclerSecondLayout.visibility = (View.GONE)
        holder.showMore.text = "Показать информацию"

        holder.showMore.setOnClickListener() {_ ->
            if (holder.recyclerSecondLayout!!.visibility == View.VISIBLE) {
                holder.showMore.text = "Показать информацию"
                holder.recyclerSecondLayout.visibility = View.GONE
            } else {
                holder.showMore.text = "Скрыть информацию"
                holder.recyclerSecondLayout.visibility = View.VISIBLE
            }
        }
    }


}
