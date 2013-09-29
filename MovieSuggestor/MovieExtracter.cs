using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace MovieSuggestor
{
    public class MovieExtracter
    {
        private MovielensEntities db;

        private static MovieExtracter singleton;

        private MovieExtracter() { }

        public static MovieExtracter GetInstance()
        {
            if (singleton == null)
            {
                singleton = new MovieExtracter();
                singleton.Initialize();
            }
            return singleton;
        }

        public void Initialize()
        {
            db = new MovielensEntities();
            db.Configuration.ProxyCreationEnabled = false;
            db.Configuration.LazyLoadingEnabled = false;
        }

        public List<Movie> GetMovies()
        {
            return db.Movie.ToList();
        }

        public Movie GetMovie(int id)
        {
            return db.Movie.Where(movie => movie.Id == id).First();
        }

        public List<User> GetUsers()
        {
            return db.User.Include("Rating").ToList();
        }

        public User GetUser(int id)
        {
            return db.User.Include("Rating").Where(user => user.Id == id).First();
        }

        public User GetRandomUser()
        {
            Random random = new Random();
            int randomId  = random.Next(db.User.Count()-1);
            return GetUser(randomId);
        } 
    }
}
