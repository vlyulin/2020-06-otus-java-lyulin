package ru.otus.frontend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.frontend.appcontainer.AppComponentsContainerImpl;
import ru.otus.frontend.appcontainer.api.AppComponentsContainer;
import ru.otus.frontend.appcontainer.exceptions.AppComponentExistsException;
import ru.otus.frontend.config.AppConfig;
import ru.otus.frontend.config.AppConfig2;
import ru.otus.frontend.devidedconfig.AppDevidedConfig;
import ru.otus.frontend.devidedconfig.AppDevidedConfig2;
import ru.otus.frontend.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class AppComponentsContainerImplTest {

    public static final int EXPECTED_COMPONENTS = 4;
    public static final int EXPECTED_3_COMPONENTS = 3;
    public static final String RU_OTUS_DEVIDEDCONFIG = "ru.otus.devidedconfig";

    @Test
    @DisplayName("Проверка на создание дубликатов объектов")
    void testDuplicates() {
        Assertions.assertThrows(AppComponentExistsException.class, () -> {
            AppComponentsContainer container =
                    new AppComponentsContainerImpl(
                            AppConfig.class,
                            AppConfig2.class);
        });
    }

    @Test
    @DisplayName("Создание контекста через указание одного конфигурационного класса")
    void testContextByOneClass() {
        AppComponentsContainer container =
                new AppComponentsContainerImpl(
                        AppDevidedConfig.class
                );
        List<Object> components = new ArrayList<>();
        components.add(container.getAppComponent(EquationPreparer.class));
        components.add(container.getAppComponent(PlayerService.class));
        components.add(container.getAppComponent(IOService.class));
        assertThat(components.size()).isEqualTo(EXPECTED_3_COMPONENTS);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Object obj = container.getAppComponent(GameProcessor.class);
        });
    }

    @Test
    @DisplayName("Создание контекста через указание конфигурационных классов")
    void testContextByClasses() {
        AppComponentsContainer container =
                new AppComponentsContainerImpl(
                        AppDevidedConfig.class,
                        AppDevidedConfig2.class
                );
        List<Object> components = new ArrayList<>();
        components.add(container.getAppComponent(EquationPreparer.class));
        components.add(container.getAppComponent(PlayerService.class));
        components.add(container.getAppComponent(IOService.class));
        components.add(container.getAppComponent(GameProcessor.class));
        assertThat(components.size()).isEqualTo(EXPECTED_COMPONENTS);
    }

    @Test
    @DisplayName("Создание контекста через указание наименования пакета")
    void testContextByPackageName() {
        AppComponentsContainer container =
                new AppComponentsContainerImpl(RU_OTUS_DEVIDEDCONFIG);
        List<Object> components = new ArrayList<>();
        components.add(container.getAppComponent(EquationPreparer.class));
        components.add(container.getAppComponent(PlayerService.class));
        components.add(container.getAppComponent(IOService.class));
        components.add(container.getAppComponent(GameProcessor.class));
        assertThat(components.size()).isEqualTo(EXPECTED_COMPONENTS);
    }

    @Test
    @DisplayName("Проверка работоспособности getAppComponent(GameProcessor.class)")
    void testRunnableByClass() {
        AppComponentsContainer container =
                new AppComponentsContainerImpl(RU_OTUS_DEVIDEDCONFIG);
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);

        assertThat(gameProcessor).isNotNull();
        assertThat(gameProcessor.getClass().getInterfaces()).contains(GameProcessor.class);
    }

    @Test
    @DisplayName("Проверка работоспособности getAppComponent(GameProcessorImpl.class)")
    void testRunnableByClassImpl() {
        AppComponentsContainer container =
                new AppComponentsContainerImpl(RU_OTUS_DEVIDEDCONFIG);
        GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        assertThat(gameProcessor.getClass()).isNotNull().isEqualTo(GameProcessorImpl.class);
    }

    @Test
    @DisplayName("Проверка работоспособности getAppComponent(\"gameProcessor\")")
    void testRunnableByClassName() {
        AppComponentsContainer container =
                new AppComponentsContainerImpl(RU_OTUS_DEVIDEDCONFIG);
        GameProcessor gameProcessor = container.getAppComponent("gameProcessor");
        assertThat(gameProcessor.getClass()).isNotNull().isEqualTo(GameProcessorImpl.class);
    }
}