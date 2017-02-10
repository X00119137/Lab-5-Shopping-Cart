package controllers;

import models.shopping.ShopOrder;
import models.users.Customer;
import models.users.User;
import play.db.ebean.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

// Import models
// Import security controllers



public class ShoppingCtrl extends Controller {
    

@Security.Authenticated(Secured.class)

@With(CheckIfCustomer.class)



    // Get a user - if logged in email will be set in the session
	private Customer getCurrentUser() {
		return (Customer)User.getLoggedIn(session().get("email"));
	}


    @Transactional
     public Result showBasket() {
     return pk(basket.render(getCurrentUser()));

  }

    @Transactional
        public Result addToBasket(Long id)        {

  //find the product

 Product p = Product.find.byId(id);

//get basket for logged in customer

Customer customer =(Customer)User.getLoggedIn(session().get("email"));

 //check ig item in basket
if(customer.getBasket()==null) {

    //if no basket create a new one

   customer.setBasket(new Basket());
   customer.getBasket().setCustomer(customer);
   customer.update();

}
 //add product to the basket and save

  customer.getBasket().addProduct(p);
   customer.update();

   return ok(basket.render(customer));

}
  
    



    // Empty Basket
    @Transactional
    public Result emptyBasket() {
        
        Customer c = getCurrentUser();
        c.getBasket().removeAllItems();
        c.getBasket().update();
        
        return ok(basket.render(c));
    }


    
    // View an individual order
    @Transactional
    public Result viewOrder(long id) {
        ShopOrder order = ShopOrder.find.byId(id);
        return ok(orderConfirmed.render(getCurrentUser(), order));
    }

@Transactional
 
public Result addOne(Long item.find.byId(itemId);

//get the order item
OrderItem item = OrderItem.find.byId(itemId);
 //increase qty

 item.increaseQty();
//save
item.update();
//show updated basket
 return redirect(routes.ShoppingCtrl.showBasket());

}


@Transactional
public Result removeOne(Long itemId) {

 //get order item
 OrderItem item = OrderItem.find.byId(itemId);
//get user
 Customer c = getCurrentUser();
//call basket remove item method
c.getBasket().removeItem(item);
c.getBasket().update();
//back to basket
 return ok(basket.render(c));

}

@Transactional

public Result placeOrder() {

 Customer c =getCurrentUser();

    shopOrder order = new ShopOrder();

 order.setCustomer(c);

order.setItems(c.getBasket().getBasketItems());

order.save();

for(OrderItem i; order.getItems()) {

 i.setOrder(order);

 i.setBasket(null);

 i.update();

 order.update();

  c.getBasket().setBasketItems(null);
  c.getBasket().update();

 return ok(orderConfirmed.render(c, order));

}



}
