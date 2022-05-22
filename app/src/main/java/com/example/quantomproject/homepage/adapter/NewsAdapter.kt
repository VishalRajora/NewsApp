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
import java.text.DateFormat
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


            //image setup
            binding.ivNews.load(modelClass.urlToImage) {
                crossfade(true)
                placeholder(R.drawable.ic_baseline_image_24)
                transformations(RoundedCornersTransformation(10f))
            }

            //date setup
            val date: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            date.timeZone = TimeZone.getTimeZone("GMT")
            val result: Long
            try {
                result = date.parse(modelClass.publishedAt).time; //2019-02-25T22:43:23.213
                val ago = TimeAgo.using(result)
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