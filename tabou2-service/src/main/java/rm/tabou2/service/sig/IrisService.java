package rm.tabou2.service.sig;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.storage.sig.item.IrisCriteria;

public interface IrisService {

    Page<Iris> searchIris(IrisCriteria irisCriteria, Pageable pageable);

    Iris getIrisById(int irisId);
}
