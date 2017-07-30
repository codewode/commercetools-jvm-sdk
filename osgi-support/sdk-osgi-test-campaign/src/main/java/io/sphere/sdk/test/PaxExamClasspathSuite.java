
package io.sphere.sdk.test;

import org.junit.extensions.cpsuite.ClassesFinder;
import org.junit.extensions.cpsuite.ClassesFinderFactory;
import org.junit.extensions.cpsuite.ClasspathFinderFactory;
import org.junit.extensions.cpsuite.SuiteType;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;
import org.ops4j.pax.exam.junit.impl.ProbeRunner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.extensions.cpsuite.ClasspathSuite.*;

public class PaxExamClasspathSuite extends Suite {

    private static final boolean DEFAULT_INCLUDE_JARS = false;
    private static final SuiteType[] DEFAULT_SUITE_TYPES = new SuiteType[]{SuiteType.TEST_CLASSES};
    private static final Class<?>[] DEFAULT_BASE_TYPES = new Class<?>[]{Object.class};
    private static final Class<?>[] DEFAULT_EXCLUDED_BASES_TYPES = new Class<?>[0];
    private static final String[] DEFAULT_CLASSNAME_FILTERS = new String[0];
    private static final String DEFAULT_CLASSPATH_PROPERTY = "java.class.path";
    private final Class<?> suiteClass;


    private static String TEST_PREFIX = "___";

    static{
        System.setProperty("org.ops4j.pax.logging.DefaultServiceLog.level","WARN");
    }


    /**
     * Used by JUnit
     */
    public PaxExamClasspathSuite(final Class<?> suiteClass, final RunnerBuilder builder) throws Exception {
        this(suiteClass, builder, new ClasspathFinderFactory());
    }

    /**
     * For testing purposes only
     */
    public PaxExamClasspathSuite(final Class<?> suiteClass,final  RunnerBuilder builder,final  ClassesFinderFactory factory) throws Exception {
        super(suiteClass, setPaxExamRunnerForTestSuitClasses(getSortedTestclasses(createFinder(suiteClass, factory))));
        this.suiteClass = suiteClass;
    }

    private static List<Runner> setPaxExamRunnerForTestSuitClasses(final Class<?>[] classes) throws Exception {
        List<Runner> runners = new ArrayList<>();
        for (Class clazz : classes) {
            if(clazz.getSimpleName().startsWith(TEST_PREFIX) ){
                runners.add(new ProbeRunner(clazz));
            }
        }
        return runners;
    }

    private static ClassesFinder createFinder(final Class<?> suiteClass,final  ClassesFinderFactory finderFactory) {
        return finderFactory.create(getSearchInJars(suiteClass), getClassnameFilters(suiteClass), getSuiteTypes(suiteClass),
                getBaseTypes(suiteClass), getExcludedBaseTypes(suiteClass), getClasspathProperty(suiteClass));
    }

    private static Class<?>[] getSortedTestclasses(final ClassesFinder finder) {
        List<Class<?>> testclasses = finder.find();
        Collections.sort(testclasses, getClassComparator());
        return testclasses.toArray(new Class[testclasses.size()]);
    }

    private static Comparator<Class<?>> getClassComparator() {
        return Comparator.comparing(Class::getName);
    }

    private static String[] getClassnameFilters(final Class<?> suiteClass) {
        ClassnameFilters filtersAnnotation = suiteClass.getAnnotation(ClassnameFilters.class);
        if (filtersAnnotation == null) {
            return DEFAULT_CLASSNAME_FILTERS;
        }
        return filtersAnnotation.value();
    }

    private static boolean getSearchInJars(final Class<?> suiteClass) {
        IncludeJars includeJarsAnnotation = suiteClass.getAnnotation(IncludeJars.class);
        if (includeJarsAnnotation == null) {
            return DEFAULT_INCLUDE_JARS;
        }
        return includeJarsAnnotation.value();
    }

    private static SuiteType[] getSuiteTypes(final Class<?> suiteClass) {
        SuiteTypes suiteTypesAnnotation = suiteClass.getAnnotation(SuiteTypes.class);
        if (suiteTypesAnnotation == null) {
            return DEFAULT_SUITE_TYPES;
        }
        return suiteTypesAnnotation.value();
    }

    private static Class<?>[] getBaseTypes(final Class<?> suiteClass) {
        BaseTypeFilter baseTypeAnnotation = suiteClass.getAnnotation(BaseTypeFilter.class);
        if (baseTypeAnnotation == null) {
            return DEFAULT_BASE_TYPES;
        }
        return baseTypeAnnotation.value();
    }

    private static Class<?>[] getExcludedBaseTypes(final Class<?> suiteClass) {
        ExcludeBaseTypeFilter excludeBaseTypeAnnotation = suiteClass.getAnnotation(ExcludeBaseTypeFilter.class);
        if (excludeBaseTypeAnnotation == null) {
            return DEFAULT_EXCLUDED_BASES_TYPES;
        }
        return excludeBaseTypeAnnotation.value();
    }

    private static String getClasspathProperty(final Class<?> suiteClass) {
        ClasspathProperty cpPropertyAnnotation = suiteClass.getAnnotation(ClasspathProperty.class);
        if (cpPropertyAnnotation == null) {
            return DEFAULT_CLASSPATH_PROPERTY;
        }
        return cpPropertyAnnotation.value();
    }

    @Override
    public void run(final RunNotifier notifier) {
        try {
            runBeforeMethods();
        } catch (Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
            return;
        }
        super.run(notifier);
    }

    private void runBeforeMethods() throws Exception {
        for (Method each : suiteClass.getMethods()) {
            if (each.isAnnotationPresent(BeforeSuite.class)) {
                if (isPublicStaticVoid(each)) {
                    each.invoke(null, new Object[0]);
                }
            }
        }
    }

    private boolean isPublicStaticVoid(final Method method) {
        return method.getReturnType() == void.class && method.getParameterTypes().length == 0
                && (method.getModifiers() & Modifier.STATIC) != 0;
    }
}
