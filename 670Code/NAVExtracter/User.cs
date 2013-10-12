using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace NAVSuggestor
{
    public class User : SuggestorUser
    {
        private string id;
        private string name;
        private List<Invoice> purchaseInvoices;

        public User() { }

        public User(string id, string name)
        {
            this.id = id;
            this.name = name;
            this.purchaseInvoices = new List<Invoice>();
        }

        public User(string id, string name, List<Invoice> purchaseInvoices)
        {
            this.id = id;
            this.name = name;
            this.purchaseInvoices = purchaseInvoices;
        }

        #region Getters/Setters

        public List<Invoice> PurchaseInvoices
        {
            get { return purchaseInvoices; }
            set { purchaseInvoices = value; }
        }

        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        public string CustomerId
        {
            get { return id; }
            set { id = value; }
        }       
  
        #endregion

        #region Suggestor Implementations

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

        /*
        public Dictionary<string, SuggestorCollection> Collections
        {
            get
            {
                return PurchaseInvoices.ToDictionary(x => (x.Key), x => (SuggestorCollection)x.Value);
            }
            set
            {
                PurchaseInvoices = value.ToDictionary(x => (x.Key), x => (Invoice)x.Value); ;
            }
        }*/

        #endregion        
    

        public Dictionary<string, SuggestorCollectionLine> CollectionLines
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }
    }
}
