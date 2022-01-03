package kr.co.infotech.monitor.dto.server;

import lombok.*;

import java.util.List;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServerDto {

    private String serverNo;
    private String serverName;
    private String serverCompany;
    private String serverPublicIp;
    private String serverPrivateIp;
    private String serverGubun;
    private String serverCpu;
    private String serverOs;
    private String serverMemory;
    private String serverCost;
    private String serverEtc;
    private List<ServerDiskDto> serverDisk;

    private String linuxUserName;
    private String linuxUserPwd;

}
