package rm.tabou2.storage.common;

import org.springframework.data.repository.CrudRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface CustomCrudRepository<T, L> extends CrudRepository<T, L> {

    default T findOneById(L id) {
        Optional<T> tOpt = findById(id);
        if (tOpt.isEmpty()) {
            throw new NoSuchElementException("L'entité avec id = " + id + " n'existe pas");
        }
        return tOpt.get();
    }
}
