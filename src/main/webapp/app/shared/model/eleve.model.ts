import { Moment } from 'moment';
import { IFeuillePresence } from 'app/shared/model/feuille-presence.model';
import { IDiscipline } from 'app/shared/model/discipline.model';
import { IFamille } from 'app/shared/model/famille.model';
import { IClasse } from 'app/shared/model/classe.model';

export interface IEleve {
  id?: number;
  nom?: string;
  prenom?: string;
  dateNaissance?: Moment;
  niveauScolaire?: string;
  niveauArabe?: string;
  comment?: string;
  imageContentType?: string;
  image?: any;
  feuillePresences?: IFeuillePresence[];
  disciplines?: IDiscipline[];
  famille?: IFamille;
  classe?: IClasse;
}

export class Eleve implements IEleve {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public dateNaissance?: Moment,
    public niveauScolaire?: string,
    public niveauArabe?: string,
    public comment?: string,
    public imageContentType?: string,
    public image?: any,
    public feuillePresences?: IFeuillePresence[],
    public disciplines?: IDiscipline[],
    public famille?: IFamille,
    public classe?: IClasse
  ) {}
}
