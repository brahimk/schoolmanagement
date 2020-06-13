import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProfesseur } from 'app/shared/model/professeur.model';

@Component({
  selector: 'jhi-professeur-detail',
  templateUrl: './professeur-detail.component.html'
})
export class ProfesseurDetailComponent implements OnInit {
  professeur: IProfesseur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professeur }) => (this.professeur = professeur));
  }

  previousState(): void {
    window.history.back();
  }
}
