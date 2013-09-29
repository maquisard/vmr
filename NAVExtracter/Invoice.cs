using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace NAVSuggestor
{
    
    public class Invoice : SuggestorCollection
    {
        private string invoiceNo;
        //private Dictionary<string, SuggestorCollectionLine> invoiceLines;
        private Dictionary<string, InvoiceLine> invoiceLines;
        private string customerId;

        public Invoice() { }

        public Invoice(string invoiceNo, string userId)
        {
            this.invoiceNo = invoiceNo;
            this.customerId = userId;
            this.invoiceLines = new Dictionary<string, InvoiceLine>();
        }

        public Invoice(string invoiceNo, string userId, Dictionary<string, InvoiceLine> invoiceLines)
        {
            this.invoiceNo = invoiceNo;
            this.customerId = userId;
            this.invoiceLines = invoiceLines;
        }

        public string UserId
        {
            get
            {
                return customerId;
            }
            set
            {
                customerId = value;
            }
        }

        public string CollectionId
        {
            get
            {
                return invoiceNo;
            }
            set
            {
                invoiceNo = value;
            }
        }

        [XmlIgnore] // Cannot serialize Dictionary
        public Dictionary<string, SuggestorCollectionLine> CollectionLines
        {
            get
            {
                return invoiceLines.ToDictionary(x => (x.Key), x => (SuggestorCollectionLine)x.Value);

                //return invoiceLines;
            }
            set
            {
                throw new Exception("Should not be here");
                //invoiceLines = value;
            }
        }

        [XmlIgnore]
        public Dictionary<string, InvoiceLine> InvoiceLines
        {
            get
            {
                return invoiceLines;
            }
            set
            {
                invoiceLines = value;
            }
        }

        // For serialization. Ugly!
        [XmlElement(ElementName = "Item")]
        public List<InvoiceLine> Lines
        {
            get
            {
                return invoiceLines.Values.ToList();
            }
        }

        /*
        [XmlElement(ElementName = "UserId")]
        public string GetUserId()
        {
            return customerId;
        }

        [XmlElement(ElementName = "CollectionId")]
        public string GetCollectionId()
        {
            return invoiceNo;
        }

        [XmlArrayAttribute("Lines")]
        public Dictionary<string, SuggestorCollectionLine> GetCollectionLines()
        {
            return invoiceLines;
        }

        public void SetUserId(string userId)
        {
            this.customerId = userId;
        }

        public void SetCollectionId(string invoiceNo)
        {
            this.invoiceNo = invoiceNo;
        }

        public void SetCollectionLines(Dictionary<string, SuggestorCollectionLine> invoiceLines)
        {
            this.invoiceLines = invoiceLines;
        }           */        
    }
}