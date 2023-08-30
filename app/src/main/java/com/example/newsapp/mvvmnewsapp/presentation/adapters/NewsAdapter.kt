package com.example.newsapp.mvvmnewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.mvvmnewsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        Glide.with(holder.itemView).load(article.urlToImage).into(holder.binding.imgArticle)
        holder.binding.apply {
            tvArticlePublishedAt.text = article.publishedAt
            tvArticleName.text = article.source?.name
            tvArticleDescription.text = article.description
            tvArticleTitle.text = article.title

        }
        holder.itemView.setOnClickListener { onItemClickListener?.let { it(article) } }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}