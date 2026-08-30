package io.github.summerain0.gqldoc.gradle.task;

import io.github.summerain0.gqldoc.core.ir.SchemaIR;
import io.github.summerain0.gqldoc.output.sdl.SdlFormatWriter;
import io.github.summerain0.gqldoc.parser.java.JavaSchemaGenerator;
import io.github.summerain0.gqldoc.parser.java.bean.ModuleArtifactInfo;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.*;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.SourceSetOutput;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.*;

/**
 * 生成GQL文档任务
 *
 * @author summerain0
 */
public abstract class GenerateGraphqlSchemaDocumentTask extends DefaultTask {
    @TaskAction
    public void generateGraphqlSchemaDocument() throws Exception {
        Project currentProject = getProject();
        Project rootProject = currentProject.getRootProject();
        Set<Project> allProjects = rootProject.getAllprojects();

        List<File> projectSrcDirFiles = new ArrayList<>();
        List<ModuleArtifactInfo> moduleArtifactInfoList = new ArrayList<>();

        for (Project project : allProjects) {
            // 处理项目的源码
            SourceSetContainer sourceSetContainer = project.getExtensions().getByType(SourceSetContainer.class);
            SourceSet sourceSet = sourceSetContainer.findByName(SourceSet.MAIN_SOURCE_SET_NAME);
            if (sourceSet != null) {
                SourceSetOutput sourceSetOutput = sourceSet.getOutput();
                FileCollection outputClassesDirs = sourceSetOutput.getClassesDirs();
                Set<File> outputClassesDirsFiles = outputClassesDirs.getFiles();
                SourceDirectorySet sourceDirectorySet = sourceSet.getJava();
                Set<File> srcDirs = sourceDirectorySet.getSrcDirs();
                projectSrcDirFiles.addAll(srcDirs);
                ModuleArtifactInfo moduleArtifactInfo = new ModuleArtifactInfo();
                moduleArtifactInfo.setGroupId(project.getGroup().toString());
                moduleArtifactInfo.setArtifactId(project.getName());
                moduleArtifactInfo.setVersion(project.getVersion().toString());
                moduleArtifactInfo.setClassesJarFiles(outputClassesDirsFiles);
                moduleArtifactInfo.setSourcesJarFiles(srcDirs);
                moduleArtifactInfoList.add(moduleArtifactInfo);
            }

            // 处理项目的依赖
            Configuration compileClasspathConfiguration = project.getConfigurations().getByName("compileClasspath");
            ResolvedConfiguration resolvedConfiguration = compileClasspathConfiguration.getResolvedConfiguration();
            Set<ResolvedArtifact> resolvedArtifacts = resolvedConfiguration.getResolvedArtifacts();
            for (ResolvedArtifact resolvedArtifact : resolvedArtifacts) {
                if (Objects.equals("jar", resolvedArtifact.getType())) {
                    ResolvedModuleVersion moduleVersion = resolvedArtifact.getModuleVersion();
                    ModuleVersionIdentifier moduleVersionIdentifier = moduleVersion.getId();
                    ModuleArtifactInfo moduleArtifactInfo = new ModuleArtifactInfo();
                    moduleArtifactInfo.setGroupId(moduleVersionIdentifier.getGroup());
                    moduleArtifactInfo.setArtifactId(moduleVersionIdentifier.getName());
                    moduleArtifactInfo.setVersion(moduleVersionIdentifier.getVersion());
                    moduleArtifactInfo.setClassesJarFiles(Collections.singletonList(resolvedArtifact.getFile()));
                    moduleArtifactInfoList.add(moduleArtifactInfo);
                }
            }
        }

        JavaSchemaGenerator schemaGenerator = new JavaSchemaGenerator(projectSrcDirFiles, moduleArtifactInfoList);
        List<SchemaIR> schemaIRList = schemaGenerator.generate();

        // 获取输出资源目录
        SourceSetContainer sourceSetContainer = currentProject.getExtensions().getByType(SourceSetContainer.class);
        SourceSet sourceSet = sourceSetContainer.findByName(SourceSet.MAIN_SOURCE_SET_NAME);
        if (sourceSet == null) {
            throw new RuntimeException("无法获取当前资源目录信息");
        }
        SourceDirectorySet sourceSetResources = sourceSet.getResources();
        Set<File> sourceSetResourcesSrcDirs = sourceSetResources.getSrcDirs();
        for (File sourceSetResourcesSrcDir : sourceSetResourcesSrcDirs) {
            File outputFile = new File(sourceSetResourcesSrcDir, "graphql");
            if (!outputFile.exists()) {
                boolean flag = outputFile.mkdirs();
                if (!flag) {
                    throw new RuntimeException("无法创建输出目录：" + outputFile.getAbsolutePath());
                }
            }
            SdlFormatWriter.write(schemaIRList, outputFile.toPath());
        }
    }
}
