package ru.robinhood.TelegramBotRobinHoodCapital.models.entities;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Deposit implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "owner_deposit", referencedColumnName = "id")
    private User owner;

    @Column(name = "status")
    private boolean status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "hash_transaction")
    private String hashTransaction;

    @Column(name = "amount")
    private Long amount;
}
