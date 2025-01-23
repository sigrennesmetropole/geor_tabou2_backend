package rm.tabou2.service.tabou.plh;

import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;

public interface PLHService {

	/**
	 * Permet de créer un type PLH
	 * Les valeurs qui seraient passées dans le TypePLH sont ignorées
	 * La cohérence du TypePLH est vérifiée (pas de fils pour les éléments VALUE
	 * @param typePLH Le TypePLH à créer
	 * @return Le TypePLH créé
	 */
	TypePLH createTypePLH(TypePLH typePLH) throws AppServiceException;

	/**
	 * Permet de récupérer un TypePLH
	 * @param id id du TypePLH
	 * @return le TypePLH
	 */
	TypePLH getTypePLH(int id)throws AppServiceException;

	/**
	 * Permet de mettre à jour un TypePLH
	 * Les valeurs qui seraient passées dans le TypePLH sont ignorées
	 * La cohérence du TypePLH est vérifiée (pas de fils pour les éléments VALUE
	 * @param typePLH Le TypePLH à mettre à jour
	 * @return le TypePLH mis à jour
	 */
	TypePLH updateTypePLH(TypePLH typePLH)throws AppServiceException;

	/**
	 * Permet de supprimer un TypePLH
	 * Possible uniquement si il n'est utilisé par aucun programme
	 * @param id l'id du TypePLH à supprimer
	 */
	void deleteTypePLH(int id)throws AppServiceException;
}
