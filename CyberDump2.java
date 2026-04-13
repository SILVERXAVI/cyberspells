import java.lang.reflect.*;

public class CyberDump2 {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.maxwell.cyber_ware_port.common.item.base.BodyPartType");
        System.out.println("Methods:");
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println(" - " + m.getName() + ": " + m.getReturnType() + " (" + m.getParameterCount() + " args)");
        }
    }
}
