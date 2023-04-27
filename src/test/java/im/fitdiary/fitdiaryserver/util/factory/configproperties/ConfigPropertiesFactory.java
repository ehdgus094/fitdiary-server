package im.fitdiary.fitdiaryserver.util.factory.configproperties;

import im.fitdiary.fitdiaryserver.config.ConfigProperties;
import im.fitdiary.fitdiaryserver.config.properties.JwtProfile;
import im.fitdiary.fitdiaryserver.config.properties.JwtProperties;
import im.fitdiary.fitdiaryserver.config.properties.Mode;
import im.fitdiary.fitdiaryserver.config.properties.TokenProperties;

public class ConfigPropertiesFactory {

    public static ConfigProperties create() {
        ConfigProperties properties = new ConfigProperties();
        properties.setMode(Mode.LOCAL);
        properties.setJwt(JwtProperties
                .builder()
                .user(JwtProfile
                        .builder()
                        .access(TokenProperties
                                .builder()
                                .secret("userAccessSecret")
                                .maxAge(100L)
                                .build()
                        )
                        .refresh(TokenProperties
                                .builder()
                                .secret("userRefreshSecret")
                                .maxAge(100L)
                                .build()
                        )
                        .build()
                )
                .build()
        );
        return properties;
    }
}
