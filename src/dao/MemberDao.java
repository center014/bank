package dao;

import model.Member;
import util.ConnectionProvider;

import java.sql.*;
import java.time.LocalDateTime;

public class MemberDao {

    private static MemberDao memberDao = new MemberDao();

    private MemberDao() {

    }

    public static MemberDao getInstance() {
        return memberDao;
    }

    // 고객 회원가입
    public int joinedMember(Member member, Connection connection) {
        int resultCnt = 0;
        PreparedStatement preparedStatement = null;
        try {
            String sql = "insert into member values(member_seq.NEXTVAL,?,?,?,sysdate,sysdate)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getMemberName());
            preparedStatement.setString(2, member.getEmail());
            preparedStatement.setString(3, member.getMemberPassword());
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

    // 계정 유무 확인
    public Member existByMemberEmail(String email, Connection connection) {
        PreparedStatement preparedStatement = null;
        try {
            String sql = "select * from member where email=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Member member = new Member();
                member.setMemberId(resultSet.getLong("member_id"));
                member.setMemberName(resultSet.getString("membername"));
                member.setEmail(resultSet.getString("email"));
                member.setMemberPassword(resultSet.getString("memberpassword"));
                member.setJoinAt(resultSet.getDate("joinat"));
                member.setRecentlyLoginedAt(resultSet.getDate("recentlyloginedat"));
                return member;
            } else {
                return null;
            }
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
        return null;
    }

    // 최근 로그인 시간
    public int updateRecentlyLogin(Member member, Connection connection) {
        int resultCnt = 0;
        try {
            String sql = "update member set recentlyloginedat=sysdate where email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, member.getEmail());
            resultCnt = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultCnt;
    }
}

















