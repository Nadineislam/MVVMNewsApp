package com.example.newsapp.mvvmnewsapp.presentation.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.mvvmnewsapp.presentation.adapters.NewsAdapter
import com.example.newsapp.mvvmnewsapp.presentation.ui.activities.NewsActivity
import com.example.newsapp.mvvmnewsapp.presentation.viewmodel.NewsViewModel
import com.example.newsapp.mvvmnewsapp.data.util.Constants
import com.example.newsapp.mvvmnewsapp.data.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.mvvmnewsapp.data.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)

            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment, bundle

            )

        }
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.getSearchNews(editable.toString())

                    }
                }
            }
        }
        lifecycleScope.launch {
                viewModel.searchNews.collect{response->
                    when(response){
                        is Resource.Loading->showProgressBar()
                        is Resource.Success->{hideProgressBar()
                            response.data?.let { newsResponse ->
                                newsAdapter.differ.submitList(newsResponse.articles.toList())
                                val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                                isLastPage = viewModel.breakingNewsPage == totalPages
                                if (isLastPage) {
                                    binding.rvSearchNews.setPadding(0, 0, 0, 0)
                                }
                            }}
                        is Resource.Error->response.message?.let { message->
                            Toast.makeText(activity,"An error occurred: $message",Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    private fun showProgressBar() {
        binding.pbSearchNews.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        binding.pbSearchNews.visibility = View.INVISIBLE
        isLoading = false
    }

    var isLoading = false
    var isScrolling = false
    var isLastPage = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getSearchNews(binding.etSearch.text.toString())
                isScrolling = false
            }

        }
    }
}
