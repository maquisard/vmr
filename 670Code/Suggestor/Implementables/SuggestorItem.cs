using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Suggestor
{
    public interface SuggestorItem
    {
        string Id { get; set; }
        string Description { get; set; }
        int Quantity { get; set; }
        SuggestorItem DeepCopy();
    }
}
