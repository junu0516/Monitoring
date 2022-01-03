package kr.co.infotech.monitor.service.server;

import kr.co.infotech.monitor.dto.server.ServerDto;
import kr.co.infotech.monitor.dto.server.ServerInfoDto;
import kr.co.infotech.monitor.dto.server.ServerStatusDto;

import java.util.List;

public interface ServerInfoService {
    void insertServerInfo(ServerDto server) throws Exception;

    ServerDto selectServerInfo(String remoteAddr) throws Exception;

    void insertServerStatus(ServerStatusDto serverStatus) throws Exception;

    List<ServerInfoDto> selectAllUpdatedServerInfo() throws Exception;

    void updateLinusServerStatus() throws Exception;
}
