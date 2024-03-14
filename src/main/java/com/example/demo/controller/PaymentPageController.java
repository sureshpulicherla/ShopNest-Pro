package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Product;
import com.example.demo.entities.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentPageController {
	@Autowired
	ProductService productService;
	@Autowired
    UserService userService;
	
	@GetMapping("/paymentPage")
    public String viewCart(Model model, HttpSession session) {
		
		 User user = (User) session.getAttribute("loggedInUser");
		 List<Product> cart = user.getCart();
		    model.addAttribute("cart", cart);
		    double totalPrice = calculateTotalPrice1(cart);
		    model.addAttribute("totalPrice", totalPrice);

        return "paymentPage";
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
	
	@GetMapping("/paymentPage/totalPrice")
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
