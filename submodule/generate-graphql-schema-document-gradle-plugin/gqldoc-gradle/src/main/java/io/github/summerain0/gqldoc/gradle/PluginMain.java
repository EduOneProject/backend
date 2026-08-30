package io.github.summerain0.gqldoc.gradle;

import io.github.summerain0.gqldoc.gradle.task.GenerateGraphqlSchemaDocumentTask;
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
        TaskProvider<GenerateGraphqlSchemaDocumentTask> generateGraphqlDocumentTaskProvider = target.getTasks()
                .register("generateGraphqlSchemaDocument", GenerateGraphqlSchemaDocumentTask.class);
        generateGraphqlDocumentTaskProvider.get().setGroup("gqldoc");

        generateGraphqlDocumentTaskProvider.configure(task -> task.dependsOn("compileJava"));
    }
}