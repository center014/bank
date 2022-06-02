package dao;

import dto.BalanceCheckDTO;
import dto.TransferDTO;
import model.Account;
import model.Member;
import util.ConnectionProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private static AccountDao accountDao = new AccountDao();

    private AccountDao() {

    }

    public static AccountDao getInstance() {
        return accountDao;
    }

    // 통장가입
    public int createAccount(Member member, Account account, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "insert into account values(account_seq.NEXTVAL,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getAccountNo());
            preparedStatement.setString(2, account.getAccountPassword());
            preparedStatement.setInt(3, account.getBalance());
            resultCnt = preparedStatement.executeUpdate();
            if (resultCnt > 0) {
                sql = "select * from account where accountno=?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, account.getAccountNo());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    account.setAccountId(resultSet.getLong("account_id"));
                }
                connectAccount(member, account, connection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultCnt;
    }

    // 계좌 연결
    public int connectAccount(Member member, Account account, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into member_account values(member_account_seq.NEXTVAL,?,?,sysdate)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, member.getMemberId());
            preparedStatement.setLong(2, account.getAccountId());
            resultCnt = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultCnt;
    }

    // 사용자 계좌 목록 조회
    public List<Account> selectAccountByMember(Member member, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Account> accountList = new ArrayList<>();
        try {
            String sql = "select a.account_id, a.accountno, a.accountpassword, a.balance from account a, member_account ma where a.account_id = ma.account_id and ma.member_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, member.getMemberId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setAccountId(resultSet.getLong("account_id"));
                account.setAccountNo(resultSet.getString("accountno"));
                account.setAccountPassword(resultSet.getString("accountpassword"));
                account.setBalance(resultSet.getInt("balance"));
                accountList.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return accountList;
    }

    // 계좌 시퀀스로 단일 계좌 조회
    public Account selectAccountByAccountId(int accountId, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account account = null;
        try {
            String sql = "select * from account where account_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, accountId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account = new Account();
                account.setAccountId(resultSet.getLong("account_id"));
                account.setAccountNo(resultSet.getString("accountno"));
                account.setAccountPassword(resultSet.getString("accountpassword"));
                account.setBalance(resultSet.getInt("balance"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return account;
    }

    // 입금
    public int deposit(int select, int money, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "update account set balance=? where account_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setLong(2, select);
            resultCnt = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultCnt;
    }

    // 출금
    public int withDraw(int select, int money, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "update account set balance=? where account_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setLong(2, select);
            resultCnt = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultCnt;
    }

    // 잔액 조회
    public List<BalanceCheckDTO> balanceCheck(int select, Connection connection) {
        Account account = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<BalanceCheckDTO> balanceCheckDTOList = new ArrayList<>();
        try {
            String sql = "select a.balance from account a where account_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, select);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                account = new Account();
//                account.setAccountId(resultSet.getLong("account_id"));
//                account.setAccountNo(resultSet.getString("accountno"));
//                account.setBalance(resultSet.getInt("balance"));
                BalanceCheckDTO response = new BalanceCheckDTO(resultSet.getInt("balance"));
                balanceCheckDTOList.add(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return balanceCheckDTOList;
    }

    // 내 계좌 확인.
    public boolean checkMyAccount(Long memberId, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from member_account where member_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, memberId);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //계좌번호 존재 유무

    public boolean checkAccountNoExist(String accountNo, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select * from account where accountno = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accountNo);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 내가 보유한 계좌 갯수
    public int checkMyAccountCount(Long memberId, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select count(*) from member_account where member_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, memberId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultCnt = resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(resultCnt);
        return resultCnt;
    }


    // 송금
    public int transferMoney(String accountNo, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String sql = "select m.membername, a.balance from account a, member m, member_account ma " +
                    "where a.account_id = ma.account_id and m.member_id = ma.member_id " +
                    "and a.accountno = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accountNo);
            resultSet = preparedStatement.executeQuery();
            TransferDTO transferDTO = new TransferDTO();
            if (resultSet.next()) {
                transferDTO.setMemberName(resultSet.getString("membername"));
                transferDTO.setBalance(resultSet.getInt("balance"));
            }
            System.out.println(transferDTO);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return resultCnt;
    }

}




























