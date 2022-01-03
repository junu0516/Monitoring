package kr.co.infotech.monitor.dto.server;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServerDiskDto {

    private String serverDiskNo;
    private String serverDiskCapacity;
    private String serverDiskName;
    private String serverNo;
}
