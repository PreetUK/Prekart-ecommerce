package com.E_Commerce.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class PaymentService {

    private final RazorpayClient client;
    private final String secretKey = "rzp_test_yourSecretHere"; // Replace with actual Razorpay secret

    public PaymentService() throws RazorpayException {
        this.client = new RazorpayClient("rzp_test_yourKeyHere", secretKey); // Replace with actual key & secret
    }

    public com.razorpay.Order createRazorpayOrder(int amount) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        return client.orders.create(orderRequest);
    }

    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            String payload = razorpayOrderId + "|" + razorpayPaymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hashBytes = mac.doFinal(payload.getBytes());

            // Convert bytes to HEX string (lowercase)
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String generatedSignature = hexString.toString();

            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
