package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {

    @GetMapping("/pay")
    public String pay() {
        return "pay";
    }

    @GetMapping("/createOrder")
    @ResponseBody
    public String createOrder(HttpSession session) {
        double amount = 100000; 
        Order order = null;
        try {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_MMd2bgsAvEvjuq", "NJZk8SudDdv40ZtWpczUJIPI");
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); 
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            order = razorpay.orders.create(orderRequest);
        } catch (RazorpayException e) {
            e.printStackTrace();
         
            return "{\"error\": \"Error creating order\"}";
        }
        return order.toString();
    }

    @PostMapping("/verify")
    @ResponseBody
    public boolean verifyPayment(@RequestParam String orderId, @RequestParam String paymentId,
            @RequestParam String signature) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_MMd2bgsAvEvjuq", "NJZk8SudDdv40ZtWpczUJIPI");
           
            String verificationData = orderId + "|" + paymentId;
          
            boolean isValidSignature = Utils.verifySignature(verificationData, signature,
                    "NJZk8SudDdv40ZtWpczUJIPI");
            return true;
        } catch (RazorpayException e) {
            e.printStackTrace();
            
            return false;
        }
    }
}
