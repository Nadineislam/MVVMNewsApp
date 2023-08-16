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
    //when i was put list here as a parameter and when i add new article it added to the list and call adapter.notify data set changed
    //that's very inefficient because by using notify the adapter always update its whole items even the items that didn't change
    //we can solve this problem using diffutils it calculates the differences between two lists and only update the changed items
    //another advantage is that it's happen in background so it's not disturb main thread
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

    //this is the tool that compares the two list and only update changed items
    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //maybe i use oldItem.id but data from api don't have default id so, we only use id for local database
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    //this tool take the 2 lists and compares them and calculates the difference it's async which means it runs on background
    val differ = AsyncListDiffer(this, differCallBack)
    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}