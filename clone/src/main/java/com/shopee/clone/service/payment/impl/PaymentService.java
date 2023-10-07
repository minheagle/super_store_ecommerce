package com.shopee.clone.service.payment.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.payment.requestAPI.PaymentRequest;
import com.shopee.clone.DTO.payment.requestAPI.SignatureRequest;
import com.shopee.clone.DTO.payment.responseAPI.PaymentResponse;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.service.payment.IPaymentService;
import com.shopee.clone.util.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PaymentService implements IPaymentService {
    private final RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String baseURL="https://api-merchant.payos.vn/";
    private final String clientId="192d2ddf-733c-4509-b9af-bd1c7746b6de";
    private final String apiKey="2aa24408-0509-4afd-980d-3fd58a01aa38";
    private final String secretKey="d8b5113be36e2274a79604c96143f99f01488b4b51895316284caf5d5e1f65d8";
    private final String cancelUrl = "localhost:8080/api/v1/payment/cancel";
    private final String successUrl = "localhost:8080/api/v1/payment/success";

    @Override
    public ResponseEntity<?> getLinkPayment(Long userId, Integer orderNumber, Integer amountPayment) {
        try {
            if(userRepository.existsById(userId)){
                UserEntity user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

                String desc = "FThB: "+orderNumber;

                SignatureRequest signatureRequest = SignatureRequest
                        .builder()
                        .amount(amountPayment.toString())
                        .cancelUrl(cancelUrl)
                        .description("FthB: "+orderNumber)
                        .orderCode(orderNumber.toString())
                        .returnUrl(successUrl)
                        .secretKey(secretKey)
                        .build();
                String signature = this.createSignature(signatureRequest);

                PaymentRequest paymentRequest = PaymentRequest
                        .builder()
                        .orderCode(orderNumber)
                        .amount(amountPayment)
                        .description(desc)
                        .buyerName(user.getFullName())
                        .buyerEmail(user.getEmail())
                        .buyerPhone(user.getPhone())
                        .cancelUrl(cancelUrl)
                        .returnUrl(successUrl)
                        .signature(signature)
                        .build();

                // Swap Object To JSON
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequestBody;
                try{
                    jsonRequestBody = objectMapper.writeValueAsString(paymentRequest);
                }catch (JsonProcessingException e){
                    return ResponseEntity
                            .status(HttpStatusCode.valueOf(404))
                            .body(
                                    ResponseObject
                                            .builder()
                                            .status("FAIL")
                                            .message(e.getMessage())
                                            .results("Error Create JSON Request")
                                            .build()
                            );
                }

                //Set-up Header Of Request
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-client-id", clientId);
                headers.add("x-api-key", apiKey);

                //Create HttpEntity With JsonData and headers
                HttpEntity<String> request = new HttpEntity<>(jsonRequestBody, headers);

                ResponseEntity<PaymentResponse> responseEntity = restTemplate
                        .exchange(baseURL+"v2/payment-requests", HttpMethod.POST, request, PaymentResponse.class);

                PaymentResponse paymentData = responseEntity.getBody();
                ResponseData<PaymentResponse> paymentResponse = new ResponseData<>();
                paymentResponse.setData(paymentData);

                return ResponseEntity
                        .status(HttpStatusCode.valueOf(200))
                        .body(
                                ResponseObject
                                        .builder()
                                        .status("SUCCESS")
                                        .message("Get Link Payment Successfully")
                                        .results(paymentResponse)
                                        .build()
                        );
            }
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(404))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("FAIL")
                                    .message(e.getMessage())
                                    .results("")
                                    .build()
                    );
        }
        return null;
    }

    @Override
    public String createSignature(SignatureRequest signatureRequest) {
        String signature="";
        if(signatureRequest != null){

            Map<String, String> rawData = new TreeMap<>();
            rawData.put("amount", signatureRequest.getAmount());
            rawData.put("cancelUrl", signatureRequest.getCancelUrl());
            rawData.put("description", signatureRequest.getDescription());
            rawData.put("orderCode", signatureRequest.getOrderCode());
            rawData.put("returnUrl", signatureRequest.getReturnUrl());

            List<String> groupKeyValue = new ArrayList<>();
            for(Map.Entry<String, String> entry : rawData.entrySet()){
                if(!StringUtils.isEmpty(entry.getValue())){
                    groupKeyValue.add(entry.getKey()+"="+entry.getValue());
                }
            }
            String dataSorted = String.join("&", groupKeyValue);

            //Begin Create Signature By HMAC_SHA256
            String secretKey = signatureRequest.getSecretKey();
            try {

                Mac sha256HMac = Mac.getInstance("HmacSHA256");
                SecretKeySpec secretKeySpec = new
                        SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                sha256HMac.init(secretKeySpec);
                byte[] signatureByte = sha256HMac.doFinal(dataSorted.getBytes(StandardCharsets.UTF_8));
                signature = Base64.getEncoder().encodeToString(signatureByte);

            }catch (NoSuchAlgorithmException | InvalidKeyException e){
                e.printStackTrace();
            }
        }
        return signature;
    }

    @Override
    public ResponseEntity<?> getDataPaymentSuccess(Integer code,
                                                   String id,
                                                   Boolean cancel,
                                                   String status,
                                                   Integer orderCode) {
        if(code==00 && !cancel){
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("PAID")
                                    .results("")
                                    .build()
                    );
        }else{
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("CANCELLED")
                                    .results("")
                                    .build()
                    );
        }
    }
}
