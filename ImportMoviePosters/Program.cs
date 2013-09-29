using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Xml;
using HtmlAgilityPack;


namespace ImportMoviePosters
{
    class Program
    {
        static void Main(string[] args)
        {
            MovielensEntities db = new MovielensEntities();

            Console.WriteLine("Are you sure you want to download posters for all images?");
            Console.WriteLine("This should have already happened. Are you sure? (Y/N)");

            if (Console.ReadLine().ToLower() != "y") return;

            int counter = 0;
            foreach (Movie movie in db.Movie.ToList())
            {
                string localImageFile = movie.Id + ".jpg";
                string localImageFilePath = @"../../../Databases/ml-100k/PosterImages/" + localImageFile;

                // Already have image
                if (File.Exists(localImageFilePath)) continue;

                // Query IMDB API & Get Poster URL
                string imdbUrl = movie.IMDBUrl;
                if (String.IsNullOrEmpty(imdbUrl)) continue;

                HtmlWeb client = new HtmlWeb();
                HtmlDocument doc = client.Load(imdbUrl);

                // Find poster url
                HtmlNode tdRow = doc.DocumentNode.SelectSingleNode("//td[@id='img_primary']");
                // Re-directed to search page
                if (tdRow == null)
                {
                    // Get first "Titles" search result
                    HtmlNode titleSearchNode = doc.DocumentNode.SelectSingleNode(@"//h3/a[@name='tt']");
                    if (titleSearchNode == null) // Nothing found - maybe url has id in it
                    {
                        // Find by id
                        int titleStartIndex = imdbUrl.IndexOf("imdb-title-");
                        if (titleStartIndex > 0)
                        {
                            titleStartIndex += 11;
                            string movieId = imdbUrl.Substring(titleStartIndex);
                            string imdbMovieUrl = "http://www.imdb.com/title/tt0" + movieId + "/";
                            doc = client.Load(imdbMovieUrl);
                            tdRow = doc.DocumentNode.SelectSingleNode("//td[@id='img_primary']");
                            if (tdRow == null) continue; // Give up - Nothing found with this id
                        }
                        else continue; // No search results & no id in url
                    }
                    else
                    {
                        HtmlNode findDiv = titleSearchNode.SelectSingleNode(@"../../table/tr/td/a");
                        string searchUrl = "http://www.imdb.com" + findDiv.Attributes["href"].Value;
                        doc = client.Load(searchUrl);
                        tdRow = doc.DocumentNode.SelectSingleNode("//td[@id='img_primary']");
                        if (tdRow == null) continue; // Give up
                    }
                }

                HtmlNode imgNode = tdRow.SelectSingleNode("div/a/img");
                if (imgNode == null) continue; // No image
                string posterUrl = imgNode.Attributes["src"].Value;

                // Save Image to File
                using (var imageClient = new WebClient())
                {                    
                    imageClient.DownloadFile(posterUrl, localImageFilePath);

                    FileStream savedImage = File.Open(localImageFilePath, FileMode.Open);
                    if (!(savedImage.Length > 0)) throw new Exception("Newly saved file not there!?!");
                    savedImage.Close();
                        
                    // Update movie record with url
                    movie.PosterUrl = localImageFile;

                    db.ObjectStateManager.ChangeObjectState(movie, System.Data.EntityState.Modified);
                }
                counter++;
                if (counter % 100 == 0) Console.WriteLine(counter + " movie items processed");
            }
            db.SaveChanges();
            
        }
    }
}
