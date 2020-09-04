package rm.tabou2.storage.dao;

import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.RefreshToken;

import java.util.List;

public interface RefreshTokenDao extends CrudRepository<RefreshToken, Long> {


    /**
     * Test l'existance d'un token
     *
     * @param token
     * @return
     */
    RefreshToken findFirstByTokenEquals(String token);

    @Override
    List<RefreshToken> findAll();
}
