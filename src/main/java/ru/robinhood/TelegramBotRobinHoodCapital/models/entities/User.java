package ru.robinhood.TelegramBotRobinHoodCapital.models.entities;


import jakarta.persistence.*;
import lombok.*;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.UserState;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @OneToOne
    private Wallet wallet;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "user_state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @OneToMany(mappedBy = "owner")
    private List<Deposit> deposits;

    @OneToMany(mappedBy = "owner")
    private List<Inference> inferences;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

}
