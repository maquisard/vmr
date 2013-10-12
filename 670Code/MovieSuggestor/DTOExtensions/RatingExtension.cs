using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace MovieSuggestor
{
    public partial class Rating : SuggestorCollectionLine
    {

        public string Id
        {
            get
            {
                // TODO: Not unique but enough?
                return MovieId.ToString();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string Description
        {
            get
            {
                return Rating1.ToString() + " stars for " + MovieExtracter.GetInstance().GetMovie(MovieId).Title;
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public double Quantity
        {
            get
            {
                return Rating1;
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public double Weight
        {
            get
            {
                return Rating1;
            }
            set
            {
                Rating1 = (int)value;
            }
        }
    }
}
