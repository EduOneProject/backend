<div align="center">

# 🎓 EduOne

**下一代教育一体化管理平台 · 让教育管理更简单**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/Gradle-9.5.1-blue?logo=gradle)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](#-贡献指南)

</div>

---

## 🚀 项目介绍

EduOne 是一个**功能全面的教育一体化管理系统**，致力于为教育机构提供从招生、教学、财务到教务管理的全链路数字化解决方案。系统围绕教育业务核心场景设计，涵盖：

- **报名与班级管理** — 学生报名、换班、班级资源管理
- **学习行为监管** — 出勤追踪、学习进度可视化、异常行为预警
- **电子证明系统** — 成绩单、在读证明、结业证书的在线生成与打印
- **交易结算系统** — 报名费缴纳、退款处理、财务对账
- **考试测评系统** — 在线考试、自动阅卷、成绩分析与试卷管理

---

## 🏗️ 技术架构

### 整体架构

EduOne 基于 **CQRS + Event Sourcing 架构模式**，采用多模块 Gradle 项目组织方式，在单一部署单元内实现命令端（Command
Model）与查询端（Query Model）的逻辑分离。这一架构选择兼顾了：

| 维度        | 说明                                            |
|-----------|-----------------------------------------------|
| **开发效率**  | 单体部署无需分布式基础设施，降低早期开发与调试复杂度                    |
| **领域驱动**  | 命令端与查询端职责清晰分离，各模块专注于自身领域逻辑                    |
| **演进能力**  | 各模块遵循分层依赖原则，未来可按限界上下文（Bounded Context）拆分为独立服务 |
| **数据一致性** | 通过事件溯源天然获得完整审计日志与业务回放能力                       |

### 核心技术栈

| 组件       | 选型                  |
|----------|---------------------|
| **基础框架** | Spring Boot         |
| **语言**   | Java 17             |
| **构建工具** | Gradle (Groovy DSL) |
| **数据库**  | MySQL               |

### 项目结构

```
eduone/
├── eduone-web/                           # Web应用启动模块
│   └── src/main/java/io/github/eduoneproject/eduone/
│       └── Application.java              # 应用启动入口
│
├── eduone-common/                        # 项目通用模块（基础工具、异常定义等）
├── eduone-domain-api/                    # 领域事件模型模块（事件定义 + 聚合根）
│
├── eduone-business-common/               # 业务端通用模块（共享业务模型、DTO）
├── eduone-business-kernel/               # 业务端核心模块（命令处理、业务编排）
├── eduone-business-gateway/              # 业务端网关模块（API 入口、请求分发）
│
├── eduone-query-common/                  # 查询端通用模块（共享查询模型、DTO）
├── eduone-query-kernel/                  # 查询端核心模块（查询处理）
├── eduone-query-gateway/                 # 查询端网关模块（查询 API 入口）
├── eduone-query-contract/                # 查询端对外接口契约模块（接口定义）
├── eduone-query-contract-adapter/        # 查询端接口契约实现模块（适配器）
│
├── eduone-repository/                    # 事件投影模块（投影物化视图落地）
│
├── build.gradle                          # 根构建脚本
├── settings.gradle                       # 多模块配置
├── gradle/                               # Gradle Wrapper 与版本目录
│   ├── libs.versions.toml                # 依赖版本目录
│   └── wrapper/
└── gradlew / gradlew.bat                 # Gradle Wrapper 脚本
```

## 🎯 设计目标

| 维度       | 目标                      |
|----------|-------------------------|
| **全面性**  | 覆盖教育机构核心业务场景，一站式解决管理痛点  |
| **可扩展性** | 模块化架构，支持功能定制            |
| **高可用性** | 支持快速恢复，保障核心业务连续性        |
| **安全性**  | 细粒度权限控制，审计日志完备，符合数据安全法规 |
| **易用性**  | 清晰的操作流程与友好的交互体验，降低使用门槛  |

---

## 🤝 贡献指南

感谢您对 EduOne 的关注！我们欢迎任何形式的贡献：

### 贡献方式

1. **提交 Issue** — 报告 Bug 或提出功能建议
2. **提交 Pull Request** — 修复问题或实现新功能
3. **完善文档** — 帮助改进项目文档和示例
4. **参与讨论** — 在 Discussions 中分享想法

### 流程

1. Fork 本仓库并创建你的分支（`git checkout -b feature/amazing-feature`）
2. 提交你的改动（`git commit -m 'feat: add amazing feature'`）
3. 推送到分支（`git push origin feature/amazing-feature`）
4. 创建一个 Pull Request

---

## 📄 许可证

本项目基于`Apache License 2.0`开源许可协议。

```
Copyright (c) 2026 EduOne Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

<div align="center">

**EduOne** — 用技术赋能教育，让管理更高效

⭐ 如果这个项目对你有帮助，欢迎 Star！

</div>
