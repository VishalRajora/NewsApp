package com.example.quantomproject.homepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.quantomproject.R
import com.example.quantomproject.databinding.NewsSingleRowBinding
import com.example.quantomproject.homepage.data.Article
import com.github.marlonlom.utilities.timeago.TimeAgo
import java.text.SimpleDateFormat
import java.util.*

class NewsAdapter :
    ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            NewsSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class NewsViewHolder(val binding: NewsSingleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(modelClass: Article) {
            binding.news = modelClass

            binding.ivNews.load(modelClass.urlToImage) {
                crossfade(true)
                placeholder(R.drawable.ic_baseline_image_24)
                transformations(RoundedCornersTransformation(10f))
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            sdf.timeZone = TimeZone.getTimeZone("GMT")

            try {
                val time: Long = sdf.parse(modelClass.publishedAt).time
                val ago = TimeAgo.using(time)
                binding.tvDateTime.text = ago
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class NewsDiffUtil : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean = oldItem.title == newItem.title

        override fun areContentsTheSame(
            oldItem: Article,
            newItem: Article
        ): Boolean = oldItem == newItem

    }
}