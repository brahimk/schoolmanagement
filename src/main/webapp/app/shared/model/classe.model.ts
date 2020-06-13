import { IEleve } from 'app/shared/model/eleve.model';
import { IProfesseur } from 'app/shared/model/professeur.model';
import { Salle } from 'app/shared/model/enumerations/salle.model';
import { Creneau } from 'app/shared/model/enumerations/creneau.model';

export interface IClasse {
  id?: number;
  nom?: string;
  niveau?: string;
  salle?: Salle;
  creneau?: Creneau;
  comment?: string;
  eleves?: IEleve[];
  professeur?: IProfesseur;
}

export class Classe implements IClasse {
  constructor(
    public id?: number,
    public nom?: string,
    public niveau?: string,
    public salle?: Salle,
    public creneau?: Creneau,
    public comment?: string,
    public eleves?: IEleve[],
    public professeur?: IProfesseur
  ) {}
}
