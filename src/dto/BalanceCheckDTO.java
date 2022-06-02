package dto;

public class BalanceCheckDTO {

    private int balance;

    public BalanceCheckDTO(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "[잔금]=" + balance + "원";
    }
}
