package com.example.quanlykhachsan.Service;

import java.util.List;

public class Model_ServiceResponse {
    private Data data;

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        private List<Model_Service> items;

        public List<Model_Service> getItems() { return items; }
        public void setItems(List<Model_Service> items) { this.items = items; }
    }
}
