package demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class BytecodeTest {

    @Test
    void avant_utilise_invokevirtual() {
        var opcodes = BytecodeInspector.opcodes(AvantPatternMatching.class, "aire");

        assertThat(opcodes).contains("invokevirtual");
        assertThat(opcodes).contains("instanceof");
        assertThat(opcodes).doesNotContain("invokedynamic");
    }

    @Test
    void apres_utilise_invokedynamic() {
        var opcodes = BytecodeInspector.opcodes(ApresPatternMatching.class, "aire");

        assertThat(opcodes).contains("invokedynamic");
        assertThat(opcodes).containsAnyOf("lookupswitch", "tableswitch");
        assertThat(opcodes).doesNotContain("instanceof");
    }

    @Test
    void imprime_rapport_bytecode() {
        var avantReport = BytecodeInspector.report(AvantPatternMatching.class, "aire");
        var apresReport = BytecodeInspector.report(ApresPatternMatching.class, "aire");

        System.out.println(avantReport);
        System.out.println(apresReport);

        assertThat(avantReport).contains("classFileSize:");
        assertThat(avantReport).contains("declaredMethods:");
        assertThat(avantReport).contains("interfaces:");
        assertThat(avantReport).contains("instanceof");

        assertThat(apresReport).contains("classFileSize:");
        assertThat(apresReport).contains("declaredMethods:");
        assertThat(apresReport).contains("interfaces:");
        assertThat(apresReport).contains("invokedynamic");
    }

    @Test
    void avant_javap_utilise_invokevirtual() throws IOException, InterruptedException, URISyntaxException {
        var disassembly = javapDisassembly(AvantPatternMatching.class);
        System.out.println(disassembly);

        assertThat(disassembly).contains("invokevirtual");
        assertThat(disassembly).contains("instanceof");
        assertThat(disassembly).doesNotContain("invokedynamic");
    }

    @Test
    void apres_javap_utilise_invokedynamic() throws IOException, InterruptedException, URISyntaxException {
        var disassembly = javapDisassembly(ApresPatternMatching.class);
        System.out.println(disassembly);

        assertThat(disassembly).contains("invokedynamic");
        assertThat(disassembly).containsAnyOf("lookupswitch", "tableswitch");
        assertThat(disassembly).doesNotContain("instanceof");
    }

    private static String javapDisassembly(Class<?> clazz) throws IOException, InterruptedException, URISyntaxException {
        var javap = javapExecutable();
        assumeTrue(Files.isRegularFile(javap), "javap introuvable: " + javap);

        var classpathRoot = classpathRoot(clazz);
        var process = new ProcessBuilder(
            javap.toString(),
            "-c",
            "-classpath",
            classpathRoot.toString(),
            clazz.getName()
        )
            .redirectErrorStream(true)
            .start();

        var output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        var exitCode = process.waitFor();

        assertThat(exitCode).as("javap exit code").isZero();
        return output;
    }

    private static Path javapExecutable() {
        var javaHome = Path.of(System.getProperty("java.home"));
        var binHome = javaHome.resolve("bin");
        var javap = binHome.resolve(isWindows() ? "javap.exe" : "javap");
        if (Files.isRegularFile(javap)) {
            return javap;
        }

        // Certains environnements retournent un java.home sous-repertoire d'un JDK.
        var parentBin = javaHome.getParent() == null ? null : javaHome.getParent().resolve("bin");
        if (parentBin != null) {
            var parentJavap = parentBin.resolve(isWindows() ? "javap.exe" : "javap");
            if (Files.isRegularFile(parentJavap)) {
                return parentJavap;
            }
        }
        return javap;
    }

    private static Path classpathRoot(Class<?> clazz) throws URISyntaxException {
        var classResource = clazz.getResource(clazz.getSimpleName() + ".class");
        assumeTrue(classResource != null, "Classe introuvable dans le classpath");
        assumeTrue("file".equalsIgnoreCase(classResource.getProtocol()), "Classe non chargee depuis un filesystem");

        var classFile = Path.of(classResource.toURI());
        var pkg = clazz.getPackageName();
        if (pkg == null || pkg.isBlank()) {
            return classFile.getParent();
        }

        var root = classFile.getParent();
        var segments = pkg.split("\\.");
        for (int i = 0; i < segments.length; i++) {
            root = root.getParent();
        }
        return root;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
