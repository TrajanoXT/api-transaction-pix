package api.transaction.pix.entity;

import api.transaction.pix.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pix_transactions",indexes = {
        @Index(name = "idx_pix_sender",columnList = "sender_id"),
        @Index(name = "idx_pix_receiver", columnList = "receiver_id"),
        @Index(name = "idx_pix_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PixTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private PixKey receiverKey;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime authorizedAt;
    private LocalDateTime processingAt;
    private LocalDateTime completedAt;
    private LocalDateTime failedAt;
    private LocalDateTime reversedAt;
    private String failReason;
}
