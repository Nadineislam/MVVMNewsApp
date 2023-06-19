package com.example.newsapp.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.mvvmnewsapp.models.Article
import kotlinx.android.synthetic.main.item_article.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    //when i was put list here as a parameter and when i add new article it added to the list and call adapter.notify data set changed
    //that's very inefficient because by using notify the adapter always update its whole items even the items that didn't change
    //we can solve this problem using diffutils it calculates the differences between two lists and only update the changed items
    //another advantage is that it's happen in background so it's not disturb main thread
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.item.apply {
            Glide.with(this).load(article.urlToImage).into(img_article)
            tv_article_publishedAt.text = article.publishedAt
            tv_article_name.text = article.source?.name
            tv_article_description.text = article.description
            tv_article_title.text = article.title
            setOnClickListener { onItemClickListener?.let { it(article) } }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsViewHolder(val item: View) : RecyclerView.ViewHolder(item)

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