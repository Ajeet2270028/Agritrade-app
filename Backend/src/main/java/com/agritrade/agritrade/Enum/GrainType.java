package com.agritrade.agritrade.Enum;

public enum GrainType {

    PADDY("Paddy", "धान"),
    WHEAT("Wheat", "गेहूं"),
    MAIZE("Maize", "मक्का"),
    RICE("Rice", "चावल"),
    RAGI("Ragi", "रागी"),
    BAJRA("Bajra", "बाजरा"),
    JOWAR("Jowar", "ज्वार"),
    BARLEY("Barley", "जौ"),
    OATS("Oats", "जई"),
    SOYBEAN("Soybean", "सोयाबीन"),
    GROUNDNUT("Groundnut", "मूंगफली"),
    CHICKPEA("Chickpea", "चना"),
    GREEN_GRAM("Green Gram", "मूंग"),
    BLACK_GRAM("Black Gram", "उड़द"),
    RED_GRAM("Red Gram", "अरहर");

    private final String englishName;
    private final String hindiName;

    GrainType(String englishName, String hindiName) {
        this.englishName = englishName;
        this.hindiName = hindiName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getHindiName() {
        return hindiName;
    }
}
