using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public class SuggestorConnector
    {
        //private Dictionary<SuggestorInvoice, double> similarInvoices;
        //private Dictionary<string, double> recommendedItems;
        private Dictionary<string, SuggestorItem> items;
        private Dictionary<string, SuggestorCollection> collections;
        private Dictionary<string, SuggestorUser> users;

        private SuggestorRecommender recommenderEngine;

        public SuggestorConnector(Dictionary<string, SuggestorCollection> collections, Dictionary<string, SuggestorItem> items, Dictionary<string, SuggestorUser> users, SuggestorRecommender recommenderEngine)
        {
            this.collections = collections;
            this.items = items;
            this.users = users;
            this.recommenderEngine = recommenderEngine;
            //Initialize();
        }

        public void SetUsers(Dictionary<string, SuggestorUser> users)
        {
            this.users = users;
        }

        public void Initialize()
        {
            recommenderEngine.PreCalculation(collections, items);
        }

        public Dictionary<string, double> SuggestItems(SuggestorCollection compareCollection, int n)
        {
            return recommenderEngine.SuggestNItems(collections, compareCollection, n);
        }

        public Dictionary<SuggestorCollection, double> SuggestCollections(List<SuggestorCollection> collectionsToCompareTo, SuggestorCollection compareCollection, int n)
        {
            return recommenderEngine.SuggestNCollections(collectionsToCompareTo, compareCollection, n);
        }

        public Dictionary<SuggestorUser, double> SuggestUsers(List<SuggestorUser> usersToCompareTo, SuggestorUser user, int n)
        {
            return recommenderEngine.SuggestNUsers(usersToCompareTo, user, n);
        }

        /*
        public void Initialize()
        {
            CalculateInvoiceWeights(invoices, items);
        }

        private void CalculateInvoiceWeights(Dictionary<string, SuggestorCollection> invoices, Dictionary<string, SuggestorItem> items)
        {
            foreach (SuggestorCollection invoice in invoices.Values)
            {
                SuggestorCollectionFunctions.CalculateTFIDF(invoice, invoices.Count);
            }
        }

        private Dictionary<SuggestorCollection, double> FindClosestInvoices(SuggestorCollection compareInvoice, int n)
        {
            // TODO: Disgusting code
            Dictionary<SuggestorCollection, double> topInvoices = new Dictionary<SuggestorCollection, double>();
            List<SuggestorCollection> compareInvoices = invoices.Values.ToList();
            compareInvoices.Remove(compareInvoice);
            // Calculate all weights
            foreach (SuggestorCollection invoice in compareInvoices)
            {
                topInvoices.Add(invoice, SuggestorCollectionFunctions.CosineScore(invoice, compareInvoice));
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
    }
}
