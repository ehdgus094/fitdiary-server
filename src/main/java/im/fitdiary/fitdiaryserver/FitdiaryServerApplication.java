package im.fitdiary.fitdiaryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@ConfigurationPropertiesScan
public class FitdiaryServerApplication {

	public static void main(String[] args) {
		// IPv6 -> IPv4 변환 (IntelliJ 빌드 시에는 적용 안됨)
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("java.net.preferIPv4Addresses", "true");

		SpringApplication.run(FitdiaryServerApplication.class, args);
	}

}
