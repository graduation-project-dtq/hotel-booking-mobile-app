package com.example.quanlykhachsan.KhuyenMai;

public class Model_KhuyenMai{
        private String id;
        private String name;
        private String description;
        private int discountAmount;
        private String startDate;
        private String endDate;
        private int quantity;
        private boolean isActive;
        private String customerId;
        private String code;
        private String lastUpdatedTime;


        // Constructor
        public Model_KhuyenMai(String id, String name, String description, int discountAmount,
                              String startDate, String endDate, int quantity, boolean isActive,
                              String customerId, String code) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.discountAmount = discountAmount;
            this.startDate = startDate;
            this.endDate = endDate;
            this.quantity = quantity;
            this.isActive = isActive;
            this.customerId = customerId;
            this.code = code;
        }

        // Getters và Setters

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public int getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(int discountAmount) { this.discountAmount = discountAmount; }

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public boolean isActive() { return isActive; }
        public void setActive(boolean active) { isActive = active; }

        public String getCustomerId() { return customerId; }
        public void setCustomerId(String customerId) { this.customerId = customerId; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
}
