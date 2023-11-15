package com.shopee.clone.service.payment.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopee.clone.DTO.ResponseData;
import com.shopee.clone.DTO.payment.requestAPI.PaymentRequest;
import com.shopee.clone.DTO.payment.requestAPI.SignatureRequest;
import com.shopee.clone.DTO.payment.requestServe.InforReturnStatusPayment;
import com.shopee.clone.DTO.payment.requestServe.PaymentServiceRequest;
import com.shopee.clone.DTO.payment.responseAPI.PaymentResponse;
import com.shopee.clone.entity.UserEntity;
import com.shopee.clone.entity.payment.PaymentEntity;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.repository.payment.PaymentRepository;
import com.shopee.clone.service.order.OrderService;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService implements IPaymentService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
   private final PaymentRepository paymentRepository;
   private final OrderService orderService;

    public PaymentService(RestTemplate restTemplate, UserRepository userRepository, PaymentRepository paymentRepository, OrderService orderService) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;

        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    private final String baseURL="https://api-merchant.payos.vn/";
    private final String clientId="192d2ddf-733c-4509-b9af-bd1c7746b6de";
    private final String apiKey="2aa24408-0509-4afd-980d-3fd58a01aa38";
    private final String secretKey="d8b5113be36e2274a79604c96143f99f01488b4b51895316284caf5d5e1f65d8";
    private final String cancelUrl = "https://super-store-hmh.vercel.app/customer/purchase/pending";
    private final String successUrl = "https://super-store-hmh.vercel.app/customer/purchase/transferred";

    @Override
    public ResponseEntity<?> getLinkPayment(PaymentServiceRequest paymentServiceRequest) {
        try {
            if(userRepository.existsById(paymentServiceRequest.getUserId())){
                UserEntity user = userRepository.findById(paymentServiceRequest.getUserId()).orElseThrow(NoSuchElementException::new);

                String desc = "FThB: "+paymentServiceRequest.getOrderNumber();

                SignatureRequest signatureRequest = SignatureRequest
                        .builder()
                        .amount(paymentServiceRequest.getAmountPayment().toString())
                        .cancelUrl(cancelUrl)
                        .description(desc)
                        .orderCode(paymentServiceRequest.getOrderNumber().toString())
                        .returnUrl(successUrl)
                        .secretKey(secretKey)
                        .build();
                String signature = this.createSignature(signatureRequest);

                PaymentRequest paymentRequest = PaymentRequest
                        .builder()
                        .orderCode(paymentServiceRequest.getOrderNumber())
                        .amount(paymentServiceRequest.getAmountPayment())
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
                //Save data
                if(paymentData != null && paymentData.getCode().equals("00")){
                    PaymentEntity paymentEntity = PaymentEntity
                            .builder()
                            .userId(paymentServiceRequest.getUserId())
                            .orderNumber(paymentRequest.getOrderCode())
                            .paymentAmount(paymentRequest.getAmount())
                            .desc_payment(paymentRequest.getDescription())
                            .paymentDate(LocalDateTime.now())
                            .paymentStatus(Boolean.FALSE)
                            .build();
                    paymentRepository.save(paymentEntity);
                }
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
                byte[] signatureBytes = sha256HMac.doFinal(dataSorted.getBytes(StandardCharsets.UTF_8));
//                signature = Base64.getEncoder().encodeToString(signatureByte);

                // Chuyển đổi chữ ký sang dạng hex
                StringBuilder signature = new StringBuilder();
                for (byte b : signatureBytes) {
                    signature.append(String.format("%02x", b));
                }
                return signature.toString();

            }catch (NoSuchAlgorithmException | InvalidKeyException e){
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public ResponseEntity<?> changePaymentStatus(InforReturnStatusPayment informationStatusPayment) {

        PaymentEntity paymentEntity = paymentRepository.findByOrderNumber(informationStatusPayment.getOrderCode());
        ResponseData<PaymentEntity> responsePayment = new ResponseData<>();

        if(informationStatusPayment.getCode() == 0 && !informationStatusPayment.getCancel()){

            paymentEntity.setPaymentStatus(Boolean.TRUE);
            paymentRepository.save(paymentEntity);
            orderService.changeStatusWhenCallPayment(informationStatusPayment.getOrderCode(), Boolean.TRUE);


            responsePayment.setData(paymentEntity);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("PAID")
                                    .results(responsePayment)
                                    .build()
                    );
        }else{
            orderService.changeStatusWhenCallPayment(informationStatusPayment.getOrderCode(), Boolean.FALSE);
            responsePayment.setData(paymentEntity);
            return ResponseEntity
                    .status(HttpStatusCode.valueOf(200))
                    .body(
                            ResponseObject
                                    .builder()
                                    .status("SUCCESS")
                                    .message("CANCELLED")
                                    .results(responsePayment)
                                    .build()
                    );
        }
    }
}
