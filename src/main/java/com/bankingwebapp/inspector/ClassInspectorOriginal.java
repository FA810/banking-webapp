package com.bankingwebapp.inspector;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;

public class ClassInspectorOriginal {

    public static void main(String[] args) throws Exception {
        // List of package names to inspect
        String[] packageNames = {
            "com/bankingwebapp/entity", // Use slashes for path
            "com/bankingwebapp/controller",
            "com/bankingwebapp/service"
        };

        // Iterate through each package
        for (String packageName : packageNames) {
            inspectClassesInPackage(packageName);
        }
    }

    private static void inspectClassesInPackage(String packageName) throws ClassNotFoundException {
        // Get the classloader of your project
        ClassLoader classLoader = ClassInspectorOriginal.class.getClassLoader();

        // Construct the URL for the package
        URL packageURL = classLoader.getResource(packageName);
        
        if (packageURL != null) {
            // Get all the .class files in the package
            File directory = new File(packageURL.getFile());
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName.replace("/", ".") + "." + file.getName().replace(".class", "");
                    Class<?> clazz = Class.forName(className);
                    System.out.println("Class: " + clazz.getName());
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        System.out.println(" - " + field.getName() + " (" + field.getType().getSimpleName() + ")");
                    }
                    System.out.println(); // Separate classes
                }
            }
        } else {
            System.out.println("Package not found: " + packageName);
        }
    }
}
