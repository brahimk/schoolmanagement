import { IEleve } from 'app/shared/model/eleve.model';

export interface IFamille {
  id?: number;
  nom?: string;
  prenom?: string;
  adresse?: string;
  codePostal?: number;
  ville?: string;
  telephone1?: number;
  telephone2?: number;
  email1?: string;
  email2?: string;
  comment?: string;
  eleves?: IEleve[];
}

export class Famille implements IFamille {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public adresse?: string,
    public codePostal?: number,
    public ville?: string,
    public telephone1?: number,
    public telephone2?: number,
    public email1?: string,
    public email2?: string,
    public comment?: string,
    public eleves?: IEleve[]
  ) {}
}
