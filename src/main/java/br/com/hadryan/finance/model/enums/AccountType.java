package br.com.hadryan.finance.model.enums;

import lombok.Getter;

@Getter
public enum AccountType {

    CHECKING("Checking"),
    SAVINGS("Savings"),
    INVESTMENT("Investment");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

}
