import java.io.*;
import java.util.*;

public class KeywordCounter {
    private static final List<String> JAVA_KEYWORDS = Arrays.asList(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
        "class", "continue", "default", "do", "double", "else", "enum", "extends",
        "final", "finally", "float", "for", "if", "implements", "import", "instanceof",
        "int", "interface", "long", "native", "new", "package", "private", "protected",
        "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized",
        "this", "throw", "throws", "transient", "try", "void", "volatile", "while"
    );

    private static final Set<String> CONTROL_STATEMENTS = Set.of("if", "for", "while", "switch", "do", "try", "catch");
    private static final Set<String> DATA_TYPES = Set.of("int", "double", "float", "boolean", "char", "byte", "long", "short");

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java KeywordCounter <source-file>");
            return;
        }

        String fileName = args[0];
        int keywordCount = 0;
        int controlCount = 0;
        int dataTypeCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean inBlockComment = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Remove block comments
                if (inBlockComment) {
                    int endBlockIndex = line.indexOf("*/");
                    if (endBlockIndex != -1) {
                        line = line.substring(endBlockIndex + 2).trim();
                        inBlockComment = false;
                    } else {
                        continue;
                    }
                }

                if (line.startsWith("/*")) {
                    inBlockComment = true;
                    continue;
                }

                // Remove inline comments
                int inlineCommentIndex = line.indexOf("//");
                if (inlineCommentIndex != -1) {
                    line = line.substring(0, inlineCommentIndex).trim();
                }

                // Remove string literals
                line = line.replaceAll("\".*?\",", "");

                // Tokenize the line
                String[] tokens = line.split("\\s+|(?<=\\W)|(?=\\W)");
                for (String token : tokens) {
                    if (JAVA_KEYWORDS.contains(token)) {
                        keywordCount++;
                        if (CONTROL_STATEMENTS.contains(token)) {
                            controlCount++;
                        }
                        if (DATA_TYPES.contains(token)) {
                            dataTypeCount++;
                        }
                    }
                }
            }

            System.out.println("Total Keywords: " + keywordCount);
            System.out.println("Control Statements: " + controlCount);
            System.out.println("Data Types: " + dataTypeCount);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
