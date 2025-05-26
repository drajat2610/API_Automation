package com.apiautomation.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseSingelObject {
    /*
     * {
        "id": 773,
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
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("data")
    public Data data;

    public static class Data {
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
    public ResponseSingelObject() {
        // Default constructor
    }
}
