using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace NAVSuggestor
{
    public class Item : SuggestorItem
    {
        private string id;
        private string description;
        private int quantity;

        public Item() { }

        public Item(string id, string description, int quantity)
        {
            this.id = id;
            this.description = description;
            this.quantity = quantity;
        }

        [XmlElement(ElementName = "Id")]
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

        [XmlElement(ElementName = "Description")]
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

        [XmlElement(ElementName = "Quantity")]
        public int Quantity
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
        /*
        public void SetId(string id)
        {
            this.id = id;
        }

        public void SetDescription(string description)
        {
            this.description = description;
        }

        public void SetQuantity(int quantity)
        {
            this.quantity = quantity;
        }*/

        public SuggestorItem DeepCopy()
        {
            return new Item(this.id, this.description, this.quantity);
        }
    }
}
