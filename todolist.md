avant nouvelle partie :
 * modifier le coût des lieutenants pour les rendre un peu moins overkill
 * changer le nom de la galaxie
 * faire un nouveau site
 * mettre un pourcentage sur le coût des dons
 * pourcentage évolution pop :
 	retour = 1
 				+ (type / 2)
 				// chaque niveau de terraformation augmente la progression de population de 1
 				+ (terraformation)
 				+ (calculeMaxPopDeBase(race) / 500)
 				+ Const.RACES_ATMOSPHERES[race][atmosphere]
 				+ Const.RACES_CARACTERISTIQUES[race][Const.RACE_CARACTERISTIQUE_AUGMENTATION_POPULATION];
