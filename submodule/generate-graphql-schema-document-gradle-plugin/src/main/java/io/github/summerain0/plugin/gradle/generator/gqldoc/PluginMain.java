package io.github.summerain0.plugin.gradle.generator.gqldoc;

import io.github.summerain0.plugin.gradle.generator.gqldoc.task.GenerateGraphqlSchemaDocumentTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;

/**
 * 插件入口类
 *
 * @author summerain0
 */
@SuppressWarnings("unused")
public class PluginMain implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        TaskProvider<GenerateGraphqlSchemaDocumentTask> generateGraphqlDocumentTaskProvider = target.getTasks().register("generate", GenerateGraphqlSchemaDocumentTask.class);
        generateGraphqlDocumentTaskProvider.get().setGroup("gen-gql-document");
    }
}
