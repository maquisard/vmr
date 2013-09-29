using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace MovieSuggestor
{
    [XmlType("Item")]
    public partial class User : SuggestorUser
    {
        public string AgeGroup
        {
            get
            {
                string ageKey = "";
                if (Age == null) return ageKey;
                double divisionFactor = double.Parse(Age.ToString()) / 5.0;
                int lower = (int)Math.Floor(divisionFactor) * 5;
                int higher = (int)Math.Ceiling(divisionFactor) * 5;
                if (lower == higher) higher += 5;
                ageKey = lower.ToString() + " - " + higher.ToString();
                return ageKey;
            }
        }

        string SuggestorUser.Id
        {
            get
            {
                return Id.ToString();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        [XmlIgnore]
        public Dictionary<string, SuggestorCollectionLine> CollectionLines
        {
            get
            {
                return Rating.ToDictionary(x => (x.Id), x => (SuggestorCollectionLine)x);

                //return Rating.Cast<SuggestorCollection>().ToList();
            }
            set
            {
                throw new NotImplementedException();
            }
        }
        // TODO: Ugly...we do not need this for the user
        [XmlIgnore]
        public List<Rating> Ratings
        {
            get
            {
                return Rating.ToList();
            }
            set
            {
                Rating = value;
            }
        }
    }
}
