import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IEleve, Eleve } from 'app/shared/model/eleve.model';
import { EleveService } from './eleve.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IFamille } from 'app/shared/model/famille.model';
import { FamilleService } from 'app/entities/famille/famille.service';
import { IClasse } from 'app/shared/model/classe.model';
import { ClasseService } from 'app/entities/classe/classe.service';

type SelectableEntity = IFamille | IClasse;

@Component({
  selector: 'jhi-eleve-update',
  templateUrl: './eleve-update.component.html'
})
export class EleveUpdateComponent implements OnInit {
  isSaving = false;
  familles: IFamille[] = [];
  classes: IClasse[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    dateNaissance: [null, [Validators.required]],
    niveauScolaire: [],
    niveauArabe: [],
    comment: [],
    image: [],
    imageContentType: [],
    famille: [],
    classe: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected eleveService: EleveService,
    protected familleService: FamilleService,
    protected classeService: ClasseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eleve }) => {
      if (!eleve.id) {
        const today = moment().startOf('day');
        eleve.dateNaissance = today;
      }

      this.updateForm(eleve);

      this.familleService.query().subscribe((res: HttpResponse<IFamille[]>) => (this.familles = res.body || []));

      this.classeService.query().subscribe((res: HttpResponse<IClasse[]>) => (this.classes = res.body || []));
    });
  }

  updateForm(eleve: IEleve): void {
    this.editForm.patchValue({
      id: eleve.id,
      nom: eleve.nom,
      prenom: eleve.prenom,
      dateNaissance: eleve.dateNaissance ? eleve.dateNaissance.format(DATE_TIME_FORMAT) : null,
      niveauScolaire: eleve.niveauScolaire,
      niveauArabe: eleve.niveauArabe,
      comment: eleve.comment,
      image: eleve.image,
      imageContentType: eleve.imageContentType,
      famille: eleve.famille,
      classe: eleve.classe
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('schoolManagementApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eleve = this.createFromForm();
    if (eleve.id !== undefined) {
      this.subscribeToSaveResponse(this.eleveService.update(eleve));
    } else {
      this.subscribeToSaveResponse(this.eleveService.create(eleve));
    }
  }

  private createFromForm(): IEleve {
    return {
      ...new Eleve(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNaissance: this.editForm.get(['dateNaissance'])!.value
        ? moment(this.editForm.get(['dateNaissance'])!.value, DATE_TIME_FORMAT)
        : undefined,
      niveauScolaire: this.editForm.get(['niveauScolaire'])!.value,
      niveauArabe: this.editForm.get(['niveauArabe'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      classe: this.editForm.get(['classe'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEleve>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
