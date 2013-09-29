using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Xml;
using NAVSuggestor;

namespace NAVSuggestorWS
{
    /// <summary>
    /// Summary description for NAVSuggestor Web Service
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class Service1 : System.Web.Services.WebService
    {

        [WebMethod]
        public bool ResetBasket()
        {
            // TODO: Every web service user uses the same basket!
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            suggestorInstance.ClearBasket();
            return true;
        }

        [WebMethod]
        public bool AddBasketItem(string itemId, int quantity)
        {
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            suggestorInstance.AddBasketItem(itemId, quantity);
            return true;
        }

        [WebMethod]
        public bool RemoveBasketItem(string itemId, int quantity)
        {
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            suggestorInstance.RemoveBasketItem(itemId, quantity);
            return true;
        }

        [WebMethod]
        public XmlDocument GetBasket()
        {
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            Invoice basketInvoice = suggestorInstance.GetBasketInvoice();
            if (basketInvoice.InvoiceLines.Count == 0) return GetError("Basket invoice is empty");
            return InvoiceSerializer.ObjectToXml(basketInvoice, typeof(Invoice));
        }

        /// <summary>
        /// Get suggested items from Suggestor engine. 
        /// </summary>
        /// <param name="invoiceXml">Xml of invoice containing items</param>
        /// <param name="n">Number of suggested items to return</param>
        /// <returns>Xml containing items recommended</returns>
        [WebMethod]
        public XmlDocument GetRecommendedItems(int n)
        {
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            Invoice basketInvoice = suggestorInstance.GetBasketInvoice();
            List<Item> recommendedItems = suggestorInstance.GetRecommendedItems(basketInvoice, n);
            XmlDocument itemsDoc = InvoiceSerializer.ObjectToXml(recommendedItems, typeof(List<Item>));
            return itemsDoc;
        }

        /// <summary>
        /// This is most useful for the initialization of the UI. Since the user needs to view some items before he adds them to the cart.
        /// </summary>
        /// <param name="n">Number of items to be retrieve</param>
        /// <returns></returns>
        [WebMethod]
        public XmlDocument GetRandomItems(int n)
        {
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            List<Item> randomItems = suggestorInstance.GetRandomItems(n);
            XmlDocument itemsDoc = InvoiceSerializer.ObjectToXml(randomItems, typeof(List<Item>));
            return itemsDoc;
        }


        #region Helper Functions

        private XmlDocument GetError(string errorMessage)
        {
            XmlDocument errorDocument = new XmlDocument();
            errorDocument.LoadXml("<Error>" + errorMessage + "</Error>");
            return errorDocument;
        }

        #endregion
    }
}