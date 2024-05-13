package ru.robinhood.TelegramBotRobinHoodCapital.models.xls;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DataUserModel {
    private String id;
    private String username;
    private String name;
    private String balance;
    private String origBalance;
    private String addressWallet;
    private String countInvited;
    private String refCode;
    private String role;
}
