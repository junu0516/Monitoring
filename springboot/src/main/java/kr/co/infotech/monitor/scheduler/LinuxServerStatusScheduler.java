package kr.co.infotech.monitor.scheduler;

import com.jcraft.jsch.JSch;
import kr.co.infotech.monitor.service.server.ServerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinuxServerStatusScheduler {

    private ServerInfoService serverInfoService;

    @Autowired
    public LinuxServerStatusScheduler(ServerInfoService serverInfoService){
        this.serverInfoService = serverInfoService;
    }

    @Scheduled(fixedDelay = 1000 * 10)
    public void updateLinuxServerStatus() throws Exception {
        serverInfoService.updateLinusServerStatus();
    }

}
