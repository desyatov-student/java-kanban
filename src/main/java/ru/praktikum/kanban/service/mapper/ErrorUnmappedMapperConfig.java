package ru.praktikum.kanban.service.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ErrorUnmappedMapperConfig {

}
