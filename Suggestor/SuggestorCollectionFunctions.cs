using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public static class SuggestorCollectionFunctions
    {
        public static double CosineScore(SuggestorCollection collection, SuggestorCollection otherCollection)
        {
            // TF-IDF
            Dictionary<string, SuggestorCollectionLine> mutualAttributes = new Dictionary<string, SuggestorCollectionLine>();
            mutualAttributes = collection.CollectionLines.Keys.Intersect(otherCollection.CollectionLines.Keys).ToDictionary(t => t, t => collection.CollectionLines[t]);
            double cosScore = 0;
            double termProductSum = 0;
            foreach (string lineNo in mutualAttributes.Keys)
            {
                termProductSum += collection.CollectionLines[lineNo].Weight * otherCollection.CollectionLines[lineNo].Weight;
            }
            double vectorNormalizingMultiplication = (GetSize(collection) * GetSize(otherCollection));
            if (vectorNormalizingMultiplication == 0)
                return 0;
            cosScore = termProductSum / vectorNormalizingMultiplication;
            return cosScore;
        }

        public static double CosineScore(SuggestorUser user, SuggestorUser otherUser)
        {
            // TF-IDF
            Dictionary<string, SuggestorCollectionLine> mutualLines = new Dictionary<string, SuggestorCollectionLine>();
            mutualLines = user.CollectionLines.Keys.Intersect(otherUser.CollectionLines.Keys).ToDictionary(t => t, t => user.CollectionLines[t]);
            
            double cosScore = 0;
            double termProductSum = 0;
            foreach (string lineId in mutualLines.Keys)
            {
                termProductSum += user.CollectionLines[lineId].Weight * otherUser.CollectionLines[lineId].Weight;
            }
            double vectorNormalizingMultiplication = (GetSize(user) * GetSize(otherUser));
            if (vectorNormalizingMultiplication == 0)
                return 0;
            cosScore = termProductSum / vectorNormalizingMultiplication;
            return cosScore;
        }

        public static void CalculateTFIDF(SuggestorCollection collection, double numberOfCollections)
        {
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                line.Weight= ((1.0 + Math.Log(line.Quantity)) * Math.Log(numberOfCollections / line.Quantity));
            }
        }

        /*
        public static double GetLimitedSize(SuggestorCollection collection, Dictionary<string, SuggestorCollectionLine> mutualCollectionLines)
        {
            double squaredSum = 0;
            foreach (string invoiceLineNo in mutualCollectionLines.Keys)
            {
                squaredSum += Math.Pow(collection.CollectionLines[invoiceLineNo].Weight, 2);
            }
            return Math.Sqrt(squaredSum);
        }
         */
        /*
        public static double GetLimitedSize(SuggestorUser user, Dictionary<string, SuggestorCollectionLine> mutualCollectionLines)
        {
            double squaredSum = 0;
            foreach (string invoiceLineNo in mutualCollectionLines.Keys)
            {
                squaredSum += Math.Pow(user.CollectionLines[invoiceLineNo].Weight, 2);
            }
            return Math.Sqrt(squaredSum);
        }
        */

        public static double GetSize(SuggestorCollection collection)
        {
            double squaredSum = 0;
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                squaredSum += Math.Pow(line.Weight, 2);
            }
            return Math.Sqrt(squaredSum);
        }

        public static double GetSize(SuggestorUser user)
        {
            double squaredSum = 0;
            foreach (SuggestorCollectionLine line in user.CollectionLines.Values)
            {
                squaredSum += Math.Pow(line.Weight, 2);
            }
            return Math.Sqrt(squaredSum);
        }

        public static void PrintInvoice(SuggestorCollection collection)
        {
            Console.WriteLine("--------------------------");
            Console.WriteLine("Collection: " + collection.CollectionId);
            
            foreach (SuggestorCollectionLine line in collection.CollectionLines.Values)
            {
                Console.WriteLine("     " + line.Description + "    " + line.Quantity);
            }
            Console.WriteLine();
            Console.WriteLine();
        }
    }
}
