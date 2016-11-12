package org.springframework.samples.petclinic.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.*;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Creator implements ApplicationContextAware {

    Log log = LogFactory.getLog(Creator.class);

    private ApplicationContext applicationContext;
    @Autowired OwnerRepository ownerRepository;
    @Autowired PetRepository petRepository;
    @Autowired VetRepository vetRepository;
    @Autowired VisitRepository visitRepository;

    private Map<Class, JpaRepository> repositories = new HashMap<>();
    @PostConstruct
    public void postConstruct() {
        repositories.put(Owner.class, ownerRepository);
        repositories.put(Pet.class, petRepository);
        repositories.put(Vet.class, vetRepository);
        repositories.put(Visit.class, visitRepository);
    }

    public void save(Object... entities) {
        for (Object entity : entities) {
            save(entity);
        }
    }

    public Object save(Object entity) {
        try {
            Map props = PropertyUtils.describe(entity);
            List<Field> allFields = FieldUtils.getAllFieldsList(entity.getClass());
            for (Field field : allFields) {
                try {
                    field.setAccessible(true);
                    Object propValue = FieldUtils.readField(field, entity);
                    saveChildEntity(propValue);
                    if ((propValue ==null) && (field.getDeclaredAnnotationsByType(NotEmpty.class).length>0)) {
                        final String value;
                        if (field.getName().equals("telephone")) {
                            value = "0123456789";
                        } else {
                            value = "Test " + field.getName();
                        }
                        PropertyUtils.setProperty(entity, field.getName(), value);
                    }
                } catch (IllegalAccessException e) {
                    log.info("Skipping " + field.getName() + ", as it is probably private");
                }
            }

            for (Object propName : props.keySet()) {
                Object propValue = props.get(propName);
                saveChildEntity(propValue);
            }

            if (entity instanceof PetType || entity instanceof Specialty) {
                // These do not have their
                log.info("Ignoring named entity");
            } else {
                ( getDao(entity)).save(entity);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Problem", e);
        }
        return entity;

    }

    private boolean isNamedEntity(Object entity) {
        return entity instanceof NamedEntity;
    }

    private JpaRepository getDao(Object entity) {
        return repositories.get(entity.getClass());
    }


    private void saveChildEntity(Object propValue) {
        if (propValue!=null)  {
            Class<?> valueClass = propValue.getClass();
            final boolean isEntity = valueClass.getDeclaredAnnotationsByType(Entity.class).length > 0;
            if ((isEntity)) {

                save(propValue);
                String className = propValue.getClass().getSimpleName();
                String daoName = className.substring(0,1).toLowerCase() + className.substring(1) + "Repository";

                JpaRepository jpaRepository = applicationContext.getBeansOfType(JpaRepository.class).get(daoName);
                jpaRepository.save(propValue);
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }
}


