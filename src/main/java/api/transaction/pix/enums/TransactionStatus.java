package api.transaction.pix.enums;

import api.transaction.pix.exception.IllegalStateException;

public enum TransactionStatus {
    CREATED,
    AUTHORIZED,
    PROCESSING,
    COMPLETED,
    FAILED,
    REVERSED,
    CANCELLED;

    public boolean canTransition(TransactionStatus next) {
        return switch (this){
            case CREATED -> next == AUTHORIZED || next ==CANCELLED;
            case AUTHORIZED -> next == PROCESSING || next ==CANCELLED || next==FAILED;
            case PROCESSING -> next==COMPLETED || next==FAILED;
            case COMPLETED -> next == REVERSED;
            case FAILED,REVERSED,CANCELLED -> false;
        };
    }
    public TransactionStatus transitionTo(TransactionStatus next) {
        if (!canTransition(next)){
            throw new IllegalStateException(
                    "Transition Invalid: %s -> %s".formatted(this, next)
            );
        }
        return next;
    }
}
