package hello.jdbc.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
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
