import java.lang.reflect.*;

public class CyberDump3 {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.maxwell.cyber_ware_port.common.menu.CyberwareMenu");
        System.out.println("CyberwareMenu Fields:");
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            System.out.println(" - " + f.getName() + " : " + f.getType() + " = " + (Modifier.isStatic(f.getModifiers()) ? f.get(null) : "instance"));
        }
    }
}
