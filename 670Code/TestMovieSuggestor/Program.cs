using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using MovieSuggestor;
using Suggestor;

namespace TestMovieSuggestor
{
    class Program
    {
        static void Main(string[] args)
        {
            List<int> usersParams = new List<int>() { 10, 20, 40, 80, 200 };
            List<int> moviesParams = new List<int>() { 10, 20, 40, 80 };
            FileStream resultsStream = File.Open("results.txt", FileMode.Create);
            StreamWriter resultsWriter = new StreamWriter(resultsStream);

            foreach (int userParam in usersParams)
            {
                foreach (int movieParam in moviesParams)
                {
                    int numberOfFolds = 5;

                    int numberOfSimilarUsersToGet = userParam;
                    int numberOfSimilarMoviesToGet = movieParam;

                    double averageRecall = 0.0;
                    double averagePrecision = 0.0;
                    double averageRecallQuality = 0.0;


                    foreach (int fileNumber in Enumerable.Range(1, numberOfFolds))
                    {
                        string baseFile = "u" + fileNumber + ".base";
                        string testFile = "u" + fileNumber + ".test";
                        List<SuggestorUser> baseUsers = null;
                        List<SuggestorUser> testUsers = null;

                        // Read files
                        ReadRatingsFile("../../../Databases/ml-100k/" + baseFile, out baseUsers);
                        ReadRatingsFile("../../../Databases/ml-100k/" + testFile, out testUsers);

                        // Run recommendation for every test user
                        MovieSuggestor.MovieSuggestor suggestor = MovieSuggestor.MovieSuggestor.GetInstance();
                        suggestor.AllUsers = baseUsers.ToDictionary(user => user.Id.ToString(), user => user);
                        suggestor.InitializeTest();

                        double recallAggregate = 0;
                        double precisionAggregate = 0;
                        double recallQualityAggregate = 0;
                        int usersTested = 0;
                        double usersWithRelevantRatings = 0;

                        foreach (User testUser in testUsers)
                        {
                            suggestor.SetCurrentUser(testUser);
                            List<Movie> recommendedMovies = suggestor.RecommendMovies(numberOfSimilarUsersToGet, numberOfSimilarMoviesToGet);

                            // Recall
                            User baseUser = (User)baseUsers.Where(user => ((User)user).Id == testUser.Id).FirstOrDefault();
                            List<Rating> relevantRatings = baseUser.Ratings.Join(recommendedMovies, rating => rating.MovieId, movie => movie.Id, (rating, movie) => rating).ToList();
                            //var movieToRatingIntersection =
                            //    (from movie in recommendedMovies select movie.Id).Intersect(from rating in baseUser.Ratings select rating.MovieId);
                            double userRecallQuality = 0;
                            foreach (Rating rating in relevantRatings)
                            {
                                userRecallQuality += (double)rating.Rating1 / 5.0;
                            }

                            double relevant = relevantRatings.Count();
                            double recall = relevant / (double)baseUser.Ratings.Count;
                            double precision = relevant / (double)numberOfSimilarMoviesToGet;
                            if (relevant == 0)
                                userRecallQuality = 0;
                            else
                            {
                                userRecallQuality /= (double)relevantRatings.Count;
                                usersWithRelevantRatings++;
                            }


                            recallAggregate += recall;
                            precisionAggregate += precision;
                            recallQualityAggregate += userRecallQuality;

                            usersTested++;
                            if (usersTested % 100.0 == 0) Console.WriteLine(usersTested + " users tested");
                        }

                        // See if 
                        recallAggregate /= (double)testUsers.Count;
                        precisionAggregate /= (double)testUsers.Count;
                        recallQualityAggregate /= usersWithRelevantRatings;

                        averageRecall += recallAggregate;
                        averagePrecision += precisionAggregate;
                        averageRecallQuality += recallQualityAggregate;

                        Console.WriteLine("Done with fold number " + fileNumber);
                    }

                    averageRecall /= (double)numberOfFolds;
                    averagePrecision /= (double)numberOfFolds;
                    averageRecallQuality /= (double)numberOfFolds;
                    double f1Score = 2.0 * (averagePrecision * averageRecall / (averagePrecision + averageRecall));

                    string runSetup = "Recommender evaluation. Folds = " + numberOfFolds + ", NumberOfSimilarUsersConsidered = " + numberOfSimilarUsersToGet + ", NumberOfMoviesToRecommend: " + numberOfSimilarMoviesToGet;
                    Console.WriteLine(runSetup);
                    Console.WriteLine("Average fold-user recall: " + averageRecall);
                    Console.WriteLine("Average fold-user precision: " + averagePrecision);
                    Console.WriteLine("Average fold-user recall quality: " + averageRecallQuality);
                    Console.WriteLine("Average F1 Score: " + f1Score);
                    resultsWriter.WriteLine(runSetup + "|" + averagePrecision + "|" + averageRecall + "|" + averageRecallQuality + "|" + f1Score);
                }
            }
            Console.WriteLine("Done!");
            Console.ReadLine();
            resultsWriter.Close();
        }

        private static void ReadRatingsFile(string filename, out List<SuggestorUser> users)
        {
            TextReader reader = File.OpenText(filename);
            string fileContent = reader.ReadToEnd();            
            users = new List<SuggestorUser>();
            

            Dictionary<int, User> usersDict = new Dictionary<int, User>();
            Dictionary<int, Movie> movieDict = new Dictionary<int, Movie>();

            using (StringReader stringReader = new StringReader(fileContent))
            {
                string line;
                while ((line = stringReader.ReadLine()) != null)
                {
                    // UserId / MovieId / Rating / UnixTimestamp
                    string[] values = line.Split('\t');
                    int userId = Int32.Parse(values[0]);
                    int movieId = Int32.Parse(values[1]);
                    int rating = Int32.Parse(values[2]);

                    if (!(movieDict.ContainsKey(movieId))) movieDict.Add(movieId, new Movie());
                    movieDict[movieId].Id = movieId;
                    
                    Rating newRating = new Rating();
                    newRating.Movie = movieDict[movieId];
                    newRating.MovieId = movieId;
                    newRating.Rating1 = rating;
                    newRating.UserId = userId;


                    if (!(usersDict.ContainsKey(userId)))
                    {
                        usersDict.Add(userId, new User());
                        usersDict[userId].Id = userId;
                        usersDict[userId].Age = 20;
                        usersDict[userId].Gender = "F";
                        usersDict[userId].Occupation = "Bum";
                    }
                    usersDict[userId].Rating.Add(newRating);
                }
            }

            users = usersDict.Values.Cast<SuggestorUser>().ToList();

            
        }
    }
}
