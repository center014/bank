package dto;

public class BalanceCheckDTO {

    private int balance;

    public BalanceCheckDTO(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "[μκΈ]=" + balance + "μ";
    }
}
