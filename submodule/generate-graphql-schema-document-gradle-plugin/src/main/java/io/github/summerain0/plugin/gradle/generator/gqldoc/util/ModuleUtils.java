package io.github.summerain0.plugin.gradle.generator.gqldoc.util;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块工具类
 *
 * @author summerain0
 */
public class ModuleUtils {
    /**
     * 获取所有模块项目
     *
     * @param project 根项目
     * @return 所有模块项目
     */
    public static List<Project> getAllModuleProject(Project project) {
        List<Project> list = new ArrayList<>();
        if (project != project.getRootProject()) {
            list.add(project);
        }
        if (project.getChildProjects().isEmpty()) {
            return list;
        }
        for (Project projectItem : project.getChildProjects().values()) {
            list.addAll(getAllModuleProject(projectItem));
        }
        return list;
    }
}