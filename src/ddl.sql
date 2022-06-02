select m.membername, a.balance from account a, member m, member_account ma
where a.account_id = ma.account_id and m.member_id = ma.member_id
and a.accountno = '0896cdbd-d7e6-4593-b2f4-872261b626d3';