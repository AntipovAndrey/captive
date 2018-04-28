package ru.captive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class CaptiveServiceImpl implements CaptiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptiveServiceImpl.class);

    @Value("${captive.unblock}")
    private Boolean unblockEnabled;

    @Autowired
    private SystemCall systemCall;

    private static final String INTERFACE = "wlan0";

    @Override
    public void allowConnection() {
        if (!unblockEnabled) {
            return;
        }
        LOGGER.info("Start unblocking");
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        final String ip = request.getRemoteAddr();
        LOGGER.info("IP " + ip);
        final String macByIpQuery = "sudo /usr/sbin/arp -an " + ip +
                " | grep -o -E '([[:xdigit:]]{1,2}:){5}[[:xdigit:]]{1,2}'";
        final String macAddress = systemCall.call(macByIpQuery);
        LOGGER.info("MAC " + macAddress);
        final String ipTablesQuery = "sudo iptables -t nat -I PREROUTING -i " + INTERFACE +
                " -p tcp -m mac --mac-source " + macAddress + " -j ACCEPT";
        systemCall.call(ipTablesQuery);
        LOGGER.info("Query " + ipTablesQuery);
    }
}
