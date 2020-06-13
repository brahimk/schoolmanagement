import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IFeuillePresence, FeuillePresence } from 'app/shared/model/feuille-presence.model';
import { FeuillePresenceService } from './feuille-presence.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IProfesseur } from 'app/shared/model/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/professeur.service';
import { IEleve } from 'app/shared/model/eleve.model';
import { EleveService } from 'app/entities/eleve/eleve.service';

type SelectableEntity = IProfesseur | IEleve;

@Component({
  selector: 'jhi-feuille-presence-update',
  templateUrl: './feuille-presence-update.component.html'
})
export class FeuillePresenceUpdateComponent implements OnInit {
  isSaving = false;
  professeurs: IProfesseur[] = [];
  eleves: IEleve[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    present: [],
    description: [],
    comment: [],
    professeur: [],
    eleve: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected feuillePresenceService: FeuillePresenceService,
    protected professeurService: ProfesseurService,
    protected eleveService: EleveService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feuillePresence }) => {
      if (!feuillePresence.id) {
        const today = moment().startOf('day');
        feuillePresence.date = today;
      }

      this.updateForm(feuillePresence);

      this.professeurService.query().subscribe((res: HttpResponse<IProfesseur[]>) => (this.professeurs = res.body || []));

      this.eleveService.query().subscribe((res: HttpResponse<IEleve[]>) => (this.eleves = res.body || []));
    });
  }

  updateForm(feuillePresence: IFeuillePresence): void {
    this.editForm.patchValue({
      id: feuillePresence.id,
      date: feuillePresence.date ? feuillePresence.date.format(DATE_TIME_FORMAT) : null,
      present: feuillePresence.present,
      description: feuillePresence.description,
      comment: feuillePresence.comment,
      professeur: feuillePresence.professeur,
      eleve: feuillePresence.eleve
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
    const feuillePresence = this.createFromForm();
    if (feuillePresence.id !== undefined) {
      this.subscribeToSaveResponse(this.feuillePresenceService.update(feuillePresence));
    } else {
      this.subscribeToSaveResponse(this.feuillePresenceService.create(feuillePresence));
    }
  }

  private createFromForm(): IFeuillePresence {
    return {
      ...new FeuillePresence(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      present: this.editForm.get(['present'])!.value,
      description: this.editForm.get(['description'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      professeur: this.editForm.get(['professeur'])!.value,
      eleve: this.editForm.get(['eleve'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeuillePresence>>): void {
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
