package ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter;


import lombok.Data;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter.Transaction;

import java.util.List;

@Data
public class Transactions {

    List<Transaction> transactions;
}
