import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProfesseur, Professeur } from 'app/shared/model/professeur.model';
import { ProfesseurService } from './professeur.service';

@Component({
  selector: 'jhi-professeur-update',
  templateUrl: './professeur-update.component.html'
})
export class ProfesseurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    comment: []
  });

  constructor(protected professeurService: ProfesseurService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professeur }) => {
      this.updateForm(professeur);
    });
  }

  updateForm(professeur: IProfesseur): void {
    this.editForm.patchValue({
      id: professeur.id,
      nom: professeur.nom,
      prenom: professeur.prenom,
      comment: professeur.comment
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professeur = this.createFromForm();
    if (professeur.id !== undefined) {
      this.subscribeToSaveResponse(this.professeurService.update(professeur));
    } else {
      this.subscribeToSaveResponse(this.professeurService.create(professeur));
    }
  }

  private createFromForm(): IProfesseur {
    return {
      ...new Professeur(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      comment: this.editForm.get(['comment'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfesseur>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
