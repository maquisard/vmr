using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace MovieSuggestor
{
    public class MovieSuggestor
    {
        private User currentUser;
        private SuggestorConnector suggestor;        
        
        private static MovieSuggestor singleton;
        // Controls how much genre similarity accounts for in movie ranking. 
        // 0 = Genre does not matter/We want to explore
        // 1 = Genre does matter/Explore some
        // 4+ = Genre similarity weights heavily/No exploring
        private static double genreRestrictiveness = 4;

        // Hack
        private Dictionary<string, SuggestorUser> users = null;

        private MovieSuggestor() { }

        public static MovieSuggestor GetInstance()
        {
            if (singleton == null)
            {
                singleton = new MovieSuggestor();
                singleton.Initialize();
            }
            return singleton;
        }

        private void Initialize()
        {
            // Initialize a suggestor instance with CollaborativeFiltering as the recommender engine.
            // Pre-Calculations are done in constructor
            MovieExtracter.GetInstance().Initialize();
            Dictionary<string, SuggestorCollection> movies = MovieExtracter.GetInstance().GetMovies().ToDictionary(x => x.Id.ToString(), x => (SuggestorCollection)x);
            //Dictionary<string, SuggestorCollection> ratings = .ToDictionary(x => (x.Key), x => (SuggestorItem)x.Value);
            users = MovieExtracter.GetInstance().GetUsers().ToDictionary(x => (x.Id.ToString()), x => (SuggestorUser)x);

            suggestor = new SuggestorConnector(null, null, users, new Suggestor.Algorithms.CollaborativeFiltering(10));            
            suggestor.Initialize();
        }

        public void InitializeTest()
        {
            suggestor = new SuggestorConnector(null, new Dictionary<string, SuggestorItem>(), users, new Suggestor.Algorithms.CollaborativeFiltering(10));
            suggestor.Initialize();
        }

        public Dictionary<string, SuggestorUser> AllUsers
        {
            get
            {
                return users;
            }
            set
            {
                users = value;
            }
        }

        public void SelectUser(int id)
        {
            currentUser = GetUser(id);
        }

        public User SelectRandomUser()
        {
            User randomUser = MovieExtracter.GetInstance().GetRandomUser();
            currentUser = randomUser;
            return currentUser;
        }

        public List<User> GetUsers()
        {
            return MovieExtracter.GetInstance().GetUsers();
        }

        public User GetUser(int id)
        {
            return MovieExtracter.GetInstance().GetUser(id);
        }

        public List<Movie> GetMovies()
        {
            return MovieExtracter.GetInstance().GetMovies();
        }

        public Movie GetMovie(int id)
        {
            return MovieExtracter.GetInstance().GetMovie(id);
        }

        public User GetCurrentUser()
        {
            return currentUser;
        }

        /// <summary>
        /// Should only be used for testing
        /// </summary>
        /// <param name="user"></param>
        public void SetCurrentUser(User user)
        {
            currentUser = user;
        }

        public List<Movie> RecommendMovies(int userN, int n)
        {
            // Get similar users
            Dictionary<SuggestorUser, double> recommendedUsers = suggestor.SuggestUsers(users.Values.ToList(), currentUser, userN);
            
            // Get top aggregate scored movies
            List<Movie> recommendedMovies = GetTopMovies(recommendedUsers, n);

            AddMovieAttributes(recommendedMovies, recommendedUsers.Keys.Cast<User>().ToList());

            return recommendedMovies;
        }

        public List<Movie> GetExtendedMovieRecommendations(int movieId, int userN, int n, string filterKey, string filterValue)
        {
            // Only retrieve users that have rated this movie AND fulfill the filter key/value
            List<SuggestorUser> filteredUsers = FilterUsers(users.Values.ToList(), movieId, filterKey, filterValue);
            Dictionary<SuggestorUser, double> recommendedUsers = suggestor.SuggestUsers(filteredUsers, currentUser, userN);
            if (recommendedUsers.Count == 0) throw new Exception("No suggested user with filter " + filterKey + " = " + filterValue);

            // Find highest ranked movies & filter by attributes
            List<Movie> topMovies = null;
            // Filter by genre
            if (filterKey.ToLower() == "genre") topMovies = GetTopFilteredMovies(recommendedUsers, movieId, n);
            // Filtering done by users - Already happened at start of function
            else topMovies = GetTopMovies(recommendedUsers, n);

            if (topMovies.Count == 0) throw new Exception("No suggested movies found with filter " + filterKey + " = " + filterValue);

            // Add our query movie - if needed
            Movie queryMovie = GetMovie(movieId);
            if (!topMovies.Contains(queryMovie))
                topMovies.Add(queryMovie);

            AddMovieAttributes(topMovies, recommendedUsers.Keys.Cast<User>().ToList());

            return topMovies;
        }

        private List<Movie> GetTopMovies(Dictionary<SuggestorUser, double> recommendedUsers, int numberOfMoviesToReturn)
        {
            Dictionary<Movie, double> topMovies = new Dictionary<Movie, double>();
            Dictionary<Movie, double> estimatedMovieRatings = new Dictionary<Movie, double>();
            double normalizingFactor = 0; // k = 1 / sum(similarity(user, user-prime))
            foreach (User similarUser in recommendedUsers.Keys)
            {
                double userSimilarityScore = recommendedUsers[similarUser];
                normalizingFactor += userSimilarityScore;
                foreach (Rating rating in similarUser.Ratings)
                {
                    // Ignore movies user has seen
                    if (currentUser.Ratings.Exists(currentUserRating => currentUserRating.MovieId == rating.MovieId)) continue;
                    if (!(topMovies.Keys.Contains(rating.Movie)))
                        topMovies.Add(rating.Movie, 0.0);
                    // Formula: R(u,i) = (1/N) * Sum(R(u,i) in SimilarUsers)
                    //topMovies[rating.Movie] += ((double)rating.Rating1 / (double)recommendedUsers.Count);
                    topMovies[rating.Movie] += (userSimilarityScore * (double)rating.Rating1);
                }
            }
            normalizingFactor = 1.0 / normalizingFactor;

            foreach (Movie canditateMovie in topMovies.Keys.ToList())
            {
                // Formula: R(u,i) = normalizingFactor * sum(similarity(user, user-prime) * itemRating)
                topMovies[canditateMovie] *= normalizingFactor;
            }

            Dictionary<Movie, double> sortedDict = (from entry in topMovies orderby entry.Value descending select entry)
                .ToDictionary(pair => pair.Key, pair => pair.Value);

            List<Movie> recommendedMovies = sortedDict.Keys.Take(numberOfMoviesToReturn).ToList();

            return recommendedMovies;
        }

        private List<Movie> GetTopFilteredMovies(Dictionary<SuggestorUser, double> recommendedUsers, int movieId, int numberOfMoviesToReturn)
        {
            Dictionary<Movie, double> topMovies = new Dictionary<Movie, double>();
            Movie queryMovie = GetMovie(movieId);

            // Find cosine similarity between movie genres

            // These are only users that have rated movie and filtered by user groups - if needed
            double normalizingFactor = 0; // k = 1 / sum(similarity(user, user-prime))
            foreach (User similarUser in recommendedUsers.Keys)
            {
                double userSimilarityScore = recommendedUsers[similarUser];
                normalizingFactor += userSimilarityScore;
                foreach (Rating rating in similarUser.Ratings)
                {
                    //if (!(IsSimilar(queryMovie, rating.Movie))) continue; // Does movie pass our filtering?
                    if (rating.MovieId == movieId) continue; // Dont want to recommend the movie we are extending the recommendation from. Add it later
                    double genreSimilarityScore = suggestor.SuggestCollections(new List<SuggestorCollection>() { rating.Movie }, queryMovie, 1).Values.ToList()[0];
                    if (!(topMovies.Keys.Contains(rating.Movie)))
                        topMovies.Add(rating.Movie, 0.0);
                    // Formula: R(u,i) = (1/N) * Sum(R(u,i) in SimilarUsers)
                   // topMovies[rating.Movie] += ((double)rating.Rating1 / (double)recommendedUsers.Count);
                    
                    double genreSimilarityToExploration = Math.Pow(genreSimilarityScore, genreRestrictiveness);
                    topMovies[rating.Movie] += (userSimilarityScore * ((double)rating.Rating1 * (genreSimilarityToExploration)));
                }
            }

            normalizingFactor = 1.0 / normalizingFactor;

            foreach (Movie canditateMovie in topMovies.Keys.ToList())
            {
                // Formula: R(u,i) = normalizingFactor * sum(similarity(user, user-prime) * itemRating)
                topMovies[canditateMovie] *= normalizingFactor;
            }

            Dictionary<Movie, double> sortedDict = (from entry in topMovies orderby entry.Value descending select entry)
                .ToDictionary(pair => pair.Key, pair => pair.Value);

            List<Movie> recommendedMovies = sortedDict.Keys.Take(numberOfMoviesToReturn).ToList();

            return recommendedMovies;
        }

        private void AddMovieAttributes(List<Movie> recommendedMovies, List<User> recommendedUsers)
        {
            // Find groups used for recommendations
            foreach (Movie recommendedMovie in recommendedMovies)
            {
                /*recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "AgeGroup");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "Occupation");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "Gender");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "Zipcode");*/
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "RankedAgeGroup");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "RankedOccupation");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "RankedGender");
                recommendedMovie.Attributes.RemoveAll(pair => pair.Key == "RankedZipcode");

                Dictionary<string, double> ageGroups = new Dictionary<string, double>();
                Dictionary<string, double> occupationGroups = new Dictionary<string, double>();
                Dictionary<string, double> genderGroups = new Dictionary<string, double>();
                Dictionary<string, double> rankedAgeGroups = new Dictionary<string, double>();
                Dictionary<string, double> rankedOccupationGroups = new Dictionary<string, double>();
                Dictionary<string, double> rankedGenderGroups = new Dictionary<string, double>();

                if (recommendedUsers.Count == 0) return;
                foreach (User similarUser in recommendedUsers)
                {
                    Rating usersMovieRating = similarUser.Rating.Where(rating => rating.MovieId == recommendedMovie.Id).FirstOrDefault();
                    if (usersMovieRating == null) continue; // User did not rate this movie

                    // Age groups                    
                    if (!(ageGroups.Keys.Contains(similarUser.AgeGroup))) ageGroups.Add(similarUser.AgeGroup, 1);
                    else ageGroups[similarUser.AgeGroup] += 1;

                    // Ranked Age groups
                    if (!(rankedAgeGroups.Keys.Contains(similarUser.AgeGroup))) rankedAgeGroups.Add(similarUser.AgeGroup, usersMovieRating.Rating1);
                    else rankedAgeGroups[similarUser.AgeGroup] += usersMovieRating.Rating1;

                    // Occupations
                    if (!(occupationGroups.Keys.Contains(similarUser.Occupation))) occupationGroups.Add(similarUser.Occupation, 1);
                    else occupationGroups[similarUser.Occupation] += 1;

                    // Ranked Occupations
                    if (!(rankedOccupationGroups.Keys.Contains(similarUser.Occupation))) rankedOccupationGroups.Add(similarUser.Occupation, usersMovieRating.Rating1);
                    else rankedOccupationGroups[similarUser.Occupation] += usersMovieRating.Rating1;

                    // Gender
                    if (!(genderGroups.Keys.Contains(similarUser.Gender))) genderGroups.Add(similarUser.Gender, 1);
                    else genderGroups[similarUser.Gender] += 1;

                    // Gender
                    if (!(rankedGenderGroups.Keys.Contains(similarUser.Gender))) rankedGenderGroups.Add(similarUser.Gender, usersMovieRating.Rating1);
                    else rankedGenderGroups[similarUser.Gender] += usersMovieRating.Rating1;

                }

                // Divide aggregate score by number of users
                foreach (string ageGroup in rankedAgeGroups.Keys.ToList())
                    rankedAgeGroups[ageGroup] /= ageGroups[ageGroup];

                foreach (string occupationGroup in rankedOccupationGroups.Keys.ToList())
                    rankedOccupationGroups[occupationGroup] /= occupationGroups[occupationGroup];

                foreach (string genderGroup in rankedGenderGroups.Keys.ToList())
                    rankedGenderGroups[genderGroup] /= genderGroups[genderGroup];

                
                // Age groups
                List<System.Collections.Generic.KeyValuePair<string, double>> topAgeGroups = ageGroups.ToList();
                topAgeGroups.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); } );
                topAgeGroups.Reverse();
                //recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("AgeGroup", topAgeGroups[0].Key));

                // Ranked age groups
                List<System.Collections.Generic.KeyValuePair<string, double>> topRankedAgeGroups = rankedAgeGroups.ToList();
                topRankedAgeGroups.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); } );
                topRankedAgeGroups.Reverse();
                recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("RankedAgeGroup", topRankedAgeGroups[0].Key));

                // Occupation groups
                List<System.Collections.Generic.KeyValuePair<string, double>> topOccupationGroups = occupationGroups.ToList();
                topOccupationGroups.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); } );
                topOccupationGroups.Reverse();
                //recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("Occupation", topOccupationGroups[0].Key));

                // Ranked Occupation groups
                List<System.Collections.Generic.KeyValuePair<string, double>> topRankedOccupationGroups = rankedOccupationGroups.ToList();
                topRankedOccupationGroups.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); } );
                topRankedOccupationGroups.Reverse();
                recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("RankedOccupation", topRankedOccupationGroups[0].Key));

                // Gender groups
                List<System.Collections.Generic.KeyValuePair<string, double>> topGenderGroups = genderGroups.ToList();
                topGenderGroups.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); } );
                topGenderGroups.Reverse();
                //recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("Gender", topGenderGroups[0].Key));

                // Ranked gender groups
                List<System.Collections.Generic.KeyValuePair<string, double>> topRankedGenderGroups = rankedGenderGroups.ToList();
                topRankedGenderGroups.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
                topRankedGenderGroups.Reverse();
                recommendedMovie.Attributes.Add(new KeyValuePair<string, string>("RankedGender", topRankedGenderGroups[0].Key));
            }
        }

        /// <summary>
        /// Two filtering happen: 
        ///   - We only consider users that have rated movie in question.
        ///   - We only consider movies that fulfill the given filterKey and filterValue
        /// </summary>
        /// <param name="users"></param>
        /// <param name="movieId"></param>
        /// <param name="filterKey"></param>
        /// <param name="filterValue"></param>
        /// <returns></returns>
        private List<SuggestorUser> FilterUsers(List<SuggestorUser> users, int movieId, string filterKey, string filterValue)
        {
            // Query only for users that have rated movie in question
            List<SuggestorUser> usersThatRatedMovie = users.Where(user => ((User)user).Ratings.Exists(rating => rating.MovieId == movieId)).ToList();
            if (usersThatRatedMovie.Count == 0) return null; // No users found that rated the movie highly...shouldnt happen

            List<string> userAttributeFilterList = new List<string> { "agegroup", "occupation", "gender", "zipcode", "rankedagegroup", "rankedoccupation", "rankedgender", "rankedzipcode" };

            if (userAttributeFilterList.Contains(filterKey.ToLower()))
            {
                switch (filterKey.ToLower())
                {
                    case "agegroup":
                    case "rankedagegroup":
                        usersThatRatedMovie = usersThatRatedMovie.Where(user => ((User)user).AgeGroup == filterValue).ToList();
                        break;
                    case "occupation":
                    case "rankedoccupation":
                        usersThatRatedMovie = usersThatRatedMovie.Where(user => ((User)user).Occupation == filterValue).ToList();
                        break;
                    case "gender":
                    case "rankedgender":
                        usersThatRatedMovie = usersThatRatedMovie.Where(user => ((User)user).Gender == filterValue).ToList();
                        break;
                    case "zipcode":
                    case "rankedzipcode":
                        usersThatRatedMovie = usersThatRatedMovie.Where(user => ((User)user).Zipcode == filterValue).ToList();
                        break;
                }
            }

            return usersThatRatedMovie;
        }

        private bool IsSimilar(Movie queryMovie, Movie compareMovie)
        {
            // TODO: Feel like this could be done dynamically - or at least cleaner
            
            if (queryMovie.Action) if (!compareMovie.Action) return false;
            if (queryMovie.Adventure) if (!compareMovie.Adventure) return false;
            if (queryMovie.Animation) if (!compareMovie.Animation) return false;
            if (queryMovie.Childrens) if (!compareMovie.Childrens) return false;
            if (queryMovie.Comedy) if (!compareMovie.Comedy) return false;
            if (queryMovie.Crime) if (!compareMovie.Crime) return false;
            if (queryMovie.Documentary) if (!compareMovie.Documentary) return false;
            if (queryMovie.Drama) if (!compareMovie.Drama) return false;
            if (queryMovie.Fantasy) if (!compareMovie.Fantasy) return false;
            if (queryMovie.Film_Noir) if (!compareMovie.Film_Noir) return false;
            if (queryMovie.Horror) if (!compareMovie.Horror) return false;
            if (queryMovie.Musical) if (!compareMovie.Musical) return false;
            if (queryMovie.Mystery) if (!compareMovie.Mystery) return false;
            if (queryMovie.Romance) if (!compareMovie.Romance) return false;
            if (queryMovie.Sci_Fi) if (!compareMovie.Sci_Fi) return false;
            if (queryMovie.Thriller) if (!compareMovie.Thriller) return false;
            if (queryMovie.Unknown) if (!compareMovie.Unknown) return false;
            if (queryMovie.War) if (!compareMovie.War) return false;
            if (queryMovie.Western) if (!compareMovie.Western) return false;
            return true;
        }
    }
}
