package rm.tabou2.service.tabou.tiers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.ContactTiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.tiers.ContactTiersRightsHelper;
import rm.tabou2.service.mapper.tabou.tiers.ContactTiersMapper;
import rm.tabou2.service.tabou.tiers.ContactTiersService;
import rm.tabou2.storage.tabou.dao.tiers.ContactTiersCustomDao;
import rm.tabou2.storage.tabou.dao.tiers.ContactTiersDao;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;
import rm.tabou2.storage.tabou.item.ContactTiersCriteria;

import java.text.MessageFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ContactTiersServiceImpl implements ContactTiersService {

    @Autowired
    private ContactTiersMapper mapper;

    @Autowired
    private ContactTiersDao contactTiersDao;

    @Autowired
    private ContactTiersCustomDao contactTiersCustomDao;

    @Autowired
    private ContactTiersRightsHelper contactTiersRightsHelper;

    private static final String CONTACT_NOT_FOUND = "Le contact tiers id={0} nexiste pas pour le tiers id={1}";


    @Override
    public ContactTiers getContactTiersById(Long tiersId, Long contactTiersId) {

        if (!contactTiersRightsHelper.checkCanGetContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'accéder un contact de tiers");
        }

        Optional<ContactTiersEntity> contactTiers = contactTiersDao.findById(contactTiersId);

        if (contactTiers.isEmpty() || !tiersId.equals(contactTiers.get().getIdTiers())) {
            throw new NoSuchElementException(MessageFormat.format(CONTACT_NOT_FOUND, contactTiersId, tiersId));
        }

        return mapper.entityToDto(contactTiers.get());
    }

    @Override
    public ContactTiers createContactTiers(Long tiersId, ContactTiers contactTiers) {
        if (!contactTiersRightsHelper.checkCanCreateContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de créer un contact de tiers");
        }

        contactTiers.setIdTiers(tiersId);

        ContactTiersEntity entity = contactTiersDao.save(mapper.dtoToEntity(contactTiers));
        return mapper.entityToDto(entity);
    }

    @Override
    public ContactTiers updateContactTiers(Long tiersId, ContactTiers contactTiers) {
        if (!contactTiersRightsHelper.checkCanUpdateContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'éditer un contact de tiers");
        }
        Optional<ContactTiersEntity> entityOptional = contactTiersDao.findById(tiersId);
        if (entityOptional.isEmpty() || !tiersId.equals(entityOptional.get().getIdTiers())) {
            throw new NoSuchElementException(MessageFormat.format(CONTACT_NOT_FOUND, contactTiers.getIdTiers(), tiersId));
        }
        ContactTiersEntity entity = contactTiersDao.save(mapper.dtoToEntity(contactTiers));
        return mapper.entityToDto(entity);

    }

    @Override
    public ContactTiers inactivateContactTiers(Long tiersId, long contactTiersId) throws AppServiceException {
        if (!contactTiersRightsHelper.checkCanUpdateContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'éditer un contact de tiers");
        }
        Optional<ContactTiersEntity> contactTiers = contactTiersDao.findById(contactTiersId);

        if (contactTiers.isEmpty() || !tiersId.equals(contactTiers.get().getIdTiers())) {
            throw new NoSuchElementException(MessageFormat.format(CONTACT_NOT_FOUND, contactTiersId, tiersId));
        }

        ContactTiersEntity entity = contactTiers.get();
        entity.setDateInactif(new Date());

        try {
            return mapper.entityToDto(contactTiersDao.save(entity));
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactif le contact tiers " + contactTiersId, e);
        }
    }

    @Override
    public Page<ContactTiers> searchContactTiers(ContactTiersCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(contactTiersCustomDao.searchContactTiers(criteria, pageable), pageable);
    }
}
