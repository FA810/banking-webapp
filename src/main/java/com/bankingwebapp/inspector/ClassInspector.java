package com.bankingwebapp.inspector;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassInspector {

    public static void inspectClass(Class<?> clazz) {
        // Print the class name
        System.out.println("Class: " + clazz.getName());

        // Inspect the fields of the class
        System.out.println("Fields:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  " + field.getName() + " - " + field.getType().getName());
        }

        // Inspect the methods of the class
        System.out.println("Methods:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  " + method.getName() + " - " + method.getReturnType().getName());
        }
    }

    public static List<Class<?>> getClassesInPackage(String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');
        File directory = new File(ClassLoader.getSystemResource(packagePath).getFile());

        // Check all files in the current directory
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // If it's a directory, recurse into it
                classes.addAll(getClassesInPackage(packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                // If it's a class file, add it to the list
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }

    public static void main(String[] args) {
        // Example: Inspect all classes in the com.bankingwebapp package
        try {
            List<Class<?>> classes = getClassesInPackage("com.bankingwebapp");
            for (Class<?> clazz : classes) {
                inspectClass(clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
