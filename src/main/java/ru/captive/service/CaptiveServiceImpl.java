package ru.captive.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.captive.LinuxSystemCall;

import javax.servlet.http.HttpServletRequest;

@Service
public class CaptiveServiceImpl implements CaptiveService {

    @Value("${captive.unblock}")
    private Boolean unblockEnabled;

    private static final String INTERFACE = "wlan0";

    @Override
    public void allowConnection() {
        if (!unblockEnabled) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        final String ip = request.getRemoteAddr();
        final String macByIpQuery = "sudo /usr/sbin/arp -an " + ip + " | grep -o -E '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}'";
        final String macAddress = new LinuxSystemCall(macByIpQuery).call();
        final String ipTablesQuery = "sudo iptables -t nat -I PREROUTING -i " + INTERFACE +
                " -p tcp -m mac --mac-source " + macAddress + " -j ACCEPT";
        new LinuxSystemCall(ipTablesQuery).call();
        System.out.println("**** IP **** " + ip + " *******");
        System.out.println("**** MAC **** " + macAddress + " ********");
        System.out.println("**** Query **** " + ipTablesQuery + " ********");
    }
}
