package ru.robinhood.TelegramBotRobinHoodCapital.models.trontransaction;


import lombok.Data;

import java.util.List;

@Data
public class Transactions {

    List<Transaction> transactions;
}
