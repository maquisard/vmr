using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorUser
    {
        string Id { get; set; }
        Dictionary<string, SuggestorCollectionLine> CollectionLines { get; set; }
    }
}
