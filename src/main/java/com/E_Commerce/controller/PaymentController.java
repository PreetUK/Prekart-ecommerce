package com.E_Commerce.controller;

import com.E_Commerce.service.OrderService;
import com.E_Commerce.service.PaymentService;
import com.razorpay.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam int amount) {
        try {
            Order order = paymentService.createRazorpayOrder(amount);
            return ResponseEntity.ok(order.toJson()); // return proper JSON, not string
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String, String> payload) {
        try {
            boolean isVerified = paymentService.verifyPayment(
                    payload.get("razorpay_order_id"),
                    payload.get("razorpay_payment_id"),
                    payload.get("razorpay_signature")
            );

            if (isVerified) {
                // TODO: Pass actual cart total here
                orderService.saveOrder(payload.get("razorpay_payment_id"), payload.get("razorpay_order_id"));
                return ResponseEntity.ok("Payment success & order saved");
            } else {
                return ResponseEntity.badRequest().body("Payment verification failed");
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying payment: " + e.getMessage());
        }
    }
}
