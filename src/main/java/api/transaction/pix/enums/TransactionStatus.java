package api.transaction.pix.enuns;

public enum TransactionStatus {
    CREATED,
    AUTHORIZED,
    PROCESSING,
    COMPLETED,
    FAILED,
    REVERSED,
    CANCELLED
}
