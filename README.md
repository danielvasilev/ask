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

Things to improve:
1) Refactoring code out of the Search Controller class
2) Write unit tests
3) Add test coverage plugin to make sure the coverage is good
4) Styling to the pages
5) Configuration: 
- The secret code for production shouldn't be stored in application.config
- The API key for the movie database service should be store differently
- Probably not all referers should be allowed to the app
