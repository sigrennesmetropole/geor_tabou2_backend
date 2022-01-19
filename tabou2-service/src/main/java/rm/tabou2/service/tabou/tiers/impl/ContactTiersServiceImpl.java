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
import rm.tabou2.storage.tabou.dao.tiers.FonctionContactsDao;
import rm.tabou2.storage.tabou.dao.tiers.TiersDao;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;
import rm.tabou2.storage.tabou.entity.tiers.FonctionContactsEntity;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;
import rm.tabou2.storage.tabou.item.ContactTiersCriteria;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactTiersServiceImpl implements ContactTiersService {

    @Autowired
    private ContactTiersMapper mapper;

    @Autowired
    private ContactTiersDao contactTiersDao;

    @Autowired
    private ContactTiersCustomDao contactTiersCustomDao;

    @Autowired
    private TiersDao tiersDao;

    @Autowired
    private ContactTiersRightsHelper contactTiersRightsHelper;

    @Autowired
    private FonctionContactsDao fonctionContactsDao;

    private static final String CONTACT_NOT_FOUND = "Le contact tiers id={0} nexiste pas pour le tiers id={1}";
    private static final String FONCTION_NOT_FOUND = "La fonction contact ({0}) n'a pas été trouvé";
    private static final String TIERS_NOT_FOUND = "Le tiers id ={0} n'existe pas";


    @Override
    public ContactTiers getContactTiersById(Long tiersId, Long contactTiersId) {

        if (!contactTiersRightsHelper.checkCanGetContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'accéder un contact de tiers");
        }
        Set<ContactTiersEntity> contacts = getTiers(tiersId).getContacts();

        Optional<ContactTiersEntity> contactTiers = contacts.stream()
                .filter(contactTiersEntity -> contactTiersEntity.getId() == contactTiersId)
                .findFirst();
        if(contactTiers.isEmpty()){
            throw new NoSuchElementException(MessageFormat.format(CONTACT_NOT_FOUND, contactTiersId, tiersId));
        }

        return mapper.entityToDto(contactTiers.get());
    }

    @Override
    public ContactTiers createContactTiers(Long tiersId, @Valid ContactTiers contactTiers) {
        if (!contactTiersRightsHelper.checkCanCreateContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de créer un contact de tiers");
        }

        //On récupère le tiers correspondant
        TiersEntity tiers = getTiers(tiersId);

        //On crée le contactTiers et on lui applique la bonne fonction de contact
        ContactTiersEntity contact = mapper.dtoToEntity(contactTiers);
        applyFonctionToContact(contact, contactTiers);

        //on ajoute le contact au tiers
        tiers.getContacts().add(contact);

        //On sauvegarde pour récupérer l'id du contact
        contact = contactTiersDao.save(contact);

        tiersDao.save(tiers);

        return mapper.entityToDto(contact);
    }

    @Override
    public ContactTiers updateContactTiers(Long tiersId, @Valid ContactTiers contactTiers) {
        if (!contactTiersRightsHelper.checkCanUpdateContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'éditer un contact de tiers");
        }

        ContactTiersEntity contactTiersEntity = mapper.dtoToEntity(getContactTiersById(tiersId, contactTiers.getId()));
        mapper.dtoToEntity(contactTiers, contactTiersEntity);

        applyFonctionToContact(contactTiersEntity, contactTiers);
        ContactTiersEntity entity = contactTiersDao.save(contactTiersEntity);
        return mapper.entityToDto(entity);

    }

    @Override
    public ContactTiers inactivateContactTiers(Long tiersId, long contactTiersId) throws AppServiceException {
        if (!contactTiersRightsHelper.checkCanUpdateContactTiers()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits d'éditer un contact de tiers");
        }
        ContactTiersEntity contactTiers = mapper.dtoToEntity(getContactTiersById(tiersId, contactTiersId));

        contactTiers.setDateInactif(new Date());

        try {
            return mapper.entityToDto(contactTiersDao.save(contactTiers));
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactif le contact tiers " + contactTiersId, e);
        }
    }

    @Override
    public Page<ContactTiers> searchContactTiers(ContactTiersCriteria criteria, Pageable pageable) {
        return mapper.entitiesToDto(contactTiersCustomDao.searchContactTiers(criteria, pageable), pageable);
    }


    private void applyFonctionToContact(ContactTiersEntity entity, ContactTiers contactTiers){

        Optional<FonctionContactsEntity> fonction = fonctionContactsDao.findById(contactTiers.getFonctionContact().getId());

        if(fonction.isEmpty()){
            throw new NoSuchElementException(MessageFormat.format(FONCTION_NOT_FOUND, contactTiers.getFonctionContact()));
        }else{
            entity.setFonctionContact(fonction.get());
        }
    }


    private TiersEntity getTiers(Long tiersId){
        Optional<TiersEntity> tiersEntity = tiersDao.findById(tiersId);
        if(tiersEntity.isEmpty()){
            throw new NoSuchElementException(MessageFormat.format(TIERS_NOT_FOUND, tiersId));
        }

        return tiersEntity.get();
    }
}
