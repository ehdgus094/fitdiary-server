package im.fitdiary.swaggeragent.logger;

public class Logger {

    private final Class<?> clazz;

    public Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void log(String message) {
        System.out.println("[SwaggerAgent] [" + clazz.getSimpleName() + "] - " + message);
    }

    public void error(String message) {
        System.out.println("[SwaggerAgent] [" + clazz.getSimpleName() + "] - " + message);
    }
}
