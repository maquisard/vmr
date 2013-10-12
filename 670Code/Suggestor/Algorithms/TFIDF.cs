using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor.Algorithms
{
    public class TFIDF : SuggestorRecommender
    {
        // TODO: Keep collections global here or in SuggestorConnector.

        private int defaultNoOfCollectionsToSuggest = 10;

        public TFIDF(int defaultNoOfCollectionsToSuggest)
        {
            this.defaultNoOfCollectionsToSuggest = defaultNoOfCollectionsToSuggest;
        }

        public override void PreCalculation(Dictionary<string, SuggestorCollection> collections, Dictionary<string, SuggestorItem> items)
        {
            // Calculate TF-IDF of all lines in all collections
            CalculateInvoiceWeights(collections, items);
        }

        public override Dictionary<string, double> SuggestNItems(Dictionary<string, SuggestorCollection> collections, SuggestorCollection compareCollection, int n)
        {
            if (compareCollection.CollectionLines.Count == 0) return null;

            Dictionary<string, double> itemAggregation = new Dictionary<string, double>();

            // Find top similar collections
            Dictionary<SuggestorCollection, double> topCollections = SuggestNCollections(collections.Values.ToList(), compareCollection, n);

            foreach (SuggestorCollection collection in topCollections.Keys)
            {
                foreach (string itemNo in collection.CollectionLines.Keys)
                {
                    if (compareCollection.CollectionLines.ContainsKey(itemNo)) continue; // Ignore items that are already in basket
                    if (!(itemAggregation.ContainsKey(itemNo))) itemAggregation.Add(itemNo, 1);
                    itemAggregation[itemNo]++;
                    //itemAggregation[itemNo] += topInvoices[invoice] * 1.0;
                }
            }

            List<KeyValuePair<string, double>> itemAggregationList = itemAggregation.ToList();
            // Sort dictionary by value
            itemAggregationList.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            itemAggregationList.Reverse();
            itemAggregation = itemAggregationList.ToDictionary(pair => pair.Key, pair => pair.Value);
            return itemAggregation;
        }

        public override Dictionary<SuggestorCollection, double> SuggestNCollections(List<SuggestorCollection> collections, SuggestorCollection compareCollection, int n)
        {
            // TODO: Disgusting code
            Dictionary<SuggestorCollection, double> topCollections = new Dictionary<SuggestorCollection, double>();
            List<SuggestorCollection> compareCollections = collections;
            compareCollections.Remove(compareCollection);
            // Calculate all weights
            // TODO: Cache this. Per instance?
            foreach (SuggestorCollection invoice in compareCollections)
            {
                topCollections.Add(invoice, SuggestorCollectionFunctions.CosineScore(invoice, compareCollection));
            }

            List<KeyValuePair<SuggestorCollection, double>> topValues = topCollections.ToList();
            topValues.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            topValues.Reverse();
            Dictionary<SuggestorCollection, double> returnInvoices = new Dictionary<SuggestorCollection, double>();
            for (int i = 0; i < n; i++)
            {
                if (topValues[i].Value != 0)
                    returnInvoices.Add(topValues[i].Key, topValues[i].Value);
            }
            return returnInvoices;
        }

        private void CalculateInvoiceWeights(Dictionary<string, SuggestorCollection> invoices, Dictionary<string, SuggestorItem> items)
        {
            foreach (SuggestorCollection invoice in invoices.Values)
            {
                SuggestorCollectionFunctions.CalculateTFIDF(invoice, invoices.Count);
            }
        }
        
        /*
        private Dictionary<SuggestorCollection, double> FindClosestInvoices(Dictionary<string, SuggestorCollection> collections, SuggestorCollection compareCollection, int n)
        {
            // TODO: Disgusting code
            Dictionary<SuggestorCollection, double> topInvoices = new Dictionary<SuggestorCollection, double>();
            List<SuggestorCollection> compareInvoices = collections.Values.ToList();
            compareInvoices.Remove(compareCollection);
            // Calculate all weights
            // TODO: Cache this. Per instance?
            foreach (SuggestorCollection invoice in compareInvoices)
            {
                topInvoices.Add(invoice, SuggestorCollectionFunctions.CosineScore(invoice, compareCollection));
            }

            List<KeyValuePair<SuggestorCollection, double>> topValues = topInvoices.ToList();
            topValues.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            topValues.Reverse();
            Dictionary<SuggestorCollection, double> returnInvoices = new Dictionary<SuggestorCollection, double>();
            for (int i = 0; i < n; i++)
            {
                if (topValues[i].Value != 0)
                    returnInvoices.Add(topValues[i].Key, topValues[i].Value);
            }
            return returnInvoices;
        }

        public Dictionary<string, double> GetRecommendedItems(SuggestorCollection compareInvoice)
        {
            if (compareInvoice.GetCollectionLines().Count == 0) return null;

            Dictionary<string, double> itemAggregation = new Dictionary<string, double>();

            Dictionary<SuggestorCollection, double> topInvoices = FindClosestInvoices(compareInvoice, 10);

            foreach (SuggestorCollection invoice in topInvoices.Keys)
            {
                foreach (string itemNo in invoice.GetCollectionLines().Keys)
                {
                    if (compareInvoice.GetCollectionLines().ContainsKey(itemNo)) continue; // Ignore items that are already in basket
                    if (!(itemAggregation.ContainsKey(itemNo))) itemAggregation.Add(itemNo, 1);
                    itemAggregation[itemNo]++;
                    //itemAggregation[itemNo] += topInvoices[invoice] * 1.0;
                }
            }

            List<KeyValuePair<string, double>> itemAggregationList = itemAggregation.ToList();
            // Sort dictionary by value
            itemAggregationList.Sort((firstPair, nextPair) => { return firstPair.Value.CompareTo(nextPair.Value); });
            itemAggregationList.Reverse();
            itemAggregation = itemAggregationList.ToDictionary(pair => pair.Key, pair => pair.Value);
            return itemAggregation;
        }*/

        public override Dictionary<SuggestorUser, double> SuggestNUsers(List<SuggestorUser> users, SuggestorUser user, int n)
        {
            throw new NotImplementedException();
        }
    }
}
