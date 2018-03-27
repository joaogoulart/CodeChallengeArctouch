package com.arctouch.codechallenge.features.home.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.common.utils.MovieImageUrlBuilder
import com.arctouch.codechallenge.features.home.model.Movie
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter(private val movies: MutableList<Movie>, val onLoadMoreData:(ProgressBar) -> Unit, val onClickMovie:(Movie, ImageView) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var isLoading = false
    var isMoreDataAvailable = true

    init {
        setHasStableIds(true)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie, onClickMovie:(Movie, ImageView) -> Unit) {
            itemView.cardViewMovie.setOnClickListener {
                onClickMovie(movie, itemView.posterImageView)
            }
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate

            Glide.with(itemView)
                .load(movie.posterPath?.let { MovieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(itemView.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun getItemId(position: Int): Long = movies[position].id.toLong()

    fun handleItens(list: List<Movie>) {
        val diffCallback = MoviesDiffCallback(this.movies, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.movies.clear()
        this.movies.addAll(list)
        diffResult.dispatchUpdatesTo(this)
        isLoading = false
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val progressLoadMore = holder.itemView.progressLoadMore
        if (position >= itemCount - 1 && !isLoading && isMoreDataAvailable) {
            isLoading = true
            onLoadMoreData(progressLoadMore)
            progressLoadMore.visibility = View.VISIBLE
        } else {
            progressLoadMore.visibility = View.GONE
        }

        holder.bind(movies[position], onClickMovie)
    }
}
