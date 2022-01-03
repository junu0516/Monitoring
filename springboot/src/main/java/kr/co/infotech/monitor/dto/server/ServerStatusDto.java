package kr.co.infotech.monitor.dto.server;

import lombok.*;

import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerStatusDto {

    String serverStatusUuid;
    String serverNo;
    String serverStatusCpuPercentage;
    String serverStatusMemoryPercentage;
    String serverStatusTime;
    List<ServerDiskStatusDto> serverDiskStatusList;

    private String serverIp;
}
