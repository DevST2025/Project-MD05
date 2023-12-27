package com.cardealer;

import com.cardealer.model.common.EType;
import com.cardealer.util.SKUGenerator;
import com.cardealer.util.SerialGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        System.out.println(SKUGenerator.generateSKU("Veloz", "suv"));
        System.out.println(SerialGenerator.generateSerial());
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
}
