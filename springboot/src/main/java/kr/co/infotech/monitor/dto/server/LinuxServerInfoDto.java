package kr.co.infotech.monitor.dto.server;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LinuxServerInfoDto {

    private String serverNo;
    private String username;
    private String password;
    private String port;
    private String ip;

}
