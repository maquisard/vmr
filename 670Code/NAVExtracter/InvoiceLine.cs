using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace NAVSuggestor
{
    [XmlRoot(ElementName = "Item")]
    public class InvoiceLine : SuggestorCollectionLine
    {
        private string id;
        private string description;
        private double quantity;
        private double weight;

        public InvoiceLine() { }

        public InvoiceLine(string itemNo, string description, double quantity)
        {
            this.id = itemNo;
            this.description = description;
            this.quantity = quantity;
        }

        public string Id
        {
            get
            {
                return id;
            }
            set
            {
                id = value;
            }
        }

        public string Description
        {
            get
            {
                return description;
            }
            set
            {
                description = value;
            }
        }

        public double Quantity
        {
            get
            {
                return quantity;
            }
            set
            {
                quantity = value;
            }
        }

        // TODO: Ignore this? At least for now we can bring it back later
        [XmlIgnore]
        public double Weight
        {
            get
            {
                return weight;
            }
            set
            {
                weight = value;
            }
        }
    }
}
