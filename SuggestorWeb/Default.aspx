<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.master" AutoEventWireup="true"
    CodeBehind="Default.aspx.cs" Inherits="SuggestorWeb._Default" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <h2>
        Shop all the things!
    </h2>

    <table style="border:1px solid;width:100%">
        <thead>
            <tr style="width:auto">
                <td>All Items</td>
                <td>Basket</td>
                <td>Recommended Items</td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <!-- All Items -->
                <td>
                    <asp:GridView runat="server" ID="itemsGrid" AutoGenerateColumns="true">
                        <Columns>
                            <asp:TemplateField HeaderText="Add" ShowHeader="False">
                              <ItemTemplate>
                                <asp:LinkButton ID="btnRemoveItem" runat="server" CausesValidation="true" Text="Add" 
                                                CommandArgument='<%# Eval("Key") %>' OnCommand="AddItemToBasket" />
                              </ItemTemplate>
                            </asp:TemplateField>
                        </Columns>
                    </asp:GridView>
                </td>

                <!-- Basket Items -->
                <td>
                    <asp:GridView runat="server" ID="basketGrid" AutoGenerateColumns="true">
                        <Columns>
                            <asp:TemplateField HeaderText="Remove" ShowHeader="False">
                              <ItemTemplate>
                                <asp:LinkButton ID="btnRemoveItem" runat="server" CausesValidation="true" Text="Remove" 
                                                CommandArgument='<%# Eval("Key") %>' OnCommand="RemoveItemFromBasket" />
                              </ItemTemplate>
                            </asp:TemplateField>
                        </Columns>
                    </asp:GridView>
                </td>

                <!-- Recommended Items-->
                <td>
                    <asp:GridView runat="server" ID="recommendedItemsGrid" AutoGenerateColumns="true">
                        <Columns>
                            <asp:TemplateField HeaderText="Add" ShowHeader="False">
                              <ItemTemplate>
                                <asp:LinkButton ID="btnRemoveItem" runat="server" CausesValidation="true" Text="Add" 
                                                CommandArgument='<%# Eval("Key") %>' OnCommand="AddItemToBasket" />
                              </ItemTemplate>
                            </asp:TemplateField>
                        </Columns>
                    </asp:GridView>
                </td>
            </tr>
        </tbody>
    </table>
</asp:Content>
