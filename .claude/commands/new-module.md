---
description: Scaffold a new module following this project's Layered + Facade pattern. Usage: /new-module {moduleName} (e.g., /new-module payment)
---

# New Module Scaffold

Generate all 10 files for a new module based on `$ARGUMENTS`, following this project's architecture conventions.

## Rules

- `$ARGUMENTS` = module name (e.g., `payment`, `orderItem`)
- Package root: `com.example.basicarch.module.{moduleName}`
- File path: `src/main/java/com/example/basicarch/module/{moduleName}/`
- `{Name}` = PascalCase (e.g., `Payment`, `OrderItem`)
- `{nameCamel}` = camelCase (e.g., `payment`, `orderItem`)
- `{table_name}` = snake_case (e.g., `payment`, `order_item`)

## Files to Generate (10 total)

### 1. `entity/{Name}.java`
```java
package com.example.basicarch.module.{moduleName}.entity;

import com.example.basicarch.base.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "{table_name}")
public class {Name} extends BaseEntity<Long> {
    // TODO: add fields
    @Column(name = "name", nullable = false)
    private String name;
}
```

### 2. `model/{Name}Model.java`
```java
package com.example.basicarch.module.{moduleName}.model;

import com.example.basicarch.base.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class {Name}Model extends BaseModel<Long> {
    // TODO: add fields
    private String name;
}
```

### 3. `model/{Name}SearchParam.java`
```java
package com.example.basicarch.module.{moduleName}.model;

import com.example.basicarch.base.model.BaseSearchParam;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class {Name}SearchParam extends BaseSearchParam<Long> {
    // TODO: add search fields
    private String name;
}
```

### 4. `repository/{Name}Repository.java`
```java
package com.example.basicarch.module.{moduleName}.repository;

import com.example.basicarch.module.{moduleName}.entity.{Name};
import org.springframework.data.jpa.repository.JpaRepository;

public interface {Name}Repository extends JpaRepository<{Name}, Long>, {Name}RepositoryCustom {
}
```

### 5. `repository/{Name}RepositoryCustom.java`
```java
package com.example.basicarch.module.{moduleName}.repository;

import com.example.basicarch.module.{moduleName}.entity.{Name};
import com.example.basicarch.module.{moduleName}.model.{Name}SearchParam;

import java.util.List;

public interface {Name}RepositoryCustom {
    List<{Name}> findAllBy({Name}SearchParam param);
}
```

### 6. `repository/{Name}RepositoryImpl.java`
```java
package com.example.basicarch.module.{moduleName}.repository;

import com.example.basicarch.module.{moduleName}.entity.{Name};
import com.example.basicarch.module.{moduleName}.entity.Q{Name};
import com.example.basicarch.module.{moduleName}.model.{Name}SearchParam;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class {Name}RepositoryImpl implements {Name}RepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<{Name}> findAllBy({Name}SearchParam param) {
        Q{Name} q = Q{Name}.{nameCamel};
        return queryFactory.selectFrom(q)
                .where(buildWhere(param, q))
                .orderBy(q.id.asc())
                .fetch();
    }

    private BooleanBuilder buildWhere({Name}SearchParam param, Q{Name} q) {
        BooleanBuilder builder = new BooleanBuilder();
        // TODO: add search conditions
        if (StringUtils.hasText(param.getName())) {
            builder.and(q.name.contains(param.getName()));
        }
        return builder;
    }
}
```

### 7. `service/{Name}Service.java`
```java
package com.example.basicarch.module.{moduleName}.service;

import com.example.basicarch.base.service.BaseService;
import com.example.basicarch.module.{moduleName}.entity.{Name};
import com.example.basicarch.module.{moduleName}.model.{Name}SearchParam;
import com.example.basicarch.module.{moduleName}.repository.{Name}Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service @Slf4j @RequiredArgsConstructor
public class {Name}Service implements BaseService<{Name}, {Name}SearchParam, Long> {
    private final {Name}Repository {nameCamel}Repository;

    @Override public boolean existsById(Long id) { return {nameCamel}Repository.existsById(id); }
    @Override public Optional<{Name}> findById(Long id) { return {nameCamel}Repository.findById(id); }
    @Override public List<{Name}> findAllBy({Name}SearchParam param) { return {nameCamel}Repository.findAllBy(param); }
    @Override public {Name} save({Name} entity) { return {nameCamel}Repository.save(entity); }
    @Override public {Name} update({Name} entity) { return {nameCamel}Repository.save(entity); }
    @Override public void deleteById(Long id) { if (id == null) return; {nameCamel}Repository.deleteById(id); }
}
```

### 8. `converter/{Name}Converter.java`
```java
package com.example.basicarch.module.{moduleName}.converter;

import com.example.basicarch.base.converter.TypeConverter;
import com.example.basicarch.module.{moduleName}.entity.{Name};
import com.example.basicarch.module.{moduleName}.model.{Name}Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TypeConverter.class})
public interface {Name}Converter {
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "localDateTimeToString")
    {Name}Model toModel({Name} entity);

    @Mapping(target = "rowNum", ignore = true)
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "stringToLocalDateTime")
    {Name} toEntity({Name}Model model);

    List<{Name}Model> toModelList(List<{Name}> list);
    List<{Name}> toEntityList(List<{Name}Model> list);
}
```

### 9. `facade/{Name}Facade.java`
```java
package com.example.basicarch.module.{moduleName}.facade;

import com.example.basicarch.base.annotation.Facade;
import com.example.basicarch.base.exception.SystemErrorCode;
import com.example.basicarch.base.exception.ToyAssert;
import com.example.basicarch.module.{moduleName}.converter.{Name}Converter;
import com.example.basicarch.module.{moduleName}.model.{Name}Model;
import com.example.basicarch.module.{moduleName}.model.{Name}SearchParam;
import com.example.basicarch.module.{moduleName}.service.{Name}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j @Facade @RequiredArgsConstructor
public class {Name}Facade {
    private final {Name}Service {nameCamel}Service;
    private final {Name}Converter {nameCamel}Converter;

    public List<{Name}Model> findAllBy({Name}SearchParam param) {
        return {nameCamel}Converter.toModelList({nameCamel}Service.findAllBy(param));
    }

    public {Name}Model findById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID is required.");
        return {nameCamel}Service.findById(id).map({nameCamel}Converter::toModel).orElse(null);
    }

    public void create({Name}Model model) {
        ToyAssert.notBlank(model.getName(), SystemErrorCode.REQUIRED, "name is required.");
        {nameCamel}Service.save({nameCamel}Converter.toEntity(model));
    }

    public void update({Name}Model model) {
        ToyAssert.notNull(model.getId(), SystemErrorCode.REQUIRED, "ID is required.");
        ToyAssert.notBlank(model.getName(), SystemErrorCode.REQUIRED, "name is required.");
        {nameCamel}Service.update({nameCamel}Converter.toEntity(model));
    }

    @Transactional
    public void deleteById(Long id) {
        ToyAssert.notNull(id, SystemErrorCode.REQUIRED, "ID is required.");
        {nameCamel}Service.deleteById(id);
    }
}
```

### 10. `controller/{Name}Controller.java`
```java
package com.example.basicarch.module.{moduleName}.controller;

import com.example.basicarch.module.{moduleName}.facade.{Name}Facade;
import com.example.basicarch.module.{moduleName}.model.{Name}Model;
import com.example.basicarch.module.{moduleName}.model.{Name}SearchParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{moduleName}")
@RequiredArgsConstructor
public class {Name}Controller {
    private final {Name}Facade {nameCamel}Facade;

    @GetMapping
    public List<{Name}Model> selectList({Name}SearchParam param) { return {nameCamel}Facade.findAllBy(param); }

    @GetMapping("/{id}")
    public {Name}Model selectById(@PathVariable Long id) { return {nameCamel}Facade.findById(id); }

    @PostMapping
    public void create(@RequestBody {Name}Model model) { {nameCamel}Facade.create(model); }

    @PutMapping
    public void update(@RequestBody {Name}Model model) { {nameCamel}Facade.update(model); }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) { {nameCamel}Facade.deleteById(id); }
}
```

## After Generation

Remind the user to:
1. Add fields to `{Name}.java` and `{Name}Model.java`
2. Create Flyway migration: `src/main/resources/db/migration/V{next}__{table_name}.sql`
3. Run build to generate QueryDSL Q-classes: `JAVA_HOME=/c/java/jdk-21.0.10+7 ./gradlew clean build`
