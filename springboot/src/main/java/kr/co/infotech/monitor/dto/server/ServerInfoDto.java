package kr.co.infotech.monitor.dto.server;

import lombok.*;

import javax.xml.ws.ServiceMode;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerInfoDto {
    private String serverNo;
    private ServerDto server;
    private ServerStatusDto serverStatus;
}
