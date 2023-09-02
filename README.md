# MVVMNewsApp
* NewsApp is an MVVM app applies the best practices and principles that built for android platform using kotlin as programming language.
* Fetches news from an API https://newsapi.org/ and cache the results then show them in a RecyclerView.
* Supports offline news browsing.
* Supports paging.
* Supports searching news.
* Displays news from USA.

  <p float="left">
  <img src="newsHome.png" width="100" />
  <img src="/img2.png" width="100" /> 
  <img src="/img3.png" width="100" />
</p>

# Libraries And Technoligies used

* MVVM : Android architecture used to saperate logic code from ui and save the application state in case the configuration changes.
* Retrofit + Gson Converter : Fetch news from rest api as a gson file and convert it to a kotlin object.
* Room : Save the articles into a local database.
* Coroutines : Executing some code in the background.
* Dagger Hilt : Dependency injection.
* Navigation Component : Navigate between fragments.
* Glide : Catch and cache images from the internet and show them in an imageView.
* View Binding : to access the views without needing to infalte them.
