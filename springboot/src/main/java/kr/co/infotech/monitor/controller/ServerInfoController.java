package kr.co.infotech.monitor.controller;

import kr.co.infotech.monitor.dto.server.*;
import kr.co.infotech.monitor.service.server.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ServerInfoController {

    private ServerInfoService serverInfoService;

    @Autowired
    public ServerInfoController(ServerInfoService serverInfoService){
        this.serverInfoService = serverInfoService;
    }
    
    //윈도우 서버로부터 상태정보 수신
    @PostMapping(value="/api/v1/server/info")
    public String getRequestFromPython(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap serverInfo) throws Exception {

        System.out.println("서버 아이피 : " + request.getRemoteHost()+" / "+request.getRemotePort());
        System.out.println("servlet path : "+request.getServletPath());
        System.out.println(serverInfo);
        ServerDto server = serverInfoService.selectServerInfo(serverInfo.get("publicip").toString());

        ServerStatusDto serverStatus = new ServerStatusDto(serverInfo.get("system_uuid").toString(),
                                                            server.getServerNo(),
                                                            serverInfo.get("cpu_percent").toString(),
                                                            serverInfo.get("memory_percent").toString(),
                                                            null,
                                                            new ArrayList<ServerDiskStatusDto>(),
                                                            request.getRemoteAddr());

        Map<String,Map<String,String>> drives = (Map<String,Map<String,String>>)serverInfo.get("drives");
        for(String key : drives.keySet()){
            String driveName = key.split("\t")[0].substring(0,2);

            ServerDiskStatusDto serverDiskStatus = new ServerDiskStatusDto(null,
                                                                            getServerDiskNo(driveName,server),
                                                                            null,
                                                                            server.getServerNo(),
                                                                            serverInfo.get("system_uuid").toString(),
                                                                            drives.get(key).get("disk_percent"),
                                                            null,
                                                                            request.getRemoteAddr());

            serverStatus.getServerDiskStatusList().add(serverDiskStatus);
        }

        serverInfoService.insertServerStatus(serverStatus);
        System.out.println("상태 등록 완료\n---------------------------------");
        return "";
    }

    //서버 정보 등록
    @PostMapping(value="/server/info")
    public String insertServerInfo(@RequestBody ServerDto server){

        try{
            serverInfoService.insertServerInfo(server);
        }catch(Exception e){
            e.printStackTrace();
        }

        return "";
    }

    private String getServerDiskNo(String driveName, ServerDto server){
        for(int i=0;i<server.getServerDisk().size();i++){
            if(server.getServerDisk().get(i).getServerDiskName().equals(driveName)){
                return server.getServerDisk().get(i).getServerDiskNo();
            }
        }
        return null;
    }

    @GetMapping(value="/server/info")
    public List<ServerInfoDto> selectServerInfo() throws Exception{
        List<ServerInfoDto> serverInfoList = serverInfoService.selectAllUpdatedServerInfo();

        return serverInfoList;
    }
}
