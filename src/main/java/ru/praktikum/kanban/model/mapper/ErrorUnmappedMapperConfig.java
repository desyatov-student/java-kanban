package ru.praktikum.kanban.model.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ErrorUnmappedMapperConfig {

}
