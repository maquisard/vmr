using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using NAVSuggestor;

namespace SuggestorWeb
{
    public partial class _Default : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            NAVSuggestor.NAVSuggestor suggestorInstance = NAVSuggestor.NAVSuggestor.GetInstance();
            //SuggestorConnector suggestorInstance = SuggestorConnector.GetInstance();

            // Items grid            
            Dictionary<string, Item> items = suggestorInstance.GetItems();
            itemsGrid.DataSource = items;
            itemsGrid.DataBind();

            // Basket
            Dictionary<string, Item> basketItems = suggestorInstance.GetCurrentItems();
            basketGrid.DataSource = basketItems;
            basketGrid.DataBind();

            // Recommended Items
            int noOfRecommenderItems = 5;
            Invoice basketInvoice = suggestorInstance.GetBasketInvoice();
            if (basketInvoice != null)
            {
                Dictionary<string, double> recommendedItems = suggestorInstance.GetRecommendedItemsScore(basketInvoice, noOfRecommenderItems);
                recommendedItemsGrid.DataSource = recommendedItems;
                recommendedItemsGrid.DataBind();
            }
        }

        protected void AddItemToBasket(object sender, System.Web.UI.WebControls.CommandEventArgs e)
        {
            string itemId = (string)e.CommandArgument;
            NAVSuggestor.NAVSuggestor.GetInstance().AddBasketItem(itemId, 1); // TODO: Quantity
            basketGrid.DataBind();
        }

        protected void RemoveItemFromBasket(object sender, System.Web.UI.WebControls.CommandEventArgs e)
        {
            string itemId = (string)e.CommandArgument;
            NAVSuggestor.NAVSuggestor.GetInstance().RemoveBasketItem(itemId, 1); // TODO: Quantity
            basketGrid.DataBind();
        }

        protected String SimpleFunctionToValidateGit()
        {
            return "Git from VS is working...Awesomeest";
        }
    }
}
