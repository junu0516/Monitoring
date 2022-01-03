package kr.co.infotech.monitor.service.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import kr.co.infotech.monitor.dao.CommonDao;
import kr.co.infotech.monitor.dto.server.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ServerInfoServiceImpl implements ServerInfoService {

    private CommonDao commonDao;

    @Autowired
    public ServerInfoServiceImpl(CommonDao commonDao){
        this.commonDao = commonDao;
    }
    
    //서버 정보 등록
    @Override
    public void insertServerInfo(ServerDto server) throws Exception {

        commonDao.ds01IR("insertServerInfo",server);
        for(int i=0;i<server.getServerDisk().size();i++){
            server.getServerDisk().get(i).setServerNo(server.getServerNo());
        }

        if(server.getLinuxUserName() != null && server.getLinuxUserPwd() != null){
            commonDao.ds01IR("insertLinuxServerInfo",server);
        }

        if(server.getServerDisk().size()>=1){
            commonDao.ds01IR("insertServerDiskInfo",server.getServerDisk());
        }
    }
    
    //서버 정보 조회
    @Override
    public ServerDto selectServerInfo(String remoteAddr) throws Exception {
        ServerDto server = (ServerDto) commonDao.ds01SR("selectServerInfo",remoteAddr);
        if(server != null){
            server.setServerDisk(new ArrayList<>());
            List<Object> serverDisks = commonDao.ds01SL("selectServerDiskInfo",server);
            for(Object obj : serverDisks){
                server.getServerDisk().add((ServerDiskDto)obj);
            }
        }
        return server;
    }
    
    //서버 상태 등록
    @Override
    public void insertServerStatus(ServerStatusDto serverStatus) throws Exception {

        commonDao.ds01IR("insertServerStatusInfo",serverStatus);
        if(serverStatus.getServerDiskStatusList().size()>=1){
            commonDao.ds01IR("insertServerDiskStatusInfo",serverStatus.getServerDiskStatusList());
        }
    }

    //서버 현황 조회
    @Override
    public List<ServerInfoDto> selectAllUpdatedServerInfo() throws Exception {
        List<ServerInfoDto> serverInfoList = new ArrayList<>();

        List<Object> serverList = commonDao.ds01SL("selectServerInfo",null);
        for(Object obj : serverList){
            ServerDto server = (ServerDto)obj;
            ServerInfoDto serverInfo = new ServerInfoDto();

            server.setServerDisk(new ArrayList<>());
            List<Object> serverDisks = commonDao.ds01SL("selectServerDiskInfo",server);
            for(Object serverDisk : serverDisks){
                server.getServerDisk().add((ServerDiskDto)serverDisk);
            }

            serverInfo.setServerNo(server.getServerNo());
            serverInfo.setServer(server);

            ServerStatusDto serverStatus = (ServerStatusDto) commonDao.ds01SR("selectUpdatedServerStatus",server);
            if(serverStatus != null){
                serverStatus.setServerDiskStatusList(new ArrayList<>());
                List<Object> serverDiskStatusList = commonDao.ds01SL("selectServerDiskStatusInfo",serverStatus);
                for(Object serverDiskStatus : serverDiskStatusList){
                    serverStatus.getServerDiskStatusList().add((ServerDiskStatusDto)serverDiskStatus);
                }

                serverInfo.setServerStatus(serverStatus);
            }

            serverInfoList.add(serverInfo);
        }


        return serverInfoList;
    }
    
    // ssh 접근으로 리눅스 서버 상태 업데이트
    @Override
    public void updateLinusServerStatus() throws Exception {
        //리눅스 ip 리스트 조회

        List<String> linuxServerList = commonDao.ds02SL("selectAllLinuxIpList","");
        System.out.println(linuxServerList);
        for(String serverNo : linuxServerList){
            LinuxServerInfoDto linuxServer = (LinuxServerInfoDto) commonDao.ds01SR("selectLinuxServerInfo",serverNo);
            String[] commands = new String[]{"top -b -n 1 | grep Cpu | awk '{print $5}' | tr -d \"%id,\" | awk '{print 100-$1\"%\"}'","free -m","df -h"};
            for(int i=0;i<commands.length;i++){
                printCommandOutput(commands[i],linuxServer);
                System.out.println("현재시간 : "+ LocalDateTime.now()+"\n");
            }
        }
    }

    private void printCommandOutput(String command, LinuxServerInfoDto linuxServer) throws JSchException, IOException {
        BufferedReader br;
        String username = linuxServer.getUsername();
        String password = linuxServer.getPassword();
        String port = linuxServer.getPort();
        String ip = linuxServer.getIp();

        System.out.println("ip 조회 : "+ip);
        JSch jsch = new JSch();
        Session session = jsch.getSession(username,ip,Integer.parseInt(port));
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking","no");
        session.connect();

        Channel channel = null;
        ChannelExec exec = null;
        if(session.isConnected()){
            channel = session.openChannel("exec");
            exec = (ChannelExec) channel;
        }

        if(exec != null){

            exec.setCommand(command);
            exec.connect();
            br = new BufferedReader(new InputStreamReader(exec.getInputStream(),"EUC-KR"));
            String str;
            while((str=br.readLine())!=null) {
                System.out.println(str);
            }
            br.close();
        }
        channel.disconnect();
        session.disconnect();
    }
}
