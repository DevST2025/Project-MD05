package com.cardealer.util;

import com.cardealer.model.common.EType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SKUGenerator {
    public static String generateSKU(String carName, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String name = carName.substring(0, 3).toUpperCase();
        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return sdf.format(new Date(System.currentTimeMillis())) +"-"+ name +"-"+ type.toUpperCase() +"-"+ uuidPart;
    }
}
