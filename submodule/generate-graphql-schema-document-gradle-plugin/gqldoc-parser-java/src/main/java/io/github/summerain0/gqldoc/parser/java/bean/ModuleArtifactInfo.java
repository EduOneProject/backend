package io.github.summerain0.gqldoc.parser.java.bean;

import lombok.Data;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * 模块工件信息
 *
 * @author summerain0
 */
@Data
public class ModuleArtifactInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 组ID
     */
    private String groupId;

    /**
     * 工件ID
     */
    private String artifactId;

    /**
     * 版本
     */
    private String version;

    /**
     * class jar路径
     */
    private Collection<File> classesJarFiles;

    /**
     * 源代码jar路径
     */
    private Collection<File> sourcesJarFiles;
}
