package ru.robinhood.TelegramBotRobinHoodCapital.models.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topic")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Topic {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;


    @ManyToOne
    @JoinColumn(name = "owner_topic", referencedColumnName = "id")
    private User owner;

    @Column(name = "problem")
    private String problem;

    @Column(name = "status")
    private boolean status;

    @Column(name = "chat_id")
    private Long chatId;
}
