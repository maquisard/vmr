using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public abstract class SuggestorRecommender
    {
        public abstract void PreCalculation(Dictionary<string, SuggestorCollection> collections, Dictionary<string, SuggestorItem> items);
        public abstract Dictionary<string, double> SuggestNItems(Dictionary<string, SuggestorCollection> collections, SuggestorCollection compareCollection, int n);
        public abstract Dictionary<SuggestorCollection, double> SuggestNCollections(List<SuggestorCollection> collections, SuggestorCollection compareCollection, int n);
        public abstract Dictionary<SuggestorUser, double> SuggestNUsers(List<SuggestorUser> users, SuggestorUser user, int n);
    }
}
