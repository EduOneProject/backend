package io.github.eduoneproject.eduone.config.graphql;

import graphql.language.*;
import graphql.parser.Parser;
import io.github.eduoneproject.eduone.common.util.SpringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 扫描graphql文件，提取@commonBusinessResponse指令
 *
 * @author summerain0
 */
public class CommonBusinessResponseDirectiveScanner {
    private static final String DIRECTIVE_NAME = "commonBusinessResponse";
    private static final String FOR_ARGUMENT_NAME = "for";

    /**
     * 扫描graphql文件，提取@commonBusinessResponse指令
     *
     * @return Set<String>
     */
    public Set<String> scanDirectives() {
        Set<String> targetTypeSet = new HashSet<>();
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String locationPattern = SpringUtils.getProperty("spring.graphql.schema.locations");
            String fileExtensions = SpringUtils.getProperty("spring.graphql.schema.file-extensions");
            Resource[] resources = resolver.getResources(locationPattern + "*" + fileExtensions);

            for (Resource resource : resources) {
                try (InputStream is = resource.getInputStream()) {
                    String sdl = readStream(is);
                    Document document = Parser.parse(sdl);
                    extractDirectives(document, targetTypeSet);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan @page directives from graphql files", e);
        }
        return targetTypeSet;
    }

    /**
     * 提取@commonBusinessResponse指令
     *
     * @param document      gql文档
     * @param targetTypeSet 需要注入CommonBusinessResponse<?>的类型
     */
    @SuppressWarnings("rawtypes")
    private void extractDirectives(Document document, Set<String> targetTypeSet) {
        List<Definition> definitions = document.getDefinitions();

        for (Definition definition : definitions) {
            if (definition instanceof FieldDefinition field) {
                for (var directive : field.getDirectives()) {
                    if (DIRECTIVE_NAME.equals(directive.getName())) {
                        String targetType = extractForArgument(directive.getArguments());
                        if (targetType != null) {
                            targetTypeSet.add(targetType);
                        }
                    }
                }
            } else if (definition instanceof ObjectTypeExtensionDefinition objectTypeExtensionDefinition) {
                List<FieldDefinition> fieldDefinitions = objectTypeExtensionDefinition.getFieldDefinitions();
                for (FieldDefinition fieldDefinition : fieldDefinitions) {
                    for (var directive : fieldDefinition.getDirectives()) {
                        if (DIRECTIVE_NAME.equals(directive.getName())) {
                            String targetType = extractForArgument(directive.getArguments());
                            if (targetType != null) {
                                targetTypeSet.add(targetType);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 提取@commonBusinessResponse指令的参数
     *
     * @param arguments 参数列表
     * @return 参数值
     */
    private String extractForArgument(List<Argument> arguments) {
        if (arguments == null) {
            return null;
        }
        for (Argument arg : arguments) {
            if (FOR_ARGUMENT_NAME.equals(arg.getName())) {
                if (arg.getValue() instanceof StringValue) {
                    return ((StringValue) arg.getValue()).getValue();
                }
            }
        }
        return null;
    }

    /**
     * 读取输入流
     *
     * @param inputStream 输入流
     * @return 字符串
     * @throws IOException 异常
     */
    private String readStream(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        }
    }
}