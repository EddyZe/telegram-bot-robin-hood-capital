package ru.robinhood.TelegramBotRobinHoodCapital.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name = "edit_number_wallet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EditNumberWallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "new_number_wallet")
    private String newNumberWallet;

    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "current_wallet", referencedColumnName = "id")
    private Wallet currentWallet;

    @Column(name = "status")
    private boolean status;
}
