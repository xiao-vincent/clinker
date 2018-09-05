package com.vince.retailmanager.mapper;


import com.vince.retailmanager.entity.Franchisor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EntityObjectMampper {

    @Mapping(ignore = true, target = "id")
    Franchisor sourceToDestination(Franchisor franchisor, @MappingTarget Franchisor target);


}
