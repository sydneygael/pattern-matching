package demo;

import java.lang.classfile.ClassFile;
import java.lang.classfile.Instruction;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BytecodeInspector {

    public static List<String> opcodes(Class<?> clazz, String methodName) {
        var opcodes = new ArrayList<String>();

        try {
            var bytes = classBytes(clazz);
            var classModel = ClassFile.of().parse(bytes);

            classModel.methods().stream()
                .filter(method -> method.methodName().stringValue().equals(methodName))
                .findFirst()
                .flatMap(method -> method.code())
                .ifPresent(code -> {
                    for (var element : code.elementList()) {
                        if (element instanceof Instruction instruction) {
                            opcodes.add(instruction.opcode().name().toLowerCase());
                        }
                    }
                });

            return opcodes;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture bytecode de " + clazz.getSimpleName(), e);
        }
    }

    public static int classFileSize(Class<?> clazz) {
        return classBytes(clazz).length;
    }

    public static String report(Class<?> clazz, String... methodNames) {
        var methods = Arrays.stream(methodNames).filter(name -> name != null && !name.isBlank()).toList();
        var report = new StringBuilder();

        report.append("=== Bytecode report: ").append(clazz.getName()).append(" ===").append(System.lineSeparator());
        report.append("simpleName: ").append(clazz.getSimpleName()).append(System.lineSeparator());
        report.append("modifiers: ").append(Modifier.toString(clazz.getModifiers())).append(System.lineSeparator());
        report.append("superClass: ")
            .append(clazz.getSuperclass() == null ? "<none>" : clazz.getSuperclass().getName())
            .append(System.lineSeparator());
        report.append("interfaces: ").append(interfaceNames(clazz)).append(System.lineSeparator());
        report.append("declaredMethods:").append(System.lineSeparator());
        for (var declaredMethod : declaredMethods(clazz)) {
            report.append("  - ").append(declaredMethod).append(System.lineSeparator());
        }
        report.append("classFileSize: ").append(classFileSize(clazz)).append(" bytes").append(System.lineSeparator());

        if (!methods.isEmpty()) {
            report.append("bytecode:").append(System.lineSeparator());
            for (var methodName : methods) {
                var opcodes = opcodes(clazz, methodName);
                report.append("  - ").append(methodName)
                    .append(": ").append(opcodes.size()).append(" instructions")
                    .append(", unique=").append(opcodes.stream().distinct().count())
                    .append(System.lineSeparator());
                report.append("    opcodes=").append(opcodes).append(System.lineSeparator());
            }
        }

        return report.toString();
    }

    private static byte[] classBytes(Class<?> clazz) {
        try (var stream = clazz.getResourceAsStream(clazz.getSimpleName() + ".class")) {
            if (stream == null) {
                throw new IllegalStateException("Classe introuvable : " + clazz.getName());
            }
            return stream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lecture classe " + clazz.getSimpleName(), e);
        }
    }

    private static String interfaceNames(Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces())
            .map(Class::getName)
            .sorted()
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static List<String> declaredMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .sorted(Comparator.comparing(Method::getName).thenComparingInt(Method::getParameterCount))
            .map(BytecodeInspector::methodSignature)
            .toList();
    }

    private static String methodSignature(Method method) {
        var modifiers = Modifier.toString(method.getModifiers());
        var params = Arrays.stream(method.getParameterTypes())
            .map(Class::getSimpleName)
            .collect(Collectors.joining(", "));
        var base = String.format(
            Locale.ROOT,
            "%s %s(%s)",
            method.getReturnType().getSimpleName(),
            method.getName(),
            params
        );
        return modifiers.isBlank() ? base : modifiers + " " + base;
    }
}
