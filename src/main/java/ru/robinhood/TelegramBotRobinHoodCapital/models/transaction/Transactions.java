package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction;


import lombok.Data;

import java.util.List;

@Data
public class Transactions {

    List<Transaction> transactions;
}
