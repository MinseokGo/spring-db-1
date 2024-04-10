package hello.jdbc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {

    private String memberId;
    private int money;

    private Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }

    public static Member valueOf(String memberId, int money) {
        return new Member(memberId, money);
    }
}
