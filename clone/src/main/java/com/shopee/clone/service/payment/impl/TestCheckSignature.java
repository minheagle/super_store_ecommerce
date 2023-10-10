package com.shopee.clone.service.payment.impl;

import java.util.*;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class TestCheckSignature {
    String checksumKey = "d8b5113be36e2274a79604c96143f99f01488b4b51895316284caf5d5e1f65d8";

    String transaction = "{'orderCode':123,'amount':2000,'description':'FThB: 123','buyerName':'Nguyen Huu Hoang Bao','buyerEmail':'killua7100@gmail.com','buyerPhone':'0706240008','cancelUrl':'localhost:8080/api/v1/payment/cancel','returnUrl':'localhost:8080/api/v1/payment/success','signature':'zPXp6Y2l67vBtAWpE8MCtlxupvdELYI2MZcICwZYz8w='}";

    String transactionSignature = "zPXp6Y2l67vBtAWpE8MCtlxupvdELYI2MZcICwZYz8w=";

    public Boolean isValidData(String transaction, String transactionSignature) throws JSONException {
        JSONObject jsonObject = new JSONObject(transaction);
        Iterator<String> sortedIt = sortedIterator(jsonObject.keys(), (a, b) -> a.compareTo(b));

        StringBuilder transactionStr = new StringBuilder();
        while (sortedIt.hasNext()) {
            String key = sortedIt.next();
            String value = jsonObject.get(key).toString();
            transactionStr.append(key);
            transactionStr.append('=');
            transactionStr.append(value);
            if (sortedIt.hasNext()) {
                transactionStr.append('&');
            }
        }

        String signature = new HmacUtils("HmacSHA256", checksumKey).hmacHex(transactionStr.toString());
        return signature.equals(transactionSignature);
    }

    public static Iterator<String> sortedIterator(Iterator<String> it, Comparator<String> comparator) {
        List<String> list = new ArrayList<String>();
        while (it.hasNext()) {
            list.add(it.next());
        }

        Collections.sort(list, comparator);
        return list.iterator();
    }
}
