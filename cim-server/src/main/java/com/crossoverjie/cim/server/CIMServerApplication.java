package com.crossoverjie.cim.server;

import com.crossoverjie.cim.server.config.AppConfiguration;
import com.crossoverjie.cim.server.kit.RegistryZK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

/**
 * @author crossoverJie
 */
@SpringBootApplication
public class CIMServerApplication implements CommandLineRunner{

	private final static Logger LOGGER = LoggerFactory.getLogger(CIMServerApplication.class);

	@Autowired
	private AppConfiguration appConfiguration ;

	@Value("${server.port}")
	private int httpPort ;

	public static void main(String[] args) {
        SpringApplication.run(CIMServerApplication.class, args);
		LOGGER.info("启动 Server 成功");
	}

	@Override
	public void run(String... args) throws Exception {
		// 优先获取配置的外网ip
		String addr = appConfiguration.getCimServerAddr();
		if (addr == null) {
			LOGGER.info("查询本地ip");
			addr = InetAddress.getLocalHost().getHostAddress();
		}

		Thread thread = new Thread(new RegistryZK(addr, appConfiguration.getCimServerPort(),httpPort));
		thread.setName("registry-zk");
		thread.start() ;
	}
}