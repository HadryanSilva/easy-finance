package br.com.hadryan.finance.model.enums;

import lombok.Getter;

@Getter
public enum Currency {

    BRL("BRL", "Real"),
    USD("USD", "Dollar"),
    EUR("EUR", "Euro"),
    GBP("GBP", "Pound Sterling"),
    JPY("JPY", "Yen"),
    AUD("AUD", "Australian Dollar"),
    CAD("CAD", "Canadian Dollar"),
    CHF("CHF", "Swiss Franc"),
    CNY("CNY", "Yuan"),
    SEK("SEK", "Swedish Krona"),
    NZD("NZD", "New Zealand Dollar"),
    KRW("KRW", "Won"),
    SGD("SGD", "Singapore Dollar"),
    NOK("NOK", "Norwegian Krone"),
    MXN("MXN", "Mexican Peso"),
    INR("INR", "Indian Rupee"),
    RUB("RUB", "Russian Ruble"),
    ZAR("ZAR", "Rand"),
    HKD("HKD", "Hong Kong Dollar"),
    TRY("TRY", "Turkish Lira"),
    IDR("IDR", "Rupiah"),
    SAR("SAR", "Saudi Riyal"),
    AED("AED", "UAE Dirham"),
    ARS("ARS", "Argentine Peso"),
    THB("THB", "Baht"),
    COP("COP", "Colombian Peso"),
    DKK("DKK", "Danish Krone"),
    MYR("MYR", "Ringgit"),
    CLP("CLP", "Chilean Peso"),
    PHP("PHP", "Philippine Peso"),
    CZK("CZK", "Czech Koruna"),
    ILS("ILS", "Shekel"),
    PLN("PLN", "Zloty"),
    EGP("EGP", "Egyptian Pound"),
    HUF("HUF", "Forint"),
    QAR("QAR", "Qatari Rial"),
    VND("VND", "Dong"),
    BHD("BHD", "Bahraini Dinar"),
    KWD("KWD", "Kuwaiti Dinar"),
    OMR("OMR", "Rial Omani"),
    RON("RON", "Romanian Leu");

    private final String code;
    private final String description;

    Currency(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
