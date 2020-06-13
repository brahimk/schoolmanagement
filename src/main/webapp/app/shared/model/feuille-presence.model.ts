import { Moment } from 'moment';
import { IProfesseur } from 'app/shared/model/professeur.model';
import { IEleve } from 'app/shared/model/eleve.model';

export interface IFeuillePresence {
  id?: number;
  date?: Moment;
  present?: boolean;
  description?: any;
  comment?: string;
  professeur?: IProfesseur;
  eleve?: IEleve;
}

export class FeuillePresence implements IFeuillePresence {
  constructor(
    public id?: number,
    public date?: Moment,
    public present?: boolean,
    public description?: any,
    public comment?: string,
    public professeur?: IProfesseur,
    public eleve?: IEleve
  ) {
    this.present = this.present || false;
  }
}
