package demo;

import static org.assertj.core.api.Assertions.assertThat;

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

}
