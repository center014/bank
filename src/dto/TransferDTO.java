package dto;

public class TransferDTO {


    // 안녕하세요

    private String memberName;
    private int balance;

    public TransferDTO() {

    }

    public TransferDTO(String memberName, int balance) {
        this.memberName = memberName;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "memberName='" + memberName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
