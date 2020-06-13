import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFamille, Famille } from 'app/shared/model/famille.model';
import { FamilleService } from './famille.service';

@Component({
  selector: 'jhi-famille-update',
  templateUrl: './famille-update.component.html'
})
export class FamilleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    adresse: [],
    codePostal: [],
    ville: [],
    telephone1: [null, [Validators.required]],
    telephone2: [null, [Validators.required]],
    email1: [null, [Validators.required]],
    email2: [],
    comment: []
  });

  constructor(protected familleService: FamilleService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ famille }) => {
      this.updateForm(famille);
    });
  }

  updateForm(famille: IFamille): void {
    this.editForm.patchValue({
      id: famille.id,
      nom: famille.nom,
      prenom: famille.prenom,
      adresse: famille.adresse,
      codePostal: famille.codePostal,
      ville: famille.ville,
      telephone1: famille.telephone1,
      telephone2: famille.telephone2,
      email1: famille.email1,
      email2: famille.email2,
      comment: famille.comment
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const famille = this.createFromForm();
    if (famille.id !== undefined) {
      this.subscribeToSaveResponse(this.familleService.update(famille));
    } else {
      this.subscribeToSaveResponse(this.familleService.create(famille));
    }
  }

  private createFromForm(): IFamille {
    return {
      ...new Famille(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      telephone1: this.editForm.get(['telephone1'])!.value,
      telephone2: this.editForm.get(['telephone2'])!.value,
      email1: this.editForm.get(['email1'])!.value,
      email2: this.editForm.get(['email2'])!.value,
      comment: this.editForm.get(['comment'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamille>>): void {
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
