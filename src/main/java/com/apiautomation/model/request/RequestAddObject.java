package com.apiautomation.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestAddObject {
    /*
    {
        "name": "Lenovo ThinkBook 7i",
        "data": {
            "year": "2023",
            "price": "1849.99",
            "cpu_model": "Intel Core i7",
            "hard_disk_size": "1 TB",
            "color": "silver",
            "capacity": 2,
            "screen_size": 14
        }
    }
     */

    @JsonProperty("name")
    public String name;

    @JsonProperty("data")
    public Data data;

    public static class Data {
        public Data(int i, double d, String string, String string2, String string3, String string4, String string5) {
            // Constructor to initialize all fields
            this.year = String.valueOf(i);
            this.price = String.valueOf(d);
            this.cpuModel = string;
            this.hardDiskSize = string2;
            this.color = string3;
            this.capacity = string4;
            this.screenSize = string5;
        }

        @JsonProperty("year")
        public String year;

        @JsonProperty("price")
        public String price;

        @JsonProperty("cpu_model")
        public String cpuModel;

        @JsonProperty("hard_disk_size")
        public String hardDiskSize;

        @JsonProperty("color")
        public String color;

        @JsonProperty("capacity")
        public String capacity;

        @JsonProperty("screen_size")
        public String screenSize;
    }

    public RequestAddObject() {}
    public RequestAddObject(String name, Data data) {
        this.name = name;
        this.data = data;
    }
}
