package kr.co.infotech.monitor.dto.auth;

import lombok.*;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    String memberNo;
    String memberId;
    String memberPwd;
    String memberRole;
    String modDate;
}
