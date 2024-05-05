package ru.robinhood.TelegramBotRobinHoodCapital.models.entities;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Wallet implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "owner_chat_id")
    private Long ownerChatId;

    @OneToOne(mappedBy = "wallet")
    private User owner;

    @Column(name = "balance")
    private long balance;

    @Column(name = "orig_balance")
    private long origBalance;

    @Column(name = "number_wallet")
    private String numberWallet;


}
