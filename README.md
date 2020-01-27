how to run:
1) sbt run

Link to the app on heroku: https://ask-movie-app.herokuapp.com/

What does this app contain:
1) Play Framework web app written in Scala
2) Single search box form
3) Gets a movie from a third-party service
4) Parses the movie plot and uses randomly as Search Terms to produce up to 10 related movies
5) Creates links to the related movies as search terms for the next search
6) Movies are distinct and so are the search terms

What did I try to show:
1) Inheritance through Trait
2) Passing a function as an argument
3) Usage of Objects
4) Pattern matching
5) Chaining operations on collections
6) Usage of Futures
7) Getting data from a third-party service
8) Reading json response into class
9) Follow MVC
10) Implement a recursive function
11) Strict immutable (val)
12) Use implicit
13) Utilize java libraries

Things to improve:
1) Refactoring code out of the Search Controller class
2) Write unit tests
3) Add test coverage plugin to make sure the coverage is good
4) Styling to the pages
5) Configuration: 
- The secret code for production shouldn't be stored in application.config
- The API key for the movie database service should be store differently
- Probably not all referers should be allowed to the app
6) Implement the sevice to fill up 10 movies in every query or at least exhust some large amount of attemtps

Bugs:
1) Some movies although correct search terms end up being a "dead end"
2) Improve which characters are removed from the potential search terms (example: Carlito's should keep the ' charecter)
