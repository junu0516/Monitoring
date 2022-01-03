package kr.co.infotech.monitor.dto.server;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerDiskStatusDto {

    private String serverDiskStatusNo;
    private String serverDiskNo;
    private String serverDiskName;
    private String serverNo;
    private String serverStatusNo;
    private String serverDiskStatusPercentage;
    private String serverDiskStatusTime;

    private String serverIp;
}
