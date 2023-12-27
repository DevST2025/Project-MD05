package com.cardealer.util;

import com.cardealer.model.common.EType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SerialGenerator {
    public static String generateSerial() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String uuidPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return sdf.format(new Date(System.currentTimeMillis())) + uuidPart;
    }
}
