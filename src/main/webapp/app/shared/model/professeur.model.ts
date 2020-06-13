import { IClasse } from 'app/shared/model/classe.model';
import { IFeuillePresence } from 'app/shared/model/feuille-presence.model';

export interface IProfesseur {
  id?: number;
  nom?: string;
  prenom?: string;
  comment?: string;
  classes?: IClasse[];
  feuillePresences?: IFeuillePresence[];
}

export class Professeur implements IProfesseur {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public comment?: string,
    public classes?: IClasse[],
    public feuillePresences?: IFeuillePresence[]
  ) {}
}
