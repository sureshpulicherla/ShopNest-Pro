package com.example.demo.controller;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	ProductService productService;
	@Autowired
    UserService userService;
	
	@GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
		 User user = (User) session.getAttribute("loggedInUser");
		    if (user == null) {
		        return "redirect:/login"; // Redirect to login if user is not logged in
		    }

		    List<Product> cart = user.getCart();
		    model.addAttribute("cart", cart);

		    double totalPrice = calculateTotalPrice1(cart);
		    model.addAttribute("totalPrice", totalPrice);

		    return "cart";
        
    }
	private double calculateTotalPrice1(List<Product> cart) {
        double totalPrice = 0.0;
        if (cart != null) {
            for (Product product : cart) {
                totalPrice += product.getPrice();
            }
        }
        return totalPrice;
    }

    @GetMapping("/addToCart/{productId}")
    public String addToCart(@PathVariable("productId") int productId, HttpSession session) {
    	 User user = (User) session.getAttribute("loggedInUser");
         if (user == null) {
        	 return "redirect:/login?cart_login_required=true"; // Redirect to login if user is not logged in
         }

         Product product = productService.getProductById(productId);
         List<Product> cart = user.getCart();
         if (cart == null) {
             cart = new ArrayList<>();
         }
         cart.add(product);
         user.setCart(cart);
         userService.updateUser(user); // Update user in the database

         return "redirect:/customerPage";
    }

    @PostMapping("/deleteItem")
    public String deleteItemFromCart(@RequestParam("deleteProduct") int id, HttpSession session) {
    	 User user = (User) session.getAttribute("loggedInUser");
         if (user == null) {
             return "redirect:/login"; // Redirect to login if user is not logged in
         }

         List<Product> cart = user.getCart();
         if (cart != null) {
             cart.removeIf(product -> product.getId() == id);
             user.setCart(cart);
             userService.updateUser(user); // Update user in the database
         }
         return "redirect:/cart";
    }
    
    @GetMapping("/cart/totalPrice")
    @ResponseBody
    public double getTotalPrice(HttpSession session) {
    	 User user = (User) session.getAttribute("loggedInUser");
         if (user == null) {
             return 0.0;
         }

         List<Product> cart = user.getCart();
         return calculateTotalPrice1(cart) * 100;
     }

     

	

}
