package im.fitdiary.swaggeragent.logger;

import net.bytebuddy.description.type.TypeDescription;

public class Logger {

    private final Class<?> clazz;

    public Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void success(TypeDescription typeDescription) {
        System.out.println("[SwaggerAgent] [" + clazz.getSimpleName() + "] - " + typeDescription.getSimpleName() + " successfully modified");
    }

    public void error(String message) {
        System.out.println("[SwaggerAgent] [" + clazz.getSimpleName() + "] - " + message);
    }
}
