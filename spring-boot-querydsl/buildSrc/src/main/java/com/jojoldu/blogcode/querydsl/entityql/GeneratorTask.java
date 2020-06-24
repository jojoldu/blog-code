package com.jojoldu.blogcode.querydsl.entityql;

import com.jojoldu.blogcode.querydsl.entityql.entity.scanner.JPAQEntityScannerFactory;
import com.jojoldu.blogcode.querydsl.entityql.entity.scanner.QEntityScannerFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.reflections.Reflections;
import pl.exsio.querydsl.entityql.EntityQL;
import pl.exsio.querydsl.entityql.QExporter;

import org.gradle.api.Project;
import pl.exsio.querydsl.entityql.entity.scanner.QEntityScanner;

import javax.persistence.Entity;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Created by jojoldu@gmail.com on 23/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Slf4j
@Getter
public class GeneratorTask extends DefaultTask {
    private static final EnumMap<Generator.Type, QEntityScannerFactory> SCANNERS = new EnumMap<>(Generator.Type.class);
    private static final EnumMap<Generator.Type, BiFunction<Generator, URLClassLoader, Set<Class<?>>>> REFLECTION_SCANNERS = new EnumMap<>(Generator.Type.class);

    static {
        SCANNERS.put(Generator.Type.JPA, new JPAQEntityScannerFactory());
        REFLECTION_SCANNERS.put(Generator.Type.JPA, GeneratorTask::resolveJpaEntityClasses);
    }

    private final QExporter exporter = new QExporter();

    private List<Generator> generators;
    private Project project;

    private void generate(Generator generator, URLClassLoader classLoader) throws Exception {
        QEntityScanner scanner = SCANNERS.get(generator.getType()).createScanner(generator.getParams());
        log.info("Using scanner: {}", scanner.getClass().getName());

        generator.setDefaultDestinationPathIfNeeded(project.getProjectDir().getAbsolutePath());
        log.info("Generating EntityQL Static Models from package {} to package {}, destination path: {}",
                generator.getSourcePackage(), generator.getDestinationPackage(), generator.getDestinationPath()
        );

        Set<Class<?>> entityClasses = REFLECTION_SCANNERS.get(generator.getType()).apply(generator, classLoader);
        log.info("Found {} Entity Classes to export in package {}", entityClasses.size(), generator.getSourcePackage());

        for (Class<?> entityClass : entityClasses) {
            log.info("Exporting class: {}", entityClass.getName());
            exporter.export(
                    EntityQL.qEntity(entityClass, scanner),
                    generator.getFilenamePattern(),
                    generator.getDestinationPackage(),
                    generator.getDestinationPath()
            );
        }
    }

    private URLClassLoader classLoader() throws GradleException {
        List<String> classpathElements = getCompileClasspathElements();
        List<URL> projectClasspathList = new ArrayList<>();

        for (String element : classpathElements) {
            try {
                projectClasspathList.add(new File(element).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new GradleException(element + " is an invalid classpath element", e);
            }
        }

        return new URLClassLoader(
                projectClasspathList.toArray(new URL[0]),
                Thread.currentThread().getContextClassLoader());
    }

    private List<String> getCompileClasspathElements() {
        return new ArrayList<>();
//        try {
//            return project.getCompileClasspathElements();
//        } catch (DependencyResolutionRequiredException e) {
//            throw new GradleException("Dependency resolution failed", e);
//        }
    }

    private static Set<Class<?>> resolveJpaEntityClasses(Generator generator,
                                                         URLClassLoader classLoader) {
        Reflections reflections = new Reflections(generator.getSourcePackage(), classLoader);
        return reflections.getTypesAnnotatedWith(Entity.class);
    }

    @TaskAction
    public void compileEntityql() {
        URLClassLoader classLoader = classLoader();
        try {
            for (Generator generator : generators) {
                generate(generator, classLoader);
            }
        } catch (Exception ex) {
            throw new GradleException("Failed to generate Static Models", ex);
        }
    }

}
