# Mitch Tabian's Local Database Cache with REST API, Retrofit2, MVVM Architecture, Dagger app ported to Kotlin!

### It integrates Google's JetPack libraries and employs the latest best practices. It's a work in progress, so if you would like to help make it better, feel free to submit questions, comments, pull requests, etc.

### You can find the original app, written in Java by Mitch Tabian, here:

<a href='https://codingwithmitch.com/courses/android-local-database-cache-rest-api/' target='_blank'>Local Database Cache with REST API, Retrofit2, MVVM Architecture, Dagger</a>

Mitch explains the entire app in his exellent course, which you can find on his website, <a href='https://codingwithmitch.com' target='_blank'>codingwithmitch.com</a>. Check out this and all his other courses, too. This one's for members only, but it's totally worth it! It pulls together all those features you kinda know how to use, but you're not sure how to put it all together. 

### Here's a link to the course demo. Details for the Kotlin port contained in this repo are at the bottom.

<a href='https://codingwithmitch.com/courses/android-local-database-cache-rest-api/demo/' target='_blank'><img class='header-img' src='https://codingwithmitch.s3.amazonaws.com/static/android-local-database-cache-rest-api/images/rest_api_database_cache_mvvm.png' /></a>

<h3><a href='https://codingwithmitch.com/courses/android-local-database-cache-rest-api/demo' target='_blank'>Watch the app demo</a>.</h3>

<br>
In Mitch's course you'll learn how to build a <strong>local database cache with SQLite and Room</strong>. The cache retrieves data from a REST API using Retrofit2. Architecture is MVVM.
<br><br>
<strong>Here's the specifics of what you will see in the course:</strong>
<br><br>
<ol>
<li>Caching data for when the network goes offline</li>
<li>Reading cached data when the network is down</li>
<li>Custom SQLite queries using Room</li>
<li>Customizing the cache (how long data will live in the cache)</li>
<li>How Retrofit caching works</li>
<li>Why SQLite and Room is better for caching than Retrofit</li>
<li>How to design a database cache
<ul>
<li>There is no "one size fits all"</li>
<li>Retrofit is better for some things but room is better for others</li>
</ul>
</li>
<li>How Glide caching works</li>
<li>Glide RecyclerView Preloader (Customizing how many list items get cached)</li>
<li>Dealing with Network Errors and slow network speeds</li>
<li>OkHttp Network Timeouts</li>
<li>Converting Retrofit Calls to LiveData (Call Adapters)</li>
<li>And much more...</li>
</ol>
<br>

<strong>Architecture Diagram</strong>
<br><br>
<div class="text-center">
<img class="img-fluid text-center" src="https://codingwithmitch.s3.amazonaws.com/static/blog/8/mvvm_architecture.png"/>
</div>
<br><br>

# Kotlin Port

## Reference Repos

Mitch's original Java app, described above:

<a href='https://codingwithmitch.com/courses/android-local-database-cache-rest-api/' target='_blank'>Local Database Cache with REST API, Retrofit2, MVVM Architecture, Dagger</a>

Google's GithubBrowserSample app in Kotlin, which you can find on their googlesamples repo. 

<a href='https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample' target='_blank'>GithubBrowserSample in Kotlin</a>

## Disclaimer

This is a work in progress and nowhere near done. Feel free to submit questions, comments, and pull requests.

* At present, it contains all the Room, Retrofit, Repository, Dagger, AndroidX, and Navigation infrastructure. 
* It's set up for MVVM, but the main Fragments and ViewModels are not implemented yet, so it runs but there's nothing on the screen.
* Google does a lot of super complicated stuff in their code that I have to admit I don't completely understand yet. I'm slowly working through it and incorporating the parts that are relevant to this app.