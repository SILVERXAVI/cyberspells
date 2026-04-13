import java.lang.reflect.*;

public class CyberDump {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.maxwell.cyber_ware_port.common.item.base.BodyPartType");
        System.out.println("Class: " + clazz.getName());
        if (clazz.isEnum()) {
            Object[] constants = clazz.getEnumConstants();
            System.out.println("Constants:");
            for (Object obj : constants) {
                System.out.println(" - " + obj.toString());
                for (Field f : clazz.getDeclaredFields()) {
                    if (!Modifier.isStatic(f.getModifiers()) || clazz.isAssignableFrom(f.getType())) {
                        f.setAccessible(true);
                        try {
                            Object val = f.get(obj);
                            if (!(val instanceof Enum)) {
                                System.out.println("   " + f.getName() + " = " + val);
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        } else {
            System.out.println("Fields:");
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                System.out.println(" - " + f.getName() + ": " + f.getType() + " (static=" + Modifier.isStatic(f.getModifiers()) + ")");
            }
        }
    }
}
