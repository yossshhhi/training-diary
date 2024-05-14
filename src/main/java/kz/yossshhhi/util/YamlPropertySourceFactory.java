package kz.yossshhhi.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Custom {@link YamlPropertySourceFactory} for loading YAML properties.
 * This class provides a mechanism for loading properties from YAML files into Spring's environment.
 */
public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {

    /**
     * Create a {@link PropertySource} from the given {@link EncodedResource}.
     * This method loads YAML formatted files into {@link Properties}.
     *
     * @param name the name of the property source; if null, the name will be derived from the resource's filename
     * @param resource the resource (YAML file) to load; if null, will fall back to default properties handling
     * @return a {@link PropertySource} containing properties loaded from the specified YAML resource
     * @throws IOException if the resource cannot be opened or read
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            return super.createPropertySource(name, resource);
        }

        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
        return new PropertiesPropertySource((name != null) ? name : Objects.requireNonNull(resource.getResource().getFilename()), propertiesFromYaml);
    }

    /**
     * Loads properties from a YAML resource into a {@link Properties} object.
     *
     * @param resource the YAML file as an {@link EncodedResource}
     * @return a {@link Properties} object containing the YAML properties
     */
    private Properties loadYamlIntoProperties(EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
