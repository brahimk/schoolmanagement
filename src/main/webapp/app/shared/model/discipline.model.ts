import { Moment } from 'moment';
import { IEleve } from 'app/shared/model/eleve.model';

export interface IDiscipline {
  id?: number;
  date?: Moment;
  description?: any;
  comment?: string;
  eleve?: IEleve;
}

export class Discipline implements IDiscipline {
  constructor(public id?: number, public date?: Moment, public description?: any, public comment?: string, public eleve?: IEleve) {}
}
