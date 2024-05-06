package ru.robinhood.TelegramBotRobinHoodCapital.models.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "amount")
    private long amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private boolean status;

    @Column(name = "wallet_address")
    private String walletAddress;

    @Column(name = "hash_transaction")
    private String hashTransaction;

    @ManyToOne
    @JoinColumn(name = "owner_inference", referencedColumnName = "id")
    private User owner;
}
