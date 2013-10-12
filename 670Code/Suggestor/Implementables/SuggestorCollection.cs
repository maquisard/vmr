using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorCollection
    {
        //string UserId { get; set; }
        string CollectionId { get; set; }
        Dictionary<string, SuggestorCollectionLine> CollectionLines { get; set; }
        //string GetUserId();
        //string GetCollectionId();
        //Dictionary<string, SuggestorCollectionLine> GetCollectionLines();

        //void SetUserId(string userId);
        //void SetCollectionId(string collectionId);
        //void SetCollectionLines(Dictionary<string, SuggestorCollectionLine> collectionLines);
    }
}
