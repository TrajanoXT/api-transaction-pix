package api.transaction.pix.entity;

import api.transaction.pix.enums.PixKeyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PixKey {
    @Id
    @GeneratedValue
    private Long id;
    private String key;
    @Enumerated(EnumType.STRING)
    private PixKeyType type;
    @ManyToOne
    private User owner;
}
