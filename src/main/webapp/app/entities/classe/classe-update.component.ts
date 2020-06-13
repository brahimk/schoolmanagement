import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClasse, Classe } from 'app/shared/model/classe.model';
import { ClasseService } from './classe.service';
import { IProfesseur } from 'app/shared/model/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/professeur.service';

@Component({
  selector: 'jhi-classe-update',
  templateUrl: './classe-update.component.html'
})
export class ClasseUpdateComponent implements OnInit {
  isSaving = false;
  professeurs: IProfesseur[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    niveau: [null, [Validators.required]],
    salle: [],
    creneau: [],
    comment: [],
    professeur: []
  });

  constructor(
    protected classeService: ClasseService,
    protected professeurService: ProfesseurService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classe }) => {
      this.updateForm(classe);

      this.professeurService.query().subscribe((res: HttpResponse<IProfesseur[]>) => (this.professeurs = res.body || []));
    });
  }

  updateForm(classe: IClasse): void {
    this.editForm.patchValue({
      id: classe.id,
      nom: classe.nom,
      niveau: classe.niveau,
      salle: classe.salle,
      creneau: classe.creneau,
      comment: classe.comment,
      professeur: classe.professeur
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classe = this.createFromForm();
    if (classe.id !== undefined) {
      this.subscribeToSaveResponse(this.classeService.update(classe));
    } else {
      this.subscribeToSaveResponse(this.classeService.create(classe));
    }
  }

  private createFromForm(): IClasse {
    return {
      ...new Classe(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      niveau: this.editForm.get(['niveau'])!.value,
      salle: this.editForm.get(['salle'])!.value,
      creneau: this.editForm.get(['creneau'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      professeur: this.editForm.get(['professeur'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClasse>>): void {
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

  trackById(index: number, item: IProfesseur): any {
    return item.id;
  }
}
