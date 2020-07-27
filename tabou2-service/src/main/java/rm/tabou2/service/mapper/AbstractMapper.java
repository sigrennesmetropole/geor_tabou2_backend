package rm.tabou2.service.mapper;

import java.util.List;

/**
 * @param <E> entity
 * @param <D> DTO
 */
public interface AbstractMapper<E, D> {

    /**
     * @param dto dto to transform to entity
     * @return entity
     */
    E dtoToEntity(D dto);

    List<E> dtoToEntities(List<D> dtos);

    /**
     * @param entity entity to transform to dto
     * @return dto
     */
    D entityToDto(E entity);

    List<D> entitiesToDto(List<E> entities);
}
